package com.kaihaosw.manager

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.BroadcastRouter 

class ManagerActor(gameSize: Int) extends Actor {
  var gameCount = 0
  var gameStations: List[ActorRef] = List.empty[ActorRef]

  import com.kaihaosw.game.GameStationMsg.{Init => GameInit, Start => GameStart, Result => GameResult}

  def receive: Receive = {
    case GameInit =>
      gameStations = sender :: gameStations
      gameCount = gameCount + 1
      if (gameCount >= gameSize) {
        var gameStationsRouter = context.actorOf(
          Props.empty.withRouter(BroadcastRouter(routees = gameStations)))
        gameStationsRouter ! GameStart
      }
    case r: GameResult =>
      println(s"Game has finished firstly by ${r}")
  }
}
