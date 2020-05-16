package com.kaizen.api.actor

import com.kaizen.api.RepositoryError
import zio.stm.TMap
import zio.{ Has, ZLayer }
import zquery.{ DataSource, Request, ZQuery }

package object repository {
  type ActorRepository     = Has[ActorRepository.Service]
  type ActorRepositoryImpl = Has[ActorRepository.Impl]

  final case class GetActorById(id: ActorId) extends Request[RepositoryError, ActorData]
  final case class UpdateActor(movie: ActorData) extends Request[RepositoryError, Unit]
  final case class DeleteActor(movie: ActorData) extends Request[RepositoryError, Unit]

  object ActorRepository {
    class Service(impl: Impl) {
      def getById(id: ActorId): ZQuery[Any, RepositoryError, ActorData] =
        ZQuery.fromRequest(GetActorById(id))(impl.getById)

      def update(actor: ActorData): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(UpdateActor(actor))(impl.update)

      def delete(actor: ActorData): ZQuery[Any, RepositoryError, Unit] =
        ZQuery.fromRequest(DeleteActor(actor))(impl.delete)
    }

    private[repository] trait Impl {
      val getById: DataSource[Any, GetActorById]
      val update: DataSource[Any, UpdateActor]
      val delete: DataSource[Any, DeleteActor]
    }

    private lazy val svc: ZLayer[ActorRepositoryImpl, Nothing, ActorRepository] =
      ZLayer.fromFunction(impl => new Service(impl.get))

    private lazy val inMemoryImpl: ZLayer[Any, Nothing, ActorRepositoryImpl] =
      ZLayer.fromEffect(
        TMap
          .empty[ActorId, ActorData]
          .map(new InMemoryActorRepository(_))
          .commit
      )

    lazy val inMemory: ZLayer[Any, Nothing, ActorRepository] = inMemoryImpl >>> svc
  }
}
