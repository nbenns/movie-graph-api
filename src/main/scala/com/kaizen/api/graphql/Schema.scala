package com.kaizen.api.graphql

import caliban.GraphQL.graphQL
import caliban.schema.GenericSchema
import caliban.{GraphQL, RootResolver}
import com.kaizen.api._
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.controller._
import com.kaizen.api.services.actor.controller._
import com.kaizen.api.services.movie.controller._
import zio.random.Random
import zio.query.ZQuery

object Schema extends GenericSchema[ZAppEnv] {
  private case class Queries(
    getActor:   GetActor   => ZQuery[ActorController, RepositoryError, Actor],
    getMovie:   GetMovie   => ZQuery[MovieController, RepositoryError, Movie],
    getActedIn: GetActedIn => ZQuery[ActedInController, RepositoryError, ActedIn]
  )

  private case class Mutations(
    addActor:      AddActor      => ZQuery[ActorController with Random, RepositoryError, Actor],
    setActorName:  SetActorName  => ZQuery[ActorController, RepositoryError, Actor],
    removeActor:   RemoveActor   => ZQuery[ActorController, RepositoryError, Unit],

    addMovie:      AddMovie      => ZQuery[MovieController with Random, RepositoryError, Movie],
    setMovieTitle: SetMovieTitle => ZQuery[MovieController, RepositoryError, Movie],
    removeMovie:   RemoveMovie   => ZQuery[MovieController, RepositoryError, Unit],

    addActedIn:    AddActedIn    => ZQuery[ActedInController, RepositoryError, ActedIn],
    removeActedIn: RemoveActedIn => ZQuery[ActedInController, RepositoryError, Unit]
  )

  private val queries = Queries(
    args => getActor(args).map(Actor.fromActorData),
    args => getMovie(args).map(Movie.fromMovieData),
    args => getActedIn(args).map(ActedIn.fromActedInData)
  )

  private val mutations = Mutations(
    args => addActor(args).map(Actor.fromActorData),
    args => setActorName(args).map(Actor.fromActorData),
    removeActor,

    args => addMovie(args).map(Movie.fromMovieData),
    args => setMovieTitle(args).map(Movie.fromMovieData),
    removeMovie,

    args => addActedIn(args).map(ActedIn.fromActedInData),
    removeActedIn
  )

  val api: GraphQL[ZAppEnv] = graphQL(RootResolver(queries, mutations))
}
