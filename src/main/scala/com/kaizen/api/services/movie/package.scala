package com.kaizen.api.services

import com.kaizen.api.services.movie.controller.MovieController
import zio.ZLayer

package object movie {
  type MovieId    = Long
  type MovieTitle = String

  val dependencies: ZLayer[Any, Nothing, MovieController] =
    repository.MovieRepository.inMemory >>>
      controller.MovieController.live
}
