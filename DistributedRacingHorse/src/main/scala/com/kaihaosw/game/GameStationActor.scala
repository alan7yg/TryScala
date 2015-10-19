package com.kaihaosw.game

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.routing.BroadcastRouter

object GameStationMsg {
  case object Init
  case object Start
  case class Result(winner: String)
}

class GameStationActor extends Actor {
  import Horse._
  import GameStationMsg._

  val horseNumber = 5
  val trackLength = 100
  var observeCount = 0

  val managerActor = context.actorSelection("akka.tcp://DRHManagerSystem@127.0.0.1:2553/user/manager")

  val horseActorVector: Vector[ActorRef] =
    (1 to horseNumber).map(n => context.actorOf(Props(new HorseActor(n.toString)), "horse-" + n)).toVector

  val broadcastHorses = context.actorOf(Props.empty.withRouter(BroadcastRouter(routees = horseActorVector)))

  def receive: Receive = {
    case o: Observe =>
      observeCount += 1
      if (o.trackLength >= trackLength) {
        broadcastHorses ! PoisonPill
        println(o.id + " wins")
        managerActor ! Result(o.id)
        context.system.shutdown
      }
      if (observeCount == horseNumber) {
        observeCount = 0
        println()
        broadcastHorses ! Run
      }
    case Start =>
      broadcastHorses ! Run
    case Init =>
      managerActor ! Init
  }
}
