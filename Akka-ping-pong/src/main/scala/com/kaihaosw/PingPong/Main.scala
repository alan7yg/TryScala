package com.kaihaosw.PingPong

import akka.actor.ActorSystem

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("PingPongSystem")
    val pingActor = system.actorOf(PingActor.props, "PingActor")
    pingActor ! PingActor.Initialize

    system.awaitTermination()
  }
}
