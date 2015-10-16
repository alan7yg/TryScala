package com.kaihaosw.actors

import akka.actor.Actor
import scala.util.Random

object Horse {
  case object Run
  case class Observe(id: String, track: String)
}

class HorseActor(id: String) extends Actor {
  import Horse._

  var track = ""

  def randomStep: Int = (new Random()).nextInt(3)

  def run(): Unit = {
    track += "*" * randomStep
    println(this)
  }

  override def toString = "Horse " + id + " " + track

  def receive: Receive = {
    case Run =>
      run()
      sender ! Observe(id, track)
  }
}
