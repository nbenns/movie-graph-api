package com.kaizen.api.actor

import com.kaizen.api.RepositoryError
import com.kaizen.api.actedIn.ActedIn
import com.kaizen.api.actedIn.controller.ActedInController
import com.kaizen.api.movie.controller.MovieController
import zquery.ZQuery

final case class Actor(
  id: ActorId,
  name: ActorName,
  actedIn: ZQuery[ActedInController with MovieController, RepositoryError, List[ActedIn]]
)
