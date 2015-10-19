package com.kaihaosw.local

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.routing.BroadcastRouter

object GameStation {
  case object Start
}

class GameStationActor extends Actor {
  import Horse._
  import GameStation._

  val horseNumber = 5
  val trackLength = 100
  var observeCount = 0

  val horseActorVector: Vector[ActorRef] =
    (1 to horseNumber).map(n => context.actorOf(Props(new HorseActor(n.toString)), "horse-" + n)).toVector

  val broadcastHorses = context.actorOf(Props.empty.withRouter(BroadcastRouter(routees = horseActorVector)))

  def receive: Receive = {
    case o: Observe =>
      observeCount += 1
      if (o.trackLength >= trackLength) {
        broadcastHorses ! PoisonPill
        println(o.id + " wins")
        context.system.shutdown
      }
      if (observeCount == horseNumber) {
        observeCount = 0
        println()
        broadcastHorses ! Run
      }
    case Start =>
      broadcastHorses ! Run
  }
}
