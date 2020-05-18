package com.kaizen.api.internal

sealed trait RepositoryError extends Throwable with Product with Serializable

object RepositoryError {
  final case class ItemNotFound[ID](itemId: ID) extends RepositoryError
}
