package rocks.telemetry.users.loadtest

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

/**
  * Quick solution for running a Gatling simulation as a standalone process
  */
object GatlingRunner extends App {
  override def main(args: Array[String]) {
    val simClass = classOf[UsersLoadSimulation].getName

    val props = new GatlingPropertiesBuilder
    props.sourcesDirectory("./src/main/scala")
    props.binariesDirectory("./target/scala-2.11/classes")
    props.simulationClass(simClass)
    //    props.runDescription(config.runDescription)
    props.outputDirectoryBaseName("./target/gatling")

    Gatling.fromMap(props.build)
  }
}
