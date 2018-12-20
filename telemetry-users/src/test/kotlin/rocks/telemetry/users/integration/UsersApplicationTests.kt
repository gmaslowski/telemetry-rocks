package rocks.telemetry.users.integration

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import rocks.telemetry.users.UsersApplication

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(UsersApplication::class))
class UsersApplicationTests {

    @Test
    fun contextLoads() {
    }
}
