package com.kaizen.api.actor.controller

import com.kaizen.api.RepositoryError
import com.kaizen.api.actor.Actor
import com.kaizen.api.actor.repository.{ActorData, ActorRepository}
import zio.random._
import zquery.ZQuery

class LiveActorController(actorRepository: ActorRepository.Service) extends ActorController.Service {
  override def getActor(getActor: GetActor): ZQuery[Any, RepositoryError, Actor] =
    actorRepository
      .getById(getActor.id)
      .map(Actor.fromActorData)

  override def addActor(addActor: AddActor): ZQuery[Random, RepositoryError, Actor] =
    for {
      id    <- ZQuery.fromEffect(nextLong(1000000000000000L))
      actor  = ActorData(id, addActor.name)
      _     <- actorRepository.update(actor)
    } yield Actor.fromActorData(actor)

  override def setActorName(setActorName: SetActorName): ZQuery[Any, RepositoryError, Actor] =
    for {
      data    <- actorRepository.getById(setActorName.id)
      updated  = data.copy(name = setActorName.name)
      _       <- actorRepository.update(updated)
    } yield Actor.fromActorData(updated)

  override def removeActor(removeActor: RemoveActor): ZQuery[Any, RepositoryError, Unit] =
    for {
      actor <- actorRepository.getById(removeActor.id)
      _     <- actorRepository.delete(actor)
    } yield ()
}
