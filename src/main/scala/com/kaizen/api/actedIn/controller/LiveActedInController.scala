package com.kaizen.api.actedIn.controller

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.{ActedIn, ActedInData}
import com.kaizen.api.actedIn.repository.ActedInRepository
import com.kaizen.api.movie.controller._
import zquery.ZQuery

class LiveActedInController(repo: ActedInRepository.Service) extends ActedInController.Service {
  override def getActedIn(getActedIn: GetActedIn): ZQuery[MovieController, RepositoryError, ActedIn] =
    for {
      data    <- repo.getActedInById(getActedIn.actorId, getActedIn.movieId)
      actedIn = ActedIn(data.actorId, getMovie(GetMovie(data.movieId)))
    } yield actedIn

  override def addActedIn(addActedIn: AddActedIn): ZQuery[Any, RepositoryError, ActedIn] =
    for {
      _       <- repo.updateActedIn(ActedInData(addActedIn.actorId, addActedIn.movieId))
      actedIn = ActedIn(addActedIn.actorId, getMovie(GetMovie(addActedIn.movieId)))
    } yield actedIn

  override def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[Any, RepositoryError, Unit] =
    repo.deleteActedIn(ActedInData(removeActedIn.actorId, removeActedIn.movieId))

  override def getMoviesActedIn(getMoviesActedIn: GetMoviesActedIn): ZQuery[MovieController, RepositoryError, List[ActedIn]] =
    for {
      list     <- repo.getMoviesActedIn(getMoviesActedIn.actorId)
      actedIns = list.map(d => ActedIn(d.actorId, getMovie(GetMovie(d.movieId))))
    } yield actedIns

  override def countMoviesActedIn(countMoviesActedIn: CountMoviesActedIn): ZQuery[Any, RepositoryError, Long] =
    repo.countMoviesActedIn(countMoviesActedIn.actorId)
}
