package com.kaizen.api.actor.controller

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.controller._
import com.kaizen.api.actor.repository.ActorRepository
import com.kaizen.api.actor.{Actor, ActorData}
import com.kaizen.api.movie.controller.MovieController
import zio.random._
import zquery.ZQuery

class LiveActorController(actorRepository: ActorRepository.Service) extends ActorController.Service {
  override def getActor(getActor: GetActor): ZQuery[ActedInController with MovieController, RepositoryError, Actor] =
    for {
      data <- actorRepository.getById(getActor.id)
      actor = Actor(data.id, data.name, getMoviesActedIn(GetMoviesActedIn(getActor.id)))
    } yield actor

  override def addActor(addActor: AddActor): ZQuery[Random, RepositoryError, Actor] =
    for {
      id    <- ZQuery.fromEffect(nextLong(1000000000000000L))
      actor = ActorData(id, addActor.name)
      _     <- actorRepository.update(actor)
      actor = Actor(id, addActor.name, ZQuery.succeed(Nil))
    } yield actor

  override def setActorName(setActorName: SetActorName): ZQuery[ActedInController, RepositoryError, Actor] =
    for {
      data    <- actorRepository.getById(setActorName.id)
      updated = data.copy(name = setActorName.name)
      _       <- actorRepository.update(updated)
      actor   = Actor(data.id, data.name, getMoviesActedIn(GetMoviesActedIn(data.id)))
    } yield actor

  override def removeActor(removeActor: RemoveActor): ZQuery[Any, RepositoryError, Unit] =
    for {
      actor <- actorRepository.getById(removeActor.id)
      _     <- actorRepository.delete(actor)
    } yield ()
}
