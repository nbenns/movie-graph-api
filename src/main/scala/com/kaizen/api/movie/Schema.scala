package com.kaizen.api.movie

import caliban.GraphQL.graphQL
import caliban.{GraphQL, RootResolver}
import caliban.schema.GenericSchema
import com.kaizen.api.RepositoryError
import com.kaizen.api.movie.controller._
import zio.ZEnv
import zio.random.Random
import zquery.ZQuery


object Schema extends GenericSchema[MovieController with Random]{
  private case class Queries(
    getMovie: GetMovie => ZQuery[MovieController, RepositoryError, Movie]
  )

  private case class Mutations(
    addMovie: AddMovie => ZQuery[MovieController with Random, RepositoryError, Movie],
    setMovieTitle: SetMovieTitle => ZQuery[MovieController, RepositoryError, Movie],
    removeMovie: RemoveMovie => ZQuery[MovieController, RepositoryError, Unit]
  )

  private val queries = Queries(
    getMovie
  )

  private val mutations = Mutations(
    addMovie,
    setMovieTitle,
    removeMovie
  )

  val api: GraphQL[ZEnv with MovieController] = graphQL(RootResolver(queries, mutations))
}
