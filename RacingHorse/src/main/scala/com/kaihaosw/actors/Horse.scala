package com.kaihaosw.actors

import akka.actor.Actor
import scala.util.Random

object Horse {
  case object Run
  case class Observe(track: String)
}


class HorseActor extends Actor {
  import Horse._

  var counter = 0
  var track = " "

  def randomStep: Int = (new Random()).nextInt(3)

  def run(): Unit = {
    track += "*" * randomStep
    println(this)
  }

  override def toString = "Horse " + this.hashCode + track

  def receive: Receive = {
    case Run =>
      run()
      sender ! Observe(track)
  }
}
