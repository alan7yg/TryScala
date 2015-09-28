package com.kaihaosw.PingPong

import akka.actor.{ Actor, ActorLogging, Props }

class PongActor extends Actor with ActorLogging {
  import PongActor._

  def receive = {
    case PingActor.PingMessage(msg) =>
      log.info(s"In PongActor. Message: {} !", msg)
      sender ! PongMessage("Pong")
  }
}

object PongActor {
  val props = Props[PongActor]
  case class PongMessage(msg: String)
}
