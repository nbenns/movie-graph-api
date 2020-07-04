package com.kaizen.api.services.actor

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.repository.ActorRepository
import zio.query.ZQuery
import zio.random.Random
import zio.{Has, ZLayer}

package object controller {
  type ActorController = Has[ActorController.Service]

  sealed trait ActorSchema extends Product with Serializable

  final case class GetActor(id: ActorId)                      extends ActorSchema
  final case class AddActor(name: ActorName)                  extends ActorSchema
  final case class SetActorName(id: ActorId, name: ActorName) extends ActorSchema
  final case class RemoveActor(id: ActorId)                   extends ActorSchema

  object ActorController {
    trait Service {
      def getActor(getActor: GetActor): ZQuery[Any, RepositoryError, ActorData]
      def addActor(addActor: AddActor): ZQuery[Random, RepositoryError, ActorData]
      def setActorName(setActorName: SetActorName): ZQuery[Any, RepositoryError, ActorData]
      def removeActor(removeActor: RemoveActor): ZQuery[Any, RepositoryError, Unit]
    }

    val live: ZLayer[ActorRepository, Nothing, ActorController] =
      ZLayer.fromService(new LiveActorController(_))
  }

  def getActor(getActor: GetActor): ZQuery[ActorController, RepositoryError, ActorData] =
    ZQuery.accessM[ActorController](_.get.getActor(getActor))

  def addActor(addActor: AddActor): ZQuery[ActorController with Random, RepositoryError, ActorData] =
    ZQuery.accessM[ActorController with Random](_.get.addActor(addActor))

  def setActorName(setActorName: SetActorName): ZQuery[ActorController, RepositoryError, ActorData] =
    ZQuery.accessM[ActorController](_.get.setActorName(setActorName))

  def removeActor(removeActor: RemoveActor): ZQuery[ActorController, RepositoryError, Unit] =
    ZQuery.accessM[ActorController](_.get.removeActor(removeActor))
}
