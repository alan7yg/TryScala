package com.kaihaosw.game

import akka.actor.Actor
import scala.util.Random

object Horse {
  case object Run
  case class Observe(id: String, trackLength: Int)
}

class HorseActor(id: String) extends Actor {
  import Horse._

  var track = ""
  val random = new Random()
  def randomStep = random.nextInt(3)

  def run(): Unit = {
    track += "*" * randomStep
    println(this)
  }

  override def toString: String = s"Horse ${id} ${track}"

  def receive: Receive = {
    case Run =>
      run()
      sender ! Observe(id, track.length)
  }
}
