package controllers

import javax.inject._
import play.api.libs.ws._
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
