package com.kaizen.api.actor

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.controller.ActedInController
import com.kaizen.api.actor.repository.ActorRepository
import com.kaizen.api.movie.controller.MovieController
import zio.random.Random
import zio.{Has, ZLayer}
import zquery.ZQuery

package object controller {
  type ActorController = Has[ActorController.Service]

  sealed trait ActorSchema extends Product with Serializable

  final case class GetActor(id: ActorId)                      extends ActorSchema
  final case class AddActor(name: ActorName)                  extends ActorSchema
  final case class SetActorName(id: ActorId, name: ActorName) extends ActorSchema
  final case class RemoveActor(id: ActorId)                   extends ActorSchema

  object ActorController {
    trait Service {
      def getActor(getActor: GetActor): ZQuery[Any, RepositoryError, Actor]
      def addActor(addActor: AddActor): ZQuery[Random, RepositoryError, Actor]
      def setActorName(setActorName: SetActorName): ZQuery[Any, RepositoryError, Actor]
      def removeActor(removeActor: RemoveActor): ZQuery[Any, RepositoryError, Unit]
    }

    val live: ZLayer[ActorRepository, Nothing, ActorController] =
      ZLayer.fromFunction(repo => new LiveActorController(repo.get))
  }

  def getActor(getActor: GetActor): ZQuery[ActorController, RepositoryError, Actor] =
    ZQuery.environment[ActorController].flatMap(_.get.getActor(getActor))

  def addActor(addActor: AddActor): ZQuery[ActorController with Random, RepositoryError, Actor] =
    ZQuery.environment[ActorController].flatMap(_.get.addActor(addActor))

  def setActorName(setActorName: SetActorName): ZQuery[ActorController, RepositoryError, Actor] =
    ZQuery.environment[ActorController].flatMap(_.get.setActorName(setActorName))

  def removeActor(removeActor: RemoveActor): ZQuery[ActorController, RepositoryError, Unit] =
    ZQuery.environment[ActorController].flatMap(_.get.removeActor(removeActor))
}
