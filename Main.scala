import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.stream._
import akka.stream.scaladsl._
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.ws._
import akka.http.scaladsl.model.HttpMethods._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

case class BenchmarkMessage(c: Int, ts: Long = 0L)

object Main {

  implicit val codec: JsonValueCodec[BenchmarkMessage] = JsonCodecMaker.make

  def getTimestamp() = {
    System.currentTimeMillis() / 1000L
  }

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(Behaviors.empty, "test-system")

    def initialTimestamp() = {
      TextMessage(
        new String(
          writeToArray(BenchmarkMessage(c = 0, ts = getTimestamp())),
          "UTF-8"
        )
      )
    }

    def wsFlow: Flow[Message, Message, Any] =
      Flow[Message].mapConcat {
        case tm: TextMessage =>
          val bm = readFromArray(tm.getStrictText.getBytes("UTF-8"))
          val json = writeToArray(bm.copy(ts = getTimestamp()))
          TextMessage(new String(json, "UTF-8")) :: Nil
        case x =>
          throw new Exception(s"Unhandled message ${x}")
      }

    val websocketRoute =
      pathSingleSlash {
        handleWebSocketMessages(
          wsFlow.prepend(Source(initialTimestamp() :: Nil))
        )
      }

    val bindingFuture =
      Http().newServerAt("0.0.0.0", 8080).bind(websocketRoute)

    println(s"Server started on port 8080")
  }
}
