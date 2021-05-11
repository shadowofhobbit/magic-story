package io.github.shadowofhobbit.magicstory

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    MagicStoryServer.stream[IO].compile.drain.as(ExitCode.Success)
}
