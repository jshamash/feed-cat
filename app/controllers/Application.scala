package controllers

import java.sql.Connection

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api._
import play.api.mvc._
import play.api.db.DB
import anorm._
import play.api.Play.current

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }
  
  def meals = Action {
    Ok(views.html.meals())
  }
  
  def cat = Action {
    Ok(views.html.cat())
  }
  
  def postEvent = Action {
    val events = DB.withConnection { implicit c =>
      logEvent
      fetchEvents
    }
    Ok(events.map(formatTime).mkString("\n"))
  }
  
  def getEvents = Action {
    val events = DB.withConnection { implicit c =>
      fetchEvents
    }
    Ok(events.map(formatTime).mkString("\n"))
  }
  
  def logEvent(implicit c: Connection) = SQL("insert into Events(eventTime) values ({timestamp})").on('timestamp -> DateTime.now().toString()).executeInsert()
  
  def fetchEvents(implicit c: Connection) = {
    val selectEvents = SQL("Select * from Events")
    selectEvents().map(x => x[String]("eventTime")).map(time => new DateTime(time)).toList
  }
  
  def formatTime(time: DateTime): String = DateTimeFormat.fullDateTime().print(time)
}