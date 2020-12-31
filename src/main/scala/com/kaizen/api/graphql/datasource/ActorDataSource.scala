package com.kaizen.api.graphql.datasource

import com.kaizen.api.graphql.{Actor, AddActor, GetActor, RemoveActor, SetActorName}
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.{ActorId, ActorName}
import com.kaizen.api.services.actor.controller.ActorController
import zio.ZIO
import zio.query.{DataSource, ZQuery}
import zio.random.Random

object ActorDataSource {
  private val getActorDS: DataSource[ActorController, GetActor] =
    DataSource.fromFunctionM("GetActor") { getActor =>
      ZIO
        .accessM[ActorController](_.get.getActor(getActor.id))
        .map(Actor.fromActorData)
    }

  private val addActorDS: DataSource[ActorController with Random, AddActor] =
    DataSource
      .fromFunctionM("AddActor") { addActor =>
      ZIO
        .accessM[ActorController with Random](_.get.addActor(addActor.name))
        .map(Actor.fromActorData)
    }

  private val setActorNameDS: DataSource[ActorController, SetActorName] =
    DataSource
    .fromFunctionM("SetActorName") { setActorName =>
      ZIO
        .accessM[ActorController](_.get.setActorName(setActorName.id, setActorName.name))
        .map(Actor.fromActorData)
    }

  private val removeActorDS: DataSource[ActorController, RemoveActor] =
    DataSource
      .fromFunctionM("RemoveActor") { removeActor =>
        ZIO
          .accessM[ActorController](_.get.removeActor(removeActor.id))
      }

  def getActor(getActor: GetActor): ZQuery[ActorController, RepositoryError, Actor] =
    ZQuery.fromRequest(getActor)(getActorDS)

  def getActor(actorId: ActorId): ZQuery[ActorController, RepositoryError, Actor] =
    getActor(GetActor(actorId))


  def addActor(addActor: AddActor): ZQuery[ActorController with Random, RepositoryError, Actor] =
    ZQuery.fromRequest(addActor)(addActorDS)

  def addActor(name: ActorName): ZQuery[ActorController with Random, RepositoryError, Actor] =
    addActor(AddActor(name))


  def setActorName(setActorName: SetActorName): ZQuery[ActorController, RepositoryError, Actor] =
    ZQuery.fromRequest(setActorName)(setActorNameDS)

  def setActorName(actorId: ActorId, actorName: ActorName): ZQuery[ActorController, RepositoryError, Actor] =
    setActorName(SetActorName(actorId, actorName))


  def removeActor(removeActor: RemoveActor): ZQuery[ActorController, RepositoryError, Unit] =
    ZQuery.fromRequest(removeActor)(removeActorDS)

  def removeActor(actorId: ActorId): ZQuery[ActorController, RepositoryError, Unit] =
    removeActor(RemoveActor(actorId))

}
