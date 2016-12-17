package controllers

import java.sql.DriverAction
import javax.inject._

import models.Tables
import models.Tables.UsersRow
import play.api.libs.ws._
import slick.codegen.SourceCodeGenerator
import slick.driver
import slick.driver.{PostgresDriver, SQLiteDriver}

import scala.concurrent.Future
//import play.api._
//import play.api.mvc._
//import play.api.cache.Cache
//import play.api.Play.current
//import play.api.db._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick._
/*
import com.typesafe.slick.driver.postgresql.Driver.simple._
import scala.slick.driver.ExtendedDriver
import scala.slick.lifted.ColumnOption.PrimaryKey
import scala.slick.session.Database*/
//import play.api.db.DB
//import play.api.Play.current


import play.api._
import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.lifted.Tag
import slick.driver.PostgresDriver.api._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global


class Application @Inject()(dbConfigProvider: DatabaseConfigProvider) extends Controller {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig.driver.api._
  def connected(request:Request[AnyContent]):Boolean = {
    val session = request.session
    session.get("email").map { email =>
      session.get("sessionKey").map { sessionKey =>
        CrapSesh.stillValid(email, sessionKey)
      }
    } match {
      case None => false
      case Some(None) => false
      case Some(Some(b)) => b
    }
  }

  def index = Action { request =>
    if(connected(request)) {
      Ok(views.html.index("Congrats you're logged in!"))
    } else {
      Ok(views.html.index("You ain't logged no in-wise"))
    }
  }

  def register = Action {
    Ok(views.html.register(null))
  }

  def codeGen = Action {
    //val db = SQLiteDriver.simple.Database.forURL("jdbc:postgresql://localhost/callforpartners")
    val session = dbConfig.db.createSession()
    val model = dbConfig.db.run(PostgresDriver.createModel())
    //val model = dbConfig.db.withSession({ implicit session => SQLiteDriver.createModel()})
    model.onSuccess{case model =>
      /** Works around a bug in Slick which causes its code generator to not mark auto-incremented columns as such.
        * Since the row id (named _ID) is auto-incremented (or technically allocated uniquely in a way which usually
        * coincides with auto-incrementing) we can safely say that column should always be auto-increment.*/
      val FixedCodeGenerator = new SourceCodeGenerator (model) {
        override def Table = new Table(_) {
          override def Column = new Column(_) {
            /* Slick's code generator tries very hard to prevent us from just saying the column is auto-increment (the
            * autoInc method is final), so instead we look through the generated code for the column options and add an
            * AutoInc option.
            *
            * This is admittedly cryptic and hacky. If you are confused, read the Tables.scala file output by the
            * generator. */
            override def code = {
              if (name == "_Id") {
                super.code.replaceFirst("O.PrimaryKey", "O.PrimaryKey, O.AutoInc")
              } else {
                super.code
              }
            }
          }
        }
      }
      FixedCodeGenerator.writeToFile(
        "slick.driver.PostgresDriver","app/models",
        "models","Tables","Tables.scala")
    }
    Ok(views.html.index(null))
  }

  def editProfile = Action { request =>
    Ok(views.html.editProfile())
  }

  def userIdOfEmail(email:String) = {
    val query = Tables.Users.filter(_.email === email).result
    dbConfig.db.run(query)
  }

  def hasProfile(id:Int):Future[Boolean] = {
    val query = Tables.Users.filter(_.id === id).result
    dbConfig.db.run(query).map(_.nonEmpty)
  }


