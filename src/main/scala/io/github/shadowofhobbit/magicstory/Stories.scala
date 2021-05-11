package io.github.shadowofhobbit.magicstory

import cats.Applicative
import cats.implicits.catsSyntaxApplicativeId
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import scala.util.Random

trait Stories[F[_]]{
  def generate(): F[Stories.Story]
}

object Stories {
  private val nouns = Vector("A theoretical physicist", "A software developer",
    "A project manager", "A student", "An astronaut", "A chess grandmaster")
  private val verbs = Vector("turns into", "finds", "meets", "falls in love with")
  private val moveVerbs = Vector("wakes up in", "appears in")
  private val creatures = Vector("a dementor", "a drunk centaur", "an elf", "a witcher", "a witch", "a hobbit")
  private val places = Vector("Narnia", "Hogwarts", "Rivendell", "the medieval Spain")

  implicit def apply[F[_]](implicit ev: Stories[F]): Stories[F] = ev

  final case class Story(story: String) extends AnyVal

  object Story {
    implicit val greetingEncoder: Encoder[Story] = (a: Story) => Json.obj(
      ("message", Json.fromString(a.story)),
    )
    implicit def greetingEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Story] =
      jsonEncoderOf[F, Story]
  }

  def impl[F[_]: Applicative]: Stories[F] = () => {
    val random = Random
    val noun = nouns(random.nextInt(nouns.length))
    val moved = random.nextBoolean()
    if (moved) {
      val verb = moveVerbs(random.nextInt(moveVerbs.length))
      val place = places(random.nextInt(places.length))
      Story(s"$noun $verb $place.").pure[F]
    } else {
      val verb = verbs(random.nextInt(verbs.length))
      val creature = creatures(random.nextInt(creatures.length))
      Story(s"$noun $verb $creature.").pure[F]
    }
  }
}
