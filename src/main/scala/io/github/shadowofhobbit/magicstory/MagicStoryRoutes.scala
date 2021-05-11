package io.github.shadowofhobbit.magicstory

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object MagicStoryRoutes {

  def storyRoutes[F[_]: Sync](S: Stories[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "story" =>
        for {
          story <- S.generate()
          resp <- Ok(story)
        } yield resp
    }
  }
}
