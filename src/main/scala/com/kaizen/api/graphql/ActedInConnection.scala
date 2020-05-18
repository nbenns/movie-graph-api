package com.kaizen.api.graphql

import com.kaizen.api.internal.RepositoryError
import com.kaizen.api.internal.actedIn.controller.ActedInController
import com.kaizen.api.internal.movie.controller.MovieController
import zquery.ZQuery

final case class ActedInConnection(
  total: ZQuery[ActedInController, RepositoryError, Long],
  before: Option[String],
  after: Option[String],
  edges: ZQuery[ActedInController with MovieController, RepositoryError, List[ActedIn]]
)
