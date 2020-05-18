package com.kaizen.api.graphql

import com.kaizen.api.services.actedIn.controller._
import com.kaizen.api.services.actor._
import com.kaizen.api.graphql

final case class Actor(
  id: ActorId,
  name: ActorName,
  portfolio: PageParams => PortfolioConnection
)

object Actor {
  def fromActorData(data: ActorData): Actor =
    Actor(
      data.id,
      data.name,
      { pageParams =>
        PortfolioConnection(
          countMoviesActedIn(CountMoviesActedIn(data.id)),
          None,
          None,
          getMoviesActedIn(pageParams.count)(GetMoviesActedIn(data.id)).map(_.map(ActedIn.fromActedInData))
        )
      }
    )
}
