package com.kaizen.api.graphql

import caliban.GraphQL.graphQL
import caliban.schema.GenericSchema
import caliban.{GraphQL, RootResolver}
import com.kaizen.api._
import com.kaizen.api.graphql.datasource.{ActedInDataSource, ActorDataSource, MovieDataSource}
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.controller._
import com.kaizen.api.services.actor.controller._
import com.kaizen.api.services.movie.controller._
import zio.query.ZQuery
import zio.random.Random

object Schema extends GenericSchema[ZAppEnv] {
  private case class Queries(
    getActor:   GetActor   => ZQuery[ActorController, RepositoryError, Actor],
    getMovie:   GetMovie   => ZQuery[MovieController, RepositoryError, Movie],
    getActedIn: GetActedIn => ZQuery[ActedInController, RepositoryError, ActedInEdge]
  )

  private case class Mutations(
    addActor:      AddActor      => ZQuery[ActorController with Random, RepositoryError, Actor],
    setActorName:  SetActorName  => ZQuery[ActorController, RepositoryError, Actor],
    removeActor:   RemoveActor   => ZQuery[ActorController, RepositoryError, Unit],

    addMovie:      AddMovie      => ZQuery[MovieController with Random, RepositoryError, Movie],
    setMovieTitle: SetMovieTitle => ZQuery[MovieController, RepositoryError, Movie],
    removeMovie:   RemoveMovie   => ZQuery[MovieController, RepositoryError, Unit],

    addActedIn:    AddActedIn    => ZQuery[ActedInController, RepositoryError, ActedInEdge],
    removeActedIn: RemoveActedIn => ZQuery[ActedInController, RepositoryError, Unit]
  )

  private val queries = Queries(
    ActorDataSource.getActor,
    MovieDataSource.getMovie,
    ActedInDataSource.getActedIn
  )

  private val mutations = Mutations(
    ActorDataSource.addActor,
    ActorDataSource.setActorName,
    ActorDataSource.removeActor,

    MovieDataSource.addMovie,
    MovieDataSource.setMovieTitle,
    MovieDataSource.removeMovie,

    ActedInDataSource.addActedIn,
    ActedInDataSource.removeActedIn
  )

  val api: GraphQL[ZAppEnv] = graphQL(RootResolver(queries, mutations))
}
