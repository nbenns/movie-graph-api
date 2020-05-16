package com.kaizen.api.actedIn

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.repository.ActedInRepository
import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.MovieId
import com.kaizen.api.movie.controller.MovieController
import zio.{Has, ZLayer}
import zquery.ZQuery

package object controller {
  type ActedInController = Has[ActedInController.Service]

  sealed trait ActedInSchema extends Product with Serializable

  case class GetActedIn(actorId: ActorId, movieId: MovieId) extends ActedInSchema
  case class AddActedIn(actorId: ActorId, movieId: MovieId) extends ActedInSchema
  case class RemoveActedIn(actorId: ActorId, movieId: MovieId) extends ActedInSchema
  case class GetMoviesActedIn(actorId: ActorId) extends ActedInSchema

  object ActedInController {
    trait Service {
      def getActedIn(getActedIn: GetActedIn): ZQuery[MovieController, RepositoryError, ActedIn]
      def addActedIn(addActedIn: AddActedIn): ZQuery[MovieController, RepositoryError, ActedIn]
      def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[Any, RepositoryError, Unit]
      def getMoviesActedIn(getMoviesActedIn: GetMoviesActedIn): ZQuery[MovieController, RepositoryError, List[ActedIn]]
    }

    val live: ZLayer[ActedInRepository, Nothing, ActedInController] =
      ZLayer.fromFunction(repo => new LiveActedInController(repo.get))
  }

  def getActedIn(getActedIn: GetActedIn): ZQuery[ActedInController with MovieController, RepositoryError, ActedIn] =
    ZQuery.environment[ActedInController].flatMap(_.get.getActedIn(getActedIn))

  def addActedIn(addActedIn: AddActedIn): ZQuery[ActedInController with MovieController, RepositoryError, ActedIn] =
    ZQuery.environment[ActedInController].flatMap(_.get.addActedIn(addActedIn))

  def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[ActedInController, RepositoryError, Unit] =
    ZQuery.environment[ActedInController].flatMap(_.get.removeActedIn(removeActedIn))

  def getMoviesActedIn(getMoviesActedIn: GetMoviesActedIn): ZQuery[ActedInController with MovieController, RepositoryError, List[ActedIn]] =
    ZQuery.environment[ActedInController].flatMap(_.get.getMoviesActedIn(getMoviesActedIn))
}
