package com.kaizen.api.actor.repository

import com.kaizen.api.RepositoryError
import com.kaizen.api.actor.{Actor, ActorId}
import zio.stm.TMap
import zquery.DataSource

class InMemoryActorRepository(memory: TMap[ActorId, Actor]) extends ActorRepository.Impl {
  override val getById: DataSource[Any, GetActorById] =
    DataSource.fromFunctionM("getActorById") { getActorById =>
      memory
        .get(getActorById.id)
        .commit
        .someOrFail(RepositoryError.ItemNotFound(getActorById.id).asInstanceOf[RepositoryError])
    }

  override val update: DataSource[Any, UpdateActor] =
    DataSource.fromFunctionM("updateActor") { updateActor =>
      memory
        .put(updateActor.movie.id, updateActor.movie)
        .commit
    }

  override val delete: DataSource[Any, DeleteActor] =
    DataSource.fromFunctionM("deleteActor") { deleteActor =>
      memory
        .delete(deleteActor.movie.id)
        .commit
    }
}
