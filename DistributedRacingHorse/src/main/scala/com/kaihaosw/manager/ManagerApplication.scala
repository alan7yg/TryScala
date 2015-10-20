package com.kaihaosw.manager

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object ManagerApplication {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("DRHManagerSystem",
      ConfigFactory.load("manager"))
    /*
    val gameStationVector = new List(
        system.actorSelection("akka.tcp//DistributedRacingHorseGameSystem@192.168.199.205:2552/user/game-station")
      ).toVector
    */
    val manager = system.actorOf(Props(new ManagerActor(1)), "manager")
  }
}
