package com.kaihaosw.local

import akka.actor.ActorSystem
import akka.actor.Props
import com.typesafe.config.ConfigFactory
import java.io.File

object RacingHorseApplication {
  def main(args: Array[String]): Unit = {
    val configFile = getClass.getClassLoader. getResource("local_application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("RacingHorseSystem", config)
    val gameStationActor = system.actorOf(Props[GameStationActor], name="GameStationActor")

    gameStationActor ! GameStation.Start
  }
}
