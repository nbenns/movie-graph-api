package com.kaizen.api.services.movie

import caliban.{GraphQL, RootResolver}
import caliban.GraphQL.graphQL
import caliban.schema.GenericSchema
import com.kaizen.api.ZAppEnv
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie.controller.MovieController
import zio.query.ZQuery
import zio.random.Random

package object graphql extends GenericSchema[ZAppEnv] {
  private object Queries {
    val getMovie: GetMovie => ZQuery[MovieController, RepositoryError, Movie] =
      MovieDataSource.getMovie
  }

  private object Mutations {
    val addMovie: AddMovie => ZQuery[MovieController with Random, RepositoryError, Movie] =
      MovieDataSource.addMovie

    val setMovieTitle: SetMovieTitle => ZQuery[MovieController, RepositoryError, Movie] =
      MovieDataSource.setMovieTitle

    val removeMovie: RemoveMovie => ZQuery[MovieController, RepositoryError, Unit] =
      MovieDataSource.removeMovie
  }

  val api: GraphQL[ZAppEnv] = graphQL(RootResolver(Queries, Mutations))
}
