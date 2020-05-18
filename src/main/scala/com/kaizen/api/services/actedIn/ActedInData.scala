package com.kaizen.api.services.actedIn

import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieId

final case class ActedInData(actorId: ActorId, movieId: MovieId)
