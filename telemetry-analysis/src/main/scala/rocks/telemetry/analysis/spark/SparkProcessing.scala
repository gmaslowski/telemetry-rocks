package rocks.telemetry.analysis.spark

import com.mongodb.spark.sql._
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel.MEMORY_ONLY
import org.apache.spark.streaming.pubsub.{PubsubUtils, SparkGCPCredentials}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkProcessing {

  case class LapTime(s1: Integer, s2: Integer, s3: Integer, lapTime: Integer, user: String, track: String)

  val MONGODB = System.getenv("MONGODB_URI")

  def startSpark = {

    val spark = SparkSession.builder()
      .master("local[3]")
      .appName("telemetry-analysis")
      .config("spark.mongodb.input.uri", MONGODB)
      .config("spark.mongodb.output.uri", MONGODB)
      .config("spark.mongodb.output.collection", "lapTimes")
      .config("spark.driver.memory", "1g")
      .getOrCreate()

    val ssc = new StreamingContext(spark.sparkContext, Seconds(10))
    val GCPCredentials = SparkGCPCredentials.builder.jsonServiceAccount("/opt/gcp.json").build()

    PubsubUtils
      .createStream(ssc, "telemetry-rocks", Option("lapTimes"), "lapTimeAnalysis", GCPCredentials, MEMORY_ONLY)
      .map(pubSubMessage => new String(pubSubMessage.getData()).toString)
      .map(dataString => dataString.split(":"))
      .foreachRDD(rdd => {
        import spark.implicits._
        import LaptTimes._
        val lapTimes = rdd.map({
          case (arr: Array[String]) => LapTime(
            arr(0) asMillis,
            arr(1) asMillis,
            arr(2) asMillis,
            arr(3) asMillis,
            arr(4), // user
            arr(5)) // track
        }).toDF()
        lapTimes.write.mode("append").mongo()
      })

    ssc.start()
    ssc.awaitTermination()
  }
}

object LaptTimes {
  implicit class Times(time: String) {
    def asMillis = (time.toFloat * 1000).intValue
  }
}