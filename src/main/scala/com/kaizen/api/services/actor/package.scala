package com.kaizen.api.services

import com.kaizen.api.services.actor.controller.ActorController
import zio.ZLayer

package object actor {
  type ActorId   = Long
  type ActorName = String

  val dependencies: ZLayer[Any, Nothing, ActorController] =
    repository.ActorRepository.inMemory >>>
      controller.ActorController.live
}
