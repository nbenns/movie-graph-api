package com.kaizen.movie

import caliban.GraphQL.graphQL
import caliban.{GraphQL, RootResolver}
import zio.ZEnv

package object graphql {
  val api: GraphQL[ZEnv] = graphQL(RootResolver(Queries(), Mutations()))
}
