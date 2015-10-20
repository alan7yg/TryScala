package com.kaihaosw.game

import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory
import java.io.File

object GameStationApplication {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("DRHGameStationSystem", 
      ConfigFactory.load("game-station"))
    val gameStationActor = system.actorOf(Props[GameStationActor], "gameStationActor")

    gameStationActor ! GameStationMsg.Init
    // gameStationActor ! GameStationMsg.Start
  }
}
