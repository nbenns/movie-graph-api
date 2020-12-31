package com.kaizen.api.graphql

import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actor.{ActorId, ActorName}
import zio.query.Request

final case class GetActor(id: ActorId)                      extends Request[RepositoryError, Actor]
final case class AddActor(name: ActorName)                  extends Request[RepositoryError, Actor]
final case class SetActorName(id: ActorId, name: ActorName) extends Request[RepositoryError, Actor]
final case class RemoveActor(id: ActorId)                   extends Request[RepositoryError, Unit]
