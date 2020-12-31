package com.kaizen.api.graphql

import com.kaizen.api.graphql.datasource.ActedInDataSource
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.controller.ActedInController
import com.kaizen.api.services.actor.ActorData
import com.kaizen.api.services.movie.controller.MovieController
import zio.query.ZQuery

final case class PortfolioConnection(
  total: ZQuery[ActedInController, RepositoryError, Long],
  before: Option[String],
  after: Option[String],
  movies: ZQuery[ActedInController with MovieController, RepositoryError, List[ActedInEdge]]
)

object PortfolioConnection {
  def from(actor: ActorData): PageParams => PortfolioConnection = { pageParams =>
    PortfolioConnection(
      total = ActedInDataSource.countMoviesActedIn(actor.id),
      before = None,
      after = None,
      movies = ActedInDataSource.getMoviesActedIn(actor.id)
    )
  }
}
