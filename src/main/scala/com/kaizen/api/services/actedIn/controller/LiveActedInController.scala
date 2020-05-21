package com.kaizen.api.services.actedIn.controller

import com.kaizen.api.services.actedIn.ActedInData
import com.kaizen.api.services.actedIn.repository.ActedInRepository
import com.kaizen.api.services.actedIn
import com.kaizen.api.services.RepositoryError
import zio.query.ZQuery

class LiveActedInController(repo: ActedInRepository.Service) extends ActedInController.Service {
  override def getActedIn(getActedIn: GetActedIn): ZQuery[Any, RepositoryError, ActedInData] =
    repo.getActedInById(getActedIn.actorId, getActedIn.movieId)

  override def addActedIn(addActedIn: AddActedIn): ZQuery[Any, RepositoryError, ActedInData] = {
    val data = actedIn.ActedInData(addActedIn.actorId, addActedIn.movieId)

    repo
      .updateActedIn(data)
      .as(data)
  }

  override def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[Any, RepositoryError, Unit] = {
    val data = ActedInData(removeActedIn.actorId, removeActedIn.movieId)

    repo.deleteActedIn(data)
  }

  override def getMoviesActedIn(getMoviesActedIn: GetMoviesActedIn): ZQuery[Any, RepositoryError, List[ActedInData]] =
    repo.getMoviesActedIn(getMoviesActedIn.actorId)

  override def countMoviesActedIn(countMoviesActedIn: CountMoviesActedIn): ZQuery[Any, RepositoryError, Long] =
    repo.countMoviesActedIn(countMoviesActedIn.actorId)
}
