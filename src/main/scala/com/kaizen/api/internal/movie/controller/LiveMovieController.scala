package com.kaizen.api.internal.movie.controller

import com.kaizen.api.internal.RepositoryError
import com.kaizen.api.internal.movie.MovieData
import com.kaizen.api.internal.movie.repository.MovieRepository
import zio.random.{Random, nextLongBounded}
import zquery.ZQuery

class LiveMovieController(movieRepository: MovieRepository.Service) extends MovieController.Service {
  def addMovie(addMovie: AddMovie): ZQuery[Random, RepositoryError, MovieData] =
    for {
      id    <- ZQuery.fromEffect(nextLongBounded(1000000000000000L))
      movie  = MovieData(id, addMovie.title)
      _     <- movieRepository.update(movie)
    } yield movie

  def getMovie(getMovie: GetMovie): ZQuery[Any, RepositoryError, MovieData] =
    movieRepository.getById(getMovie.id)

  def setMovieTitle(setMovieTitle: SetMovieTitle): ZQuery[Any, RepositoryError, MovieData] =
    for {
      movie   <- movieRepository.getById(setMovieTitle.id)
      updated  = movie.copy(title = setMovieTitle.title)
      _       <- movieRepository.update(updated)
    } yield updated

  override def removeMovie(removeMovie: RemoveMovie): ZQuery[Any, RepositoryError, Unit] =
    for {
      movie <- movieRepository.getById(removeMovie.id)
      _     <- movieRepository.delete(movie)
    } yield ()
}
