package com.kaizen.api.graphql

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn._
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieData
import com.kaizen.api.services.movie.controller._
import zquery.ZQuery

final case class ActedIn(actorId: ActorId, movie: ZQuery[MovieController, RepositoryError, MovieData])

object ActedIn {
  def fromActedInData(data: ActedInData): ActedIn =
    ActedIn(data.actorId, getMovie(GetMovie(data.movieId)))
}
