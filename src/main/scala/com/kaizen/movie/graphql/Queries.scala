package com.kaizen.movie.graphql

import com.kaizen.movie.model.Movie
import zquery.ZQuery

case class Queries(
  test: Test => ZQuery[Any, Nothing, Movie] = _ => ZQuery.succeed(Movie(1))
)
