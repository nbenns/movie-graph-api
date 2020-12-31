package com.kaizen.api.services.actor

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.repository.ActorRepository
import zio.random.Random
import zio.{Has, ZIO, ZLayer}

package object controller {
  type ActorController = Has[ActorController.Service]

  object ActorController {
    trait Service {
      def getActor(id: ActorId): ZIO[Any, RepositoryError, ActorData]
      def addActor(name: ActorName): ZIO[Random, RepositoryError, ActorData]
      def setActorName(id: ActorId, name: ActorName): ZIO[Any, RepositoryError, ActorData]
      def removeActor(id: ActorId): ZIO[Any, RepositoryError, Unit]
    }

    val live: ZLayer[ActorRepository, Nothing, ActorController] =
      ZLayer.fromService(new LiveActorController(_))
  }

  def getActor(id: ActorId): ZIO[ActorController, RepositoryError, ActorData] =
    ZIO.accessM[ActorController](_.get.getActor(id))

  def addActor(name: ActorName): ZIO[ActorController with Random, RepositoryError, ActorData] =
    ZIO.accessM[ActorController with Random](_.get.addActor(name))

  def setActorName(id: ActorId, name: ActorName): ZIO[ActorController, RepositoryError, ActorData] =
    ZIO.accessM[ActorController](_.get.setActorName(id, name))

  def removeActor(id: ActorId): ZIO[ActorController, RepositoryError, Unit] =
    ZIO.accessM[ActorController](_.get.removeActor(id))
}
