package com.kaizen.api.graphql

import com.kaizen.api.internal.RepositoryError
import com.kaizen.api.internal.actedIn._
import com.kaizen.api.internal.actor.ActorId
import com.kaizen.api.internal.movie.MovieData
import com.kaizen.api.internal.movie.controller._
import zquery.ZQuery

final case class ActedIn(actorId: ActorId, movie: ZQuery[MovieController, RepositoryError, MovieData])

object ActedIn {
  def fromActedInData(data: ActedInData): ActedIn =
    ActedIn(data.actorId, getMovie(GetMovie(data.movieId)))
}
