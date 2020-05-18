package com.kaizen.api.services.movie

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie.repository.MovieRepository
import zio.random.Random
import zio.{Has, ZLayer}
import zquery.ZQuery

package object controller {
  type MovieController = Has[MovieController.Service]

  sealed trait MovieSchema extends Product with Serializable

  final case class GetMovie(id: MovieId)                         extends MovieSchema
  final case class AddMovie(title: MovieTitle)                   extends MovieSchema
  final case class SetMovieTitle(id: MovieId, title: MovieTitle) extends MovieSchema
  final case class RemoveMovie(id: MovieId)                      extends MovieSchema

  object MovieController {
    trait Service {
      def getMovie(getMovie: GetMovie): ZQuery[Any, RepositoryError, MovieData]
      def addMovie(addMovie: AddMovie): ZQuery[Random, RepositoryError, MovieData]
      def setMovieTitle(setMovieTitle: SetMovieTitle): ZQuery[Any, RepositoryError, MovieData]
      def removeMovie(removeMovie: RemoveMovie): ZQuery[Any, RepositoryError, Unit]
    }

    val live: ZLayer[MovieRepository, Nothing, MovieController] =
      ZLayer.fromService(new LiveMovieController(_))
  }

  def getMovie(getMovie: GetMovie): ZQuery[MovieController, RepositoryError, MovieData] =
    ZQuery.environment[MovieController].flatMap(_.get.getMovie(getMovie))

  def addMovie(addMovie: AddMovie): ZQuery[Random with MovieController, RepositoryError, MovieData] =
    ZQuery.environment[MovieController].flatMap(_.get.addMovie(addMovie))

  def setMovieTitle(setMovieTitle: SetMovieTitle): ZQuery[MovieController, RepositoryError, MovieData] =
    ZQuery.environment[MovieController].flatMap(_.get.setMovieTitle(setMovieTitle))

  def removeMovie(removeMovie: RemoveMovie): ZQuery[MovieController, RepositoryError, Unit] =
    ZQuery.environment[MovieController].flatMap(_.get.removeMovie(removeMovie))
}
