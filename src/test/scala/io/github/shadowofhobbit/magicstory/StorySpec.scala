package io.github.shadowofhobbit.magicstory

import cats.effect.IO
import munit.CatsEffectSuite
import org.http4s._
import org.http4s.implicits._

class StorySpec extends CatsEffectSuite {

  test("Story returns status code 200") {
    assertIO(retStory.map(_.status) ,Status.Ok)
  }

//  test("Story returns a story message") {
//    assertIO(retStory.flatMap(_.as[String]), "{\"message\":\"Hello, world\"}")
//  }

  private[this] val retStory: IO[Response[IO]] = {
    val getStory = Request[IO](Method.GET, uri"/story")
    val story = Stories.impl[IO]
    MagicStoryRoutes.storyRoutes(story).orNotFound(getStory)
  }
}
