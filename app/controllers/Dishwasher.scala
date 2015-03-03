package controllers

import controllers.Application._
import play.api._
import play.api.mvc._

object Dishwasher extends Controller {

  def getDishwasher(id: Int) = Action {
    Ok(id)
  }
}
