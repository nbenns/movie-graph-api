package com.kaizen.api.services.movie.graphql

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.movie.{MovieId, MovieTitle}
import zio.query.Request

final case class GetMovie(id: MovieId)                         extends Request[RepositoryError, Movie]
final case class AddMovie(title: MovieTitle)                   extends Request[RepositoryError, Movie]
final case class SetMovieTitle(id: MovieId, title: MovieTitle) extends Request[RepositoryError, Movie]
final case class RemoveMovie(id: MovieId)                      extends Request[RepositoryError, Unit]
