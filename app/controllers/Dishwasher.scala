package controllers

import java.util.UUID

import anorm._
import play.api.db.DB
import play.api.mvc._
import play.api.Play.current

object Dishwasher extends Controller {

  def createDishwasher = Action {
    val uuid = UUID.randomUUID()//.toString
    DB.withConnection { implicit c =>
      SQL("insert into Dishwashers(dishwasher_id, isClean) values ({id}, {isClean});").on('id -> Object(uuid), 'isClean -> false).executeInsert()
    }
    Ok(uuid.toString)
  }
  
  def getDishwasher(id: String) = Action {
    // For validation
    val uuid = UUID.fromString(id)
    val isClean = DB.withConnection { implicit c =>
      val selectDishwasher = SQL("Select isClean from Dishwashers where dishwasher_id={id}").on('id -> Object(uuid))
      selectDishwasher().map(x => x[Boolean]("isClean")).head
    }
    Ok(views.html.dishwasher(uuid.toString, isClean))
  }
  
  def toggleDishwasher(id: String) = Action {
    // For validation
    val uuid = UUID.fromString(id)
    
    val isClean = DB.withConnection { implicit c =>
      val selectDishwasher = SQL("Select isClean from Dishwashers where dishwasher_id={id}").on('id -> Object(uuid))
      val isClean = selectDishwasher().map(x => x[Boolean]("isClean")).head
      
      // update dishwasher
      SQL("update Dishwashers set isClean={isClean} where dishwasher_id=({id});").on('id -> Object(uuid), 'isClean -> !isClean).executeUpdate()
      
      !isClean
    }

    Ok(views.html.dishwasher(uuid.toString, isClean))
  }
}
