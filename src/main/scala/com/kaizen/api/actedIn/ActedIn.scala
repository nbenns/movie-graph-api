package com.kaizen.api.actedIn

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.repository.ActedInData
import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.Movie
import com.kaizen.api.movie.controller._
import zquery.ZQuery

final case class ActedIn(actorId: ActorId, movie: ZQuery[MovieController, RepositoryError, Movie])

object ActedIn {
  def fromActedInData(data: ActedInData): ActedIn =
    ActedIn(data.actorId, getMovie(GetMovie(data.movieId)))
}
