package com.kaizen.api.actor

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.ActedIn
import com.kaizen.api.actedIn.controller.ActedInController
import com.kaizen.api.movie.controller.MovieController
import zquery.ZQuery

final case class ActedInConnection(
  total: ZQuery[ActedInController, RepositoryError, Long],
  before: Option[String],
  after: Option[String],
  edges: Long => ZQuery[ActedInController with MovieController, RepositoryError, List[ActedIn]]
)

final case class Actor(
  id: ActorId,
  name: ActorName,
  actedIn: ActedInConnection
)
