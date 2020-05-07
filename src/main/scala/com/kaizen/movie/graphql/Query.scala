package com.kaizen.movie.graphql

import com.kaizen.movie.model.Movie
import zquery.Request

sealed trait Query extends Product with Serializable

case class Test() extends Query with Request[Nothing, Movie]
