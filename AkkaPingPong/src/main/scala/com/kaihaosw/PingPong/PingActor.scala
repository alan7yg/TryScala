package com.kaihaosw.PingPong

import akka.actor.{ Actor, ActorLogging, Props }

class PingActor extends Actor with ActorLogging {
  import PingActor._

  var count = 0

  val pongActor = context.actorOf(PongActor.props, "PongActor")

  def receive = {
    case Initialize =>
      log.info("PingPong started!")
      pongActor ! PingMessage("PingActor Started")

    case PongActor.PongMessage(msg) =>
      log.info(s"In PingActor. Message: {} !", msg)
      count += 1
      if (count <= 3)
        sender ! PingMessage("Ping")
      else
        context.system.shutdown()
  }
}

object PingActor {
  val props = Props[PingActor]
  case object Initialize
  case class PingMessage(msg: String)
}
