package com.kaihaosw.actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}

object Game {
  case object Start
}

class GameActor extends Actor {
  import Horse._
  import Game._

  val horseNumber = 5
  val trackLength = 100

  val horseActorList: IndexedSeq[ActorRef] =
    (1 to horseNumber).map(n => context.actorOf(Props(new HorseActor(n.toString)), "horse-" + n))

  def receive: Receive = {
    case o: Observe =>
      if (o.track.length >= trackLength) {
        horseActorList.foreach { horse =>
          horse ! PoisonPill
        }
        println(o.id + " wins")
        context.system.shutdown
      } else
        sender ! Run
    case Start =>
      horseActorList.map { horse =>
        horse ! Run
      }
  }
}
