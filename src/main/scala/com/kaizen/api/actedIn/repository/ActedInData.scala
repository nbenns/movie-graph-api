package com.kaizen.api.actedIn.repository

import com.kaizen.api.actor.ActorId
import com.kaizen.api.movie.MovieId

final case class ActedInData(actorId: ActorId, movieId: MovieId)
