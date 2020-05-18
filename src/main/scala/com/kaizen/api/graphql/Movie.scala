package com.kaizen.api.graphql

import com.kaizen.api.services.movie._

final case class Movie(id: MovieId, title: MovieTitle)

object Movie {
  def fromMovieData(data: MovieData): Movie =
    Movie(
      data.id,
      data.title
    )
}
