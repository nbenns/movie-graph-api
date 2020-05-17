package com.kaizen.api.actedIn.controller

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.repository.{ActedInData, ActedInRepository}
import com.kaizen.api.actedIn.{ActedIn, repository}
import zquery.ZQuery

class LiveActedInController(repo: ActedInRepository.Service) extends ActedInController.Service {
  override def getActedIn(getActedIn: GetActedIn): ZQuery[Any, RepositoryError, ActedIn] =
    repo
      .getActedInById(getActedIn.actorId, getActedIn.movieId)
      .map(ActedIn.fromActedInData)

  override def addActedIn(addActedIn: AddActedIn): ZQuery[Any, RepositoryError, ActedIn] = {
    val data = ActedInData(addActedIn.actorId, addActedIn.movieId)

    repo
      .updateActedIn(data)
      .map(_ => ActedIn.fromActedInData(data))
  }

  override def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[Any, RepositoryError, Unit] = {
    val data = repository.ActedInData(removeActedIn.actorId, removeActedIn.movieId)

    repo.deleteActedIn(data)
  }

  override def getMoviesActedIn(getMoviesActedIn: GetMoviesActedIn): ZQuery[Any, RepositoryError, List[ActedIn]] =
    repo
      .getMoviesActedIn(getMoviesActedIn.actorId)
      .map(_.map(ActedIn.fromActedInData))

  override def countMoviesActedIn(countMoviesActedIn: CountMoviesActedIn): ZQuery[Any, RepositoryError, Long] =
    repo.countMoviesActedIn(countMoviesActedIn.actorId)
}
