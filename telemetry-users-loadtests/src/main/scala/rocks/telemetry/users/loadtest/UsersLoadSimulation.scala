package rocks.telemetry.users.loadtest

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class UsersLoadSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080")
    .header("Content-Type", "application/json")

  val scn = scenario("BasicSimulation")
    .exec(http("request_1")
      .post("/api/user")
      .body(StringBody("""{"firstName": "Greg", "lastName": "Maslowski", "email": "gregmaslowski@gmail.com"}""")))

  setUp(
    scn.inject(rampUsers(8000).over(30 seconds))
  ).protocols(httpConf)
}
