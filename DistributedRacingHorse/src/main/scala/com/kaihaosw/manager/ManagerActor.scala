package com.kaihaosw.manager

import akka.actor.{Actor, ActorRef, Props, PoisonPill}
import akka.routing.BroadcastRouter 

class ManagerActor(gameSize: Int) extends Actor {
  var gameCount = 0
  var gameStations: List[ActorRef] = List.empty[ActorRef]
  var gameStationsRouter = context.actorOf(
    Props.empty.withRouter(BroadcastRouter(routees = gameStations)))

  import com.kaihaosw.game.GameStationMsg.{Init => GameInit, Start => GameStart, Result => GameResult, Shut => GameShut}

  def receive: Receive = {
    case GameInit =>
      gameStations = sender :: gameStations
      gameCount = gameCount + 1
      if (gameCount >= gameSize) {
        gameStationsRouter = context.actorOf(
          Props.empty.withRouter(BroadcastRouter(routees = gameStations)))
        gameStationsRouter ! GameStart
      }
    case r: GameResult =>
      println(s"Game has finished firstly by ${sender} with result: ${r}")
      gameStationsRouter ! GameShut
      gameCount = 0
      gameStations = List.empty[ActorRef]
  }
}
