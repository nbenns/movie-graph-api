package com.kaizen.api.services

sealed trait RepositoryError extends Throwable with Product with Serializable

object RepositoryError {
  final case class ItemNotFound[ID](itemId: ID) extends RepositoryError
}
