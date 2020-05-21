package com.kaizen.api.services.actor.controller

import com.kaizen.api.services.actor.ActorData
import com.kaizen.api.services.actor.repository.ActorRepository
import com.kaizen.api.services.RepositoryError
import zio.random._
import zio.query.ZQuery

class LiveActorController(actorRepository: ActorRepository.Service) extends ActorController.Service {
  override def getActor(getActor: GetActor): ZQuery[Any, RepositoryError, ActorData] =
    actorRepository
      .getById(getActor.id)


  override def addActor(addActor: AddActor): ZQuery[Random, RepositoryError, ActorData] =
    for {
      id    <- ZQuery.fromEffect(nextLongBounded(1000000000000000L))
      actor  = ActorData(id, addActor.name)
      _     <- actorRepository.update(actor)
    } yield actor

  override def setActorName(setActorName: SetActorName): ZQuery[Any, RepositoryError, ActorData] =
    for {
      data    <- actorRepository.getById(setActorName.id)
      updated  = data.copy(name = setActorName.name)
      _       <- actorRepository.update(updated)
    } yield updated

  override def removeActor(removeActor: RemoveActor): ZQuery[Any, RepositoryError, Unit] =
    for {
      actor <- actorRepository.getById(removeActor.id)
      _     <- actorRepository.delete(actor)
    } yield ()
}
