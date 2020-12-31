package com.kaizen.api.graphql

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieId
import zio.query.Request

case class GetActedIn(actorId: ActorId, movieId: MovieId) extends Request[RepositoryError, ActedInEdge]
case class AddActedIn(actorId: ActorId, movieId: MovieId) extends Request[RepositoryError, ActedInEdge]
case class RemoveActedIn(actorId: ActorId, movieId: MovieId) extends Request[RepositoryError, Unit]
case class GetMoviesActedIn(actorId: ActorId) extends Request[RepositoryError, List[ActedInEdge]]
case class CountMoviesActedIn(actorId: ActorId) extends Request[RepositoryError, Long]
