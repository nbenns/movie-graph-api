package com.kaizen.api.actor.controller

import com.kaizen.api.RepositoryError
import com.kaizen.api.actor.Actor
import com.kaizen.api.actor.repository.ActorRepository
import zio.random._
import zquery.ZQuery

class LiveActorController(actorRepository: ActorRepository.Service) extends ActorController.Service {
  override def getActor(getActor: GetActor): ZQuery[Any, RepositoryError, Actor] =
    actorRepository.getById(getActor.id)

  override def addActor(addActor: AddActor): ZQuery[Random, RepositoryError, Actor] =
    for {
      id    <- ZQuery.fromEffect(nextLong(1000000000000000L))
      actor = Actor(id, addActor.name)
      _     <- actorRepository.update(actor)
    } yield actor

  override def setActorName(setActorName: SetActorName): ZQuery[Any, RepositoryError, Actor] =
    for {
      actor   <- actorRepository.getById(setActorName.id)
      updated = actor.copy(name = setActorName.name)
      _       <- actorRepository.update(updated)
    } yield updated

  override def removeActor(removeActor: RemoveActor): ZQuery[Any, RepositoryError, Unit] =
    for {
      actor <- actorRepository.getById(removeActor.id)
      _     <- actorRepository.delete(actor)
    } yield ()
}
