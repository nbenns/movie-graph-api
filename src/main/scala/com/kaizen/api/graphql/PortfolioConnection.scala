package com.kaizen.api.graphql

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.controller.ActedInController
import com.kaizen.api.services.movie.controller.MovieController
import zquery.ZQuery

final case class PortfolioConnection(
  total: ZQuery[ActedInController, RepositoryError, Long],
  before: Option[String],
  after: Option[String],
  movies: ZQuery[ActedInController with MovieController, RepositoryError, List[ActedIn]]
)
