package controllers

import java.sql.Connection

import anorm._
import models.Meal
import org.joda.time.DateTime
import play.api.Play.current
import play.api.db.DB
import play.api.mvc._

object Cat extends Controller {
  def getMeals = Action { request =>
    val events = DB.withConnection { implicit c =>
      fetchEvents
    }
    Ok(views.html.meals(latestMeal(events)))
  }

  def cat = Action {
    Ok(views.html.cat())
  }

  def postMeal = Action { request =>
    val events = DB.withConnection { implicit c =>
      logEvent
      fetchEvents
    }
    Ok(views.html.meals(latestMeal(events)))
  }

  def latestMeal(events: List[DateTime]) = {
    import Meal._
    events.map(Meal(_)).sorted.reverse.head
  }

  def logEvent(implicit c: Connection) = SQL("insert into Events(eventTime) values ({millis});").on('millis -> DateTime.now().getMillis).executeInsert()

  def fetchEvents(implicit c: Connection) = {
    val selectEvents = SQL("Select * from Events")
    selectEvents().map(x => x[Long]("eventTime")).map(millis => new DateTime(millis)).toList
  }
}
