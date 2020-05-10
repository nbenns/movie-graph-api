package com.kaizen.api

import caliban.GraphQL.graphQL
import caliban.schema.GenericSchema
import caliban.{GraphQL, RootResolver}
import com.kaizen.api.actor.Actor
import com.kaizen.api.actor.controller._
import com.kaizen.api.movie.Movie
import com.kaizen.api.movie.controller._
import zio.random.Random
import zquery.ZQuery

object Graphql extends GenericSchema[MovieController with ActorController with Random] {
  case class Mutations(
    addMovie: AddMovie => ZQuery[MovieController with Random, RepositoryError, Movie],
    setMovieTitle: SetMovieTitle => ZQuery[MovieController, RepositoryError, Movie],
    removeMovie: RemoveMovie => ZQuery[MovieController, RepositoryError, Unit],
    addActor: AddActor => ZQuery[ActorController with Random, RepositoryError, Actor],
    setActorName: SetActorName => ZQuery[ActorController, RepositoryError, Actor],
    removeActor: RemoveActor => ZQuery[ActorController, RepositoryError, Unit]
  )

  case class Queries(
    getMovie: GetMovie => ZQuery[MovieController, RepositoryError, Movie],
    getActor: GetActor => ZQuery[ActorController, RepositoryError, Actor]
  )

  private val queries = Queries(
    getMovie,
    getActor
  )

  private val mutations = Mutations(
    addMovie,
    setMovieTitle,
    removeMovie,
    addActor,
    setActorName,
    removeActor
  )

  val api: GraphQL[MovieController with ActorController with Random] = graphQL(RootResolver(queries, mutations))
}
