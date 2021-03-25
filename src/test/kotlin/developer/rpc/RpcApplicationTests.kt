package developer.rpc

import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlinx.coroutines.*

@SpringBootTest
class RpcApplicationTests {

	@Autowired
	private val client: Client ?= null

	@Test
	fun firstTest(){
		val num = 7
		val jsonObj = JSONObject()
		jsonObj.put("num", num)
		jsonObj.put("s", "example")
		println(" [TEST1] ")
		GlobalScope.launch {
			val result = client!!.send(jsonObj, routingKey = "first")
			println(" [TEST1] $result")
		}
		println(" [TEST1] Non-blocking start")
		println(" [TEST1] Non-blocking in progress")
		Thread.sleep(7L)
		println(" [TEST1] Non-blocking in progress 2")
		Thread.sleep(7L)
		println(" [TEST1] Non-blocking in progress 3")
		Thread.sleep(7L)
		println(" [TEST1] Non-blocking in progress 4")
		Thread.sleep(7L)
		println(" [TEST1] Non-blocking in progress 5")
	}

	@Test
	fun secondTest(){
		val num = 6
		val jsonObj = JSONObject()
		jsonObj.put("num", num)
		println(" [TEST2] ")
		val result = client!!.send(jsonObj, routingKey = "second")
		println(" [TEST2] $result")
	}

	@Test
	fun thirdTest(){
		val num = 5
		val str = "third Str"
		val jsonObj = JSONObject()
		jsonObj.put("num", num)
		jsonObj.put("s", str)
		println(" [TEST3]")
		val result = client!!.send(jsonObj, routingKey = "third")
		println(" [TEST3] $result")
	}

}
