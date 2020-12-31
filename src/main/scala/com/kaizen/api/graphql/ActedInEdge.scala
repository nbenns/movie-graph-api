package com.kaizen.api.graphql

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn._
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.controller._
import com.kaizen.api.services.movie.graphql.{Movie, MovieDataSource}
import zio.query.ZQuery

final case class ActedInEdge(actorId: ActorId, movie: ZQuery[MovieController, RepositoryError, Movie])

object ActedInEdge {
  def fromActedInData(data: ActedInData): ActedInEdge =
    ActedInEdge(data.actorId, MovieDataSource.getMovie(data.movieId))

  def fromBatch(actedInData: ActedInData, movies: List[Movie]): List[ActedInEdge] =
    movies.map(m => ActedInEdge(actedInData.actorId, ZQuery.succeed(m)))
}
