package com.kaizen.api.graphql

import caliban.GraphQL.graphQL
import caliban.schema.GenericSchema
import caliban.{GraphQL, RootResolver}
import com.kaizen.api._
import com.kaizen.api.graphql.datasource.{ActedInDataSource, ActorDataSource}
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.controller._
import com.kaizen.api.services.actor.controller._
import zio.query.ZQuery
import zio.random.Random

object Schema extends GenericSchema[ZAppEnv] {
  private case class Queries(
    getActor:   GetActor   => ZQuery[ActorController, RepositoryError, Actor],
    getActedIn: GetActedIn => ZQuery[ActedInController, RepositoryError, ActedInEdge]
  )

  private case class Mutations(
    addActor:      AddActor      => ZQuery[ActorController with Random, RepositoryError, Actor],
    setActorName:  SetActorName  => ZQuery[ActorController, RepositoryError, Actor],
    removeActor:   RemoveActor   => ZQuery[ActorController, RepositoryError, Unit],

    addActedIn:    AddActedIn    => ZQuery[ActedInController, RepositoryError, ActedInEdge],
    removeActedIn: RemoveActedIn => ZQuery[ActedInController, RepositoryError, Unit]
  )

  private val queries = Queries(
    ActorDataSource.getActor,
    ActedInDataSource.getActedIn
  )

  private val mutations = Mutations(
    ActorDataSource.addActor,
    ActorDataSource.setActorName,
    ActorDataSource.removeActor,

    ActedInDataSource.addActedIn,
    ActedInDataSource.removeActedIn
  )

  val api: GraphQL[ZAppEnv] = graphQL(RootResolver(queries, mutations)) |+| services.movie.graphql.api
}
