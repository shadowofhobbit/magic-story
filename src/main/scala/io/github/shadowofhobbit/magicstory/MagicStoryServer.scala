package io.github.shadowofhobbit.magicstory

import cats.effect.{ConcurrentEffect, Timer}
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.Logger

import scala.concurrent.ExecutionContext.global

object MagicStoryServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F]): Stream[F, Nothing] = {
      val storyAlg = Stories.impl[F]
      val httpApp = MagicStoryRoutes.storyRoutes[F](storyAlg).orNotFound

      // With Middlewares in place
      val finalHttpApp = Logger.httpApp(logHeaders = true, logBody = true)(httpApp)

      val exitCode = BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    exitCode.drain
  }
}
