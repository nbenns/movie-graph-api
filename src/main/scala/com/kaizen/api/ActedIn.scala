package com.kaizen.api

import com.kaizen.api.actedIn.ActedInData
import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.MovieData
import com.kaizen.api.movie.controller._
import zquery.ZQuery

final case class ActedIn(actorId: ActorId, movie: ZQuery[MovieController, RepositoryError, MovieData])

object ActedIn {
  def fromActedInData(data: ActedInData): ActedIn =
    ActedIn(data.actorId, getMovie(GetMovie(data.movieId)))
}
