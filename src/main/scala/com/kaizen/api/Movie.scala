package com.kaizen.api

import com.kaizen.api.movie.{MovieData, MovieId, MovieTitle}

final case class Movie(id: MovieId, title: MovieTitle)

object Movie {
  def fromMovieData(data: MovieData): Movie =
    Movie(
      data.id,
      data.title
    )
}
