package com.kaizen.api.movie.controller

import com.kaizen.api
import com.kaizen.api.RepositoryError
import com.kaizen.api.movie.MovieData
import com.kaizen.api.movie.repository.MovieRepository
import zio.random.{ nextLongBounded, Random }
import zquery.ZQuery

class LiveMovieController(movieRepository: MovieRepository.Service) extends MovieController.Service {
  def addMovie(addMovie: AddMovie): ZQuery[Random, RepositoryError, MovieData] =
    for {
      id    <- ZQuery.fromEffect(nextLongBounded(1000000000000000L))
      movie = api.movie.MovieData(id, addMovie.title)
      _     <- movieRepository.update(movie)
    } yield movie

  def getMovie(getMovie: GetMovie): ZQuery[Any, RepositoryError, MovieData] =
    movieRepository.getById(getMovie.id)

  def setMovieTitle(setMovieTitle: SetMovieTitle): ZQuery[Any, RepositoryError, MovieData] =
    for {
      movie   <- movieRepository.getById(setMovieTitle.id)
      updated = movie.copy(title = setMovieTitle.title)
      _       <- movieRepository.update(updated)
    } yield updated

  override def removeMovie(removeMovie: RemoveMovie): ZQuery[Any, RepositoryError, Unit] =
    for {
      movie <- movieRepository.getById(removeMovie.id)
      _     <- movieRepository.delete(movie)
    } yield ()
}
