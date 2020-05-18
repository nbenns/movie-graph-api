package com.kaizen

import com.kaizen.api.services.actedIn.controller.ActedInController
import com.kaizen.api.services.actor.controller.ActorController
import com.kaizen.api.services.movie.controller.MovieController
import zio.{ZEnv, ZIO}

package object api {
  type Controllers = MovieController with ActorController with ActedInController

  type ZAppEnv = ZEnv with Controllers

  type ZApp[+A] = ZIO[ZAppEnv, Throwable, A]
}
