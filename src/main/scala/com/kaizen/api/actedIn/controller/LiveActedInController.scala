package com.kaizen.api.actedIn.controller

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.ActedIn
import com.kaizen.api.actedIn.repository.ActedInRepository
import zquery.ZQuery

class LiveActedInController(repo: ActedInRepository.Service) extends ActedInController.Service {
  override def getActedIn(getActedIn: GetActedIn): ZQuery[Any, RepositoryError, ActedIn] =
    repo.getActedInById(getActedIn.actorId, getActedIn.movieId)

  override def addActedIn(addActedIn: AddActedIn): ZQuery[Any, RepositoryError, ActedIn] = {
    val actedIn = ActedIn(addActedIn.actorId, addActedIn.movieId)
    repo.updateActedIn(actedIn).map(_ => actedIn)
  }

  override def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[Any, RepositoryError, Unit] =
    repo.deleteActedIn(ActedIn(removeActedIn.actorId, removeActedIn.movieId))
}
