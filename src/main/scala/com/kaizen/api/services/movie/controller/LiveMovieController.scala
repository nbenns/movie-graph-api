package com.kaizen.api.services.movie.controller

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie._
import com.kaizen.api.services.movie.repository.MovieRepository
import zio.ZIO
import zio.random.{Random, nextLongBounded}

class LiveMovieController(movieRepository: MovieRepository.Service) extends MovieController.Service {
  def addMovie(title: MovieTitle): ZIO[Random, RepositoryError, MovieData] =
    for {
      id    <- nextLongBounded(1000000000000000L)
      movie  = MovieData(id, title)
      _     <- movieRepository.update(movie).commit
    } yield movie

  def getMovie(id: MovieId): ZIO[Any, RepositoryError, MovieData] =
    movieRepository
      .getById(id)
      .commit

  def setMovieTitle(id: MovieId, title: MovieTitle): ZIO[Any, RepositoryError, MovieData] =
    (
      for {
        movie   <- movieRepository.getById(id)
        updated  = movie.copy(title = title)
        _       <- movieRepository.update(updated)
      } yield updated
    ).commit

  override def removeMovie(id: MovieId): ZIO[Any, RepositoryError, Unit] =
    (
      for {
        movie <- movieRepository.getById(id)
        _     <- movieRepository.delete(movie)
      } yield ()
    ).commit
}
