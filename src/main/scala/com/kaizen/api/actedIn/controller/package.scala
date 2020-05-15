package com.kaizen.api.actedIn

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.repository.ActedInRepository
import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.MovieId
import zio.{Has, ZLayer}
import zquery.ZQuery

package object controller {
  type ActedInController = Has[ActedInController.Service]

  sealed trait ActedInSchema extends Product with Serializable

  case class GetActedIn(actorId: ActorId, movieId: MovieId) extends ActedInSchema
  case class AddActedIn(actorId: ActorId, movieId: MovieId) extends ActedInSchema
  case class RemoveActedIn(actorId: ActorId, movieId: MovieId) extends ActedInSchema

  object ActedInController {
    trait Service {
      def getActedIn(getActedIn: GetActedIn): ZQuery[Any, RepositoryError, ActedIn]
      def addActedIn(addActedIn: AddActedIn): ZQuery[Any, RepositoryError, ActedIn]
      def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[Any, RepositoryError, Unit]
    }

    val live: ZLayer[ActedInRepository, Nothing, ActedInController] =
      ZLayer.fromFunction(repo => new LiveActedInController(repo.get))
  }

  def getActedIn(getActedIn: GetActedIn): ZQuery[ActedInController, RepositoryError, ActedIn] =
    ZQuery.environment[ActedInController].flatMap(_.get.getActedIn(getActedIn))

  def addActedIn(addActedIn: AddActedIn): ZQuery[ActedInController, RepositoryError, ActedIn] =
    ZQuery.environment[ActedInController].flatMap(_.get.addActedIn(addActedIn))

  def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[ActedInController, RepositoryError, Unit] =
    ZQuery.environment[ActedInController].flatMap(_.get.removeActedIn(removeActedIn))
}
