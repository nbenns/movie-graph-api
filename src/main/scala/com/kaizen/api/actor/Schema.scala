package com.kaizen.api.actor

import caliban._
import caliban.GraphQL.graphQL
import caliban.schema.GenericSchema
import com.kaizen.api.RepositoryError
import controller._
import zio.ZEnv
import zio.random.Random
import zquery.ZQuery

object Schema extends GenericSchema[ActorController with Random] {
  private case class Queries(
    getActor: GetActor => ZQuery[ActorController, RepositoryError, Actor]
  )

  private case class Mutations(
    addActor: AddActor => ZQuery[ActorController with Random, RepositoryError, Actor],
    setActorName: SetActorName => ZQuery[ActorController, RepositoryError, Actor],
    removeActor: RemoveActor => ZQuery[ActorController, RepositoryError, Unit]
  )

  private val queries = Queries(
    getActor
  )

  private val mutations = Mutations(
    addActor,
    setActorName,
    removeActor
  )

  val api: GraphQL[ZEnv with ActorController] = graphQL(RootResolver(queries, mutations))
}
