package com.kaihaosw.actors

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import scala.collection.mutable.MutableList
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object Game {
  case object Start
}

class GameActor extends Actor {
  import Horse._
  import Game._

  implicit val timeout = Timeout(5 seconds)
  val horseNumber = 5
  val trackLength = 100

  var horseActorList = MutableList.empty[ActorRef]
  for (n <- 1 to horseNumber)
    horseActorList += context.actorOf(Props[HorseActor], "horse-" + n)

  def startGame(): Future[Unit] = {
    var state = Future.sequence(horseActorList.map(actor => (actor ? Run).mapTo[Observe]))
    val isExist = state.map {obList =>
      obList.exists { o =>
        o.track.length >= trackLength
      }
    }
    isExist.map { exist =>
      if (!exist) startGame()
    }
  }

  def receive: Receive = {
    case Start => startGame()
  }
}
