package controllers

import org.joda.time.DateTime
import play.api._
import play.api.mvc._
import play.api.db.DB
import anorm._
import play.api.Play.current

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def postEvent = Action {
    val events = logAndFetchEvent
    Ok(events.toString())
  }
  
  def getEvents = Action {
    val events = fetchEvents
    Ok(events.toString())
  }
  
  def logAndFetchEvent() = DB.withConnection { implicit c =>
    val result = SQL("insert into Events(eventTime) values ({timestamp})").on('timestamp -> DateTime.now().toString()).executeInsert()
    val selectEvents = SQL("Select * from Events")
    selectEvents().map(x => x[String]("eventTime")).toList
  }
  
  def fetchEvents = DB.withConnection { implicit c =>
    val selectEvents = SQL("Select * from Events")
    selectEvents().map(x => x[String]("eventTime")).toList
  }
}