package models

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}

case class Meal(timestamp: DateTime) {
  override def toString = DateTimeFormat.fullDateTime().print(timestamp.withZone(DateTimeZone.forID("America/Montreal")))
}

object Meal {
  implicit def mealOrdering: Ordering[Meal] = Ordering.fromLessThan(_.timestamp isBefore _.timestamp)
}
