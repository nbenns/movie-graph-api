package com.kaizen.api

import com.kaizen.api.actedIn.controller.ActedInController
import com.kaizen.api.movie.controller.MovieController
import zquery.ZQuery

final case class ActedInConnection(
  total: ZQuery[ActedInController, RepositoryError, Long],
  before: Option[String],
  after: Option[String],
  edges: ZQuery[ActedInController with MovieController, RepositoryError, List[ActedIn]]
)
