package com.kaihaosw.manager

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object ManagerApplication {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("DRHManagerSystem",
      ConfigFactory.load("manager"))
    val manager = system.actorOf(Props(new ManagerActor(1)), "manager")
  }
}
