package com.kaizen.api.services.actor

import com.kaizen.api.services.RepositoryError
import zio.query.Request
import zio.stm.{TMap, ZSTM}
import zio.{Has, ZLayer}

package object repository {
  type ActorRepository     = Has[ActorRepository.Service]

  object ActorRepository {
    trait Service {
      def getById(id: ActorId): ZSTM[Any, RepositoryError, ActorData]
      def update(actorData: ActorData): ZSTM[Any, RepositoryError, Unit]
      def delete(actorData: ActorData): ZSTM[Any, RepositoryError, Unit]
    }

    lazy val inMemory: ZLayer[Any, Nothing, ActorRepository] =
      TMap
        .empty[ActorId, ActorData]
        .map(new InMemoryActorRepository(_))
        .commit
        .toLayer
  }
}
