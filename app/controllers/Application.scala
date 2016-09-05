package controllers

import javax.inject._

import play.api.libs.ws._
import slick.codegen.SourceCodeGenerator
import slick.driver.{PostgresDriver, SQLiteDriver}
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
  def index = Action {
    val model = dbConfig.db.run(PostgresDriver.createModel())
    model.onSuccess{case model =>
    }
    Ok(views.html.index(null))
    }

  def codeGen =Action {
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


  def doRegister = Action { request =>
    val name = request.body.asFormUrlEncoded.get("name")
    Ok("")
    //Ok(views.html.doRegister(null,null,null,null))
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
