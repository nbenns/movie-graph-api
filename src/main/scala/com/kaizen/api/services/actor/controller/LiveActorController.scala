package com.kaizen.api.services.actor.controller

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.repository.ActorRepository
import com.kaizen.api.services.actor.{ActorData, ActorId, ActorName}
import zio.ZIO
import zio.random._

class LiveActorController(actorRepository: ActorRepository.Service) extends ActorController.Service {
  override def getActor(id: ActorId) =
    actorRepository
      .getById(id)
      .commit

  override def addActor(name: ActorName): ZIO[Random, RepositoryError, ActorData] =
    for {
      id    <- nextLongBounded(1000000000000000L)
      actor  = ActorData(id, name)
      _     <- actorRepository.update(actor).commit
    } yield actor

  override def setActorName(id: ActorId, name: ActorName): ZIO[Any, RepositoryError, ActorData] =
    (
      for {
        data    <- actorRepository.getById(id)
        updated  = data.copy(name = name)
        _       <- actorRepository.update(updated)
      } yield updated
    ).commit

  override def removeActor(id: ActorId): ZIO[Any, RepositoryError, Unit] =
    (
      for {
        actor <- actorRepository.getById(id)
        _     <- actorRepository.delete(actor)
      } yield ()
    ).commit
}
