package com.kaizen.api.actor

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.ActedIn
import com.kaizen.api.actedIn.controller.{ActedInController, CountMoviesActedIn, GetMoviesActedIn, countMoviesActedIn, getMoviesActedIn}
import com.kaizen.api.actor.repository.{ActorData, ActorRepository}
import com.kaizen.api.movie.controller.MovieController
import zquery.ZQuery

final case class PageParms(count: Long, before: Option[String], after: Option[String])

final case class ActedInConnection(
  total: ZQuery[ActedInController, RepositoryError, Long],
  before: Option[String],
  after: Option[String],
  edges: ZQuery[ActedInController with MovieController, RepositoryError, List[ActedIn]]
)

final case class Actor(
  id: ActorId,
  name: ActorName,
  actedIn: PageParms => ActedInConnection
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
          getMoviesActedIn(pageParams.count)(GetMoviesActedIn(data.id))
        )
      }
    )
}
