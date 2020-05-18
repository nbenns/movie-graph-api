package com.kaizen.api

import com.kaizen.api.actedIn.controller._
import com.kaizen.api.actor.{ActorData, ActorId, ActorName}

final case class Actor(
  id: ActorId,
  name: ActorName,
  actedIn: PageParams => ActedInConnection
)

object Actor {
  def fromActorData(data: ActorData): Actor =
    Actor(
      data.id,
      data.name,
      { pageParams =>
        ActedInConnection(
          countMoviesActedIn(CountMoviesActedIn(data.id)),
          None,
          None,
          getMoviesActedIn(pageParams.count)(GetMoviesActedIn(data.id)).map(_.map(ActedIn.fromActedInData))
        )
      }
    )
}
