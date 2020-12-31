package com.kaizen.api.graphql.datasource

import com.kaizen.api.graphql.{ActedInEdge, AddActedIn, CountMoviesActedIn, GetActedIn, GetMoviesActedIn, RemoveActedIn}
import com.kaizen.api.services.RepositoryError
import com.kaizen.api.services.actedIn.controller.{ActedInController, countMoviesActedIn}
import com.kaizen.api.services.actor.ActorId
import com.kaizen.api.services.movie.MovieId
import zio.ZIO
import zio.query.{DataSource, ZQuery}

object ActedInDataSource {
  private val getActedInDS: DataSource[ActedInController, GetActedIn] =
    DataSource
      .fromFunctionM("GetActedIn") { getActedIn =>
        ZIO
          .accessM[ActedInController](_.get.getActedIn(getActedIn.actorId, getActedIn.movieId))
          .map(ActedInEdge.fromActedInData)
      }

  private val countMoviesActedInDS: DataSource[ActedInController, CountMoviesActedIn] =
    DataSource
      .fromFunctionM("CountMoviesActedIn") { countMoviesActedIn =>
        ZIO
          .accessM[ActedInController](_.get.countMoviesActedIn(countMoviesActedIn.actorId))
      }

  private val getMoviesActedInDS: DataSource[ActedInController, GetMoviesActedIn] =
    DataSource
      .fromFunctionM("GetMoviesActedIn") { getMoviesActedIn =>
        ZIO
          .accessM[ActedInController](_.get.getMoviesActedIn(getMoviesActedIn.actorId))
          .map(_.map(ActedInEdge.fromActedInData))
      }

  private val addActedInDS: DataSource[ActedInController, AddActedIn] =
    DataSource
      .fromFunctionM("AddActedIn") { addActedIn =>
        ZIO
          .accessM[ActedInController](_.get.addActedIn(addActedIn.actorId, addActedIn.movieId))
          .map(ActedInEdge.fromActedInData)
      }

  private val removeActedInDS: DataSource[ActedInController, RemoveActedIn] =
    DataSource
      .fromFunctionM("RemoveActedIn") { removeActedIn =>
        ZIO
          .accessM[ActedInController](_.get.removeActedIn(removeActedIn.actorId, removeActedIn.movieId))
      }

  def getActedIn(g: GetActedIn): ZQuery[ActedInController, RepositoryError, ActedInEdge] =
    ZQuery.fromRequest(g)(getActedInDS)

  def getActedIn(actorId: ActorId, movieId: MovieId): ZQuery[ActedInController, RepositoryError, ActedInEdge] =
    getActedIn(GetActedIn(actorId, movieId))


  def countMoviesActedIn(c: CountMoviesActedIn): ZQuery[ActedInController, RepositoryError, Long] =
    ZQuery.fromRequest(c)(countMoviesActedInDS)

  def countMoviesActedIn(actorId: ActorId): ZQuery[ActedInController, RepositoryError, Long] =
    countMoviesActedIn(CountMoviesActedIn(actorId))


  def getMoviesActedIn(g: GetMoviesActedIn): ZQuery[ActedInController, RepositoryError, List[ActedInEdge]] =
    ZQuery.fromRequest(g)(getMoviesActedInDS)

  def getMoviesActedIn(actorId: ActorId): ZQuery[ActedInController, RepositoryError, List[ActedInEdge]] =
    getMoviesActedIn(GetMoviesActedIn(actorId))


  def addActedIn(addActedIn: AddActedIn): ZQuery[ActedInController, RepositoryError, ActedInEdge] =
    ZQuery.fromRequest(addActedIn)(addActedInDS)

  def addActedIn(actorId: ActorId, movieId: MovieId): ZQuery[ActedInController, RepositoryError, ActedInEdge] =
    addActedIn(AddActedIn(actorId, movieId))


  def removeActedIn(removeActedIn: RemoveActedIn): ZQuery[ActedInController, RepositoryError, Unit] =
    ZQuery.fromRequest(removeActedIn)(removeActedInDS)

  def removeActedIn(actorId: ActorId, movieId: MovieId): ZQuery[ActedInController, RepositoryError, Unit] =
    removeActedIn(RemoveActedIn(actorId, movieId))
}
