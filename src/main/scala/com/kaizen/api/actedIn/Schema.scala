package com.kaizen.api.actedIn

import caliban.{GraphQL, RootResolver}
import caliban.GraphQL.graphQL
import caliban.schema.GenericSchema
import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.controller._
import com.kaizen.api.movie.controller.MovieController
import zio.ZEnv
import zquery.ZQuery

object Schema extends GenericSchema[ActedInController with MovieController] {
  private case class Queries(
    getActedIn: GetActedIn => ZQuery[ActedInController with MovieController, RepositoryError, ActedIn]
  )

  private case class Mutations(
    addActedIn: AddActedIn => ZQuery[ActedInController with MovieController, RepositoryError, ActedIn],
    removeActedIn: RemoveActedIn => ZQuery[ActedInController, RepositoryError, Unit]
  )

  private val queries = Queries(
    getActedIn
  )

  private val mutations = Mutations(
    addActedIn,
    removeActedIn
  )

  val api: GraphQL[ZEnv with ActedInController with MovieController] = graphQL(RootResolver(queries, mutations))
}
