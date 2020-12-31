package com.kaizen.api.services.actor.repository

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.{ActorData, ActorId}
import zio.stm.{TMap, ZSTM}

class InMemoryActorRepository(memory: TMap[ActorId, ActorData]) extends ActorRepository.Service {
  override def getById(id: ActorId): ZSTM[Any, RepositoryError, ActorData] =
      memory
        .get(id)
        .someOrFail(RepositoryError.ItemNotFound(id))

  override def update(actorData: ActorData): ZSTM[Any, Nothing, Unit] =
      memory
        .put(actorData.id, actorData)

  override def delete(actorData: ActorData): ZSTM[Any, Nothing, Unit] =
      memory
        .delete(actorData.id)
}
