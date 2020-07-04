package com.kaizen.api.services

import com.kaizen.api.services.actedIn.controller.ActedInController
import zio.ZLayer

package object actedIn {
  val dependencies: ZLayer[Any, Nothing, ActedInController] =
    repository.ActedInRepository.inMemory >>>
      controller.ActedInController.live
}