  def doEditProfile = Action.async { request =>
    val readableName = request.body.asFormUrlEncoded.get("name").head
    val age = request.body.asFormUrlEncoded.get("age").headOption
    val sex = request.body.asFormUrlEncoded.get("sex").headOption
    val name = request.body.asFormUrlEncoded.get("name").headOption
    val location = request.body.asFormUrlEncoded.get("location").headOption
    val academicRank = request.body.asFormUrlEncoded.get("academicRank").headOption
    val field = request.body.asFormUrlEncoded.get("field").headOption
    val concentration = request.body.asFormUrlEncoded.get("concentration").headOption
    val thesisTitle = request.body.asFormUrlEncoded.get("thesisTitle").headOption
    val thesisAdvisor = request.body.asFormUrlEncoded.get("thesisAdvisor").headOption
    val publications = request.body.asFormUrlEncoded.get("publications").headOption
    val aboutYou = request.body.asFormUrlEncoded.get("aboutYou").headOption
    val email:String = request.session("email")
    val sesh = request.session("sessionKey")
    userIdOfEmail(email).flatMap { case userId =>
      def editProfile() = {
        val id:Int = userId.head.id
        hasProfile(id).map { case canHas =>
          if (canHas) {
            val query =
              Tables.Profiles
                .filter(_.id === id)
                .map(r =>
                  (r.concentration, r.field, r.academicrank, r.location, r.sex, r.thesistitle, r.thesisadvisor, r.publications, r.aboutyou, r.user, r.name))
                .update((concentration, field, academicRank, location, sex, thesisTitle, thesisAdvisor, publications, aboutYou, id, name))
            dbConfig.db.run(query)

          } else {
            val query =
              (Tables.Profiles.map(r =>
                (r.concentration, r.field, r.academicrank, r.location, r.sex, r.thesistitle, r.thesisadvisor, r.publications, r.aboutyou, r.user, r.name)) returning Tables.Profiles.map(_.id))
                .+=((concentration, field, academicRank, location, sex, thesisTitle, thesisAdvisor, publications, aboutYou, id, name))
            dbConfig.db.run(query)
          }
        }

      }
      editProfile().map({ case _ => Redirect("editProfile")})
    }
  }

  def doLogin = Action.async { request =>
    val email = request.body.asFormUrlEncoded.get("email").head
    val password = request.body.asFormUrlEncoded.get("password").head
    dbConfig.db.run(Tables.Users.filter(_.email === email).result)
      .map {case matchingUsers =>
        val available = matchingUsers.isEmpty
        if (available) {
          Ok(views.html.index("Error: Account does not exist or password incorrect: " + email))
        } else {
          val hash = matchingUsers.head.passwordhash
          val salt = matchingUsers.head.passwordsalt
          val iter = matchingUsers.head.passworditerations
          val newHash = Password.hash(password.toCharArray, salt.getBytes("UTF-8"), iter)
          val matches = Password.safeEquals(hash, newHash)
          if (matches) {
            val sessionKey = CrapSesh.login(email)
            Redirect("editProfile").withSession(
              request.session + ("email" -> email) + ("sessionKey" -> sessionKey))
          } else {
            Ok(views.html.index("Error: Account does not exist or password incorrect: " + email))
          }
        }
      }
  }

  def doRegister = Action.async { request =>
    val email = request.body.asFormUrlEncoded.get("email").head
    val password = request.body.asFormUrlEncoded.get("password").head
    val confirmPassword = request.body.asFormUrlEncoded.get("confirmPassword").head
    val passwordsMatch = Password.safeEquals(password, confirmPassword)
    def createAccount() = {
      val iterations = 10000
      val saltLength = 512
      val (hash, salt) = Password.generateKey(password, iterations, saltLength)
      val query =

        (Tables.Users.map(r => (r.email, r.passwordhash, r.passwordsalt, r.passworditerations)) returning Tables.Users.map(_.id)).+=((email, hash, salt, iterations))
      dbConfig.db.run(query)
    }
    dbConfig.db.run(Tables.Users.filter(_.email === email).result)
    .map {case matchingUsers =>
      val available = matchingUsers.isEmpty
      if (!available) {
        Ok(views.html.index("Error: Account already exists: " + email))
      } else if (!passwordsMatch) {
        Ok(views.html.index("Error: Passwords did not match"))
      } else {
        createAccount()
        val sessionKey = CrapSesh.login(email)
        Redirect("editProfile").withSession(
          request.session + ("email" -> email) + ("sessionKey" -> sessionKey))
      }
    }
  }

  def db = Action.async { implicit request =>
    /*dbConfig.db.run()
    var out = ""
    val conn = DB.getConnection()
    try {
      val stmt = conn.createStatement

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())")

      val rs = stmt.executeQuery("SELECT tick FROM ticks")

      while (rs.next) {
        out += "Read from DB: " + rs.getTimestamp("tick") + "\n"
      }
    } finally {
      conn.close()
    }*/
    Future {Ok("")}
  }
}
