package com.kaizen.api.graphql

import com.kaizen.api.services.actor._

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
      PortfolioConnection.from(data)
    )
}
