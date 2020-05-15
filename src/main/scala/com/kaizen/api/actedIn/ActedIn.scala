package com.kaizen.api.actedIn

import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.MovieId

final case class ActedIn(actorId: ActorId, movieId: MovieId)
