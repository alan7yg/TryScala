package com.kaihaosw.actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props}
import akka.routing.{BroadcastRouter, RandomRouter}

object Game {
  case object Start
}

class GameActor extends Actor {
  import Horse._
  import Game._

  val horseNumber = 5
  val trackLength = 100
  var observeCount = 0
  var winCount = 0

  val horseActorVector: Vector[ActorRef] =
    (1 to horseNumber).map(n => context.actorOf(Props(new HorseActor(n.toString)), "horse-" + n)).toVector

  val broadcastHorses = context.actorOf(Props.empty.withRouter(BroadcastRouter(routees = horseActorVector)))

  def broadcastRun: PartialFunction[Any, Unit] = {
    case o: Observe =>
      observeCount += 1
      if (o.track.length >= trackLength) {
        winCount += 1
        broadcastHorses ! PoisonPill
        if (winCount == 1)
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

  val randomHorses = context.actorOf(Props.empty.withRouter(RandomRouter(routees = horseActorVector)))

  def randomRun: PartialFunction[Any, Unit] = {
    case o: Observe =>
      if (o.track.length >= trackLength) {
        broadcastHorses ! PoisonPill
        println(o.id + " wins")
        context.system.shutdown
      }
      else
        randomHorses ! Run
    case Start =>
      randomHorses ! Run
  }

  def receive: Receive = broadcastRun
}
