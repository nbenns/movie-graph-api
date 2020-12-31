package com.kaizen.api.services.actedIn.controller

import com.kaizen.api.services.{RepositoryError, actedIn}
import com.kaizen.api.services.actedIn.ActedInData
import com.kaizen.api.services.actedIn.repository.ActedInRepository
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieId
import zio.ZIO

class LiveActedInController(repo: ActedInRepository.Service) extends ActedInController.Service {
  override def getActedIn(actorId: ActorId, movieId: MovieId): ZIO[Any, RepositoryError, ActedInData] =
    repo.getById(actorId, movieId).commit

  override def addActedIn(actorId: ActorId, movieId: MovieId): ZIO[Any, RepositoryError, ActedInData] = {
    val data = actedIn.ActedInData(actorId, movieId)

    repo
      .update(data)
      .as(data)
      .commit
  }

  override def removeActedIn(actorId: ActorId, movieId: MovieId): ZIO[Any, RepositoryError, Unit] = {
    val data = ActedInData(actorId, movieId)

    repo.delete(data).commit
  }

  override def getMoviesActedIn(actorId: ActorId): ZIO[Any, RepositoryError, List[ActedInData]] =
    repo.getMoviesActedIn(actorId).commit

  override def countMoviesActedIn(actorId: ActorId): ZIO[Any, RepositoryError, Long] =
    repo.countMoviesActedIn(actorId).commit
}
