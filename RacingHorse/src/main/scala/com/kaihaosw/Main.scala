package com.kaihaosw

import akka.actor.{ActorSystem, Props}
import com.kaihaosw.actors._

object RacingHorseApplication {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("RacingHorseSystem")
    val gameActor = system.actorOf(Props[GameActor], "Game")

    import Horse._
    import Game._

    gameActor ! Start
  }
}
