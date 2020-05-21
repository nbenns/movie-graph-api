package com.kaizen.api.services.actor.repository

import com.kaizen.api.services.actor.{ActorData, ActorId}
import com.kaizen.api.services.RepositoryError
import zio.stm.TMap
import zio.query.DataSource

class InMemoryActorRepository(memory: TMap[ActorId, ActorData]) extends ActorRepository.Impl {
  override val getById: DataSource[Any, GetActorById] =
    DataSource.fromFunctionM("getActorById") { getActorById =>
      memory
        .get(getActorById.id)
        .commit
        .someOrFail[ActorData, RepositoryError](RepositoryError.ItemNotFound(getActorById.id))
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
