package rocks.telemetry.core.pubsub

import java.util.Collections.singletonList

import akka.actor.{Actor, ActorLogging, Props}
import com.google.api.gax.core.GoogleCredentialsProvider
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf
import com.google.pubsub.v1.{PubsubMessage, TopicName}
import rocks.telemetry.core.live.model.LapInformation.LapData
import rocks.telemetry.core.live.model.TelemetryModel.TelemetryDataWithPlayer

object PubSubPublisher {
  def props = Props(classOf[PubSubPublisher])
}

class PubSubPublisher extends Actor with ActorLogging {

  private val publisher: Publisher = Publisher
    .newBuilder(TopicName.of("telemetry-rocks", "lapTimes"))
    .setCredentialsProvider(GoogleCredentialsProvider.newBuilder().setScopesToApply(singletonList("https://www.googleapis.com/auth/pubsub")).build())
    .build()


  override def receive = {
    case TelemetryDataWithPlayer(player, lapData: LapData) =>
      if (lapData.lap != 0 && lapData.sector3 != 0) {
        val formatted = s"${lapData.sector1}:${lapData.sector2}:${lapData.sector3}:${lapData.lap}:${player.email}:${lapData.track}"
        publisher.publish(PubsubMessage.newBuilder().setData(protobuf.ByteString.copyFromUtf8(formatted)).build())
      }
  }
}
