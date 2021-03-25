package developer.rpc

import org.json.JSONObject
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.annotation.*
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.stereotype.Component

@Component
class Server{ // Consumer(Agent) role

    @RabbitListener(bindings = [QueueBinding(
            value = Queue( ""),
            exchange = Exchange(name = "rpc", type = ExchangeTypes.DIRECT),
            key = ["first"])])
    fun first(message: String): String {
        val req = JSONObject(message)
        val n = req["num"] as Int
        val s = req["s"] as String
        println(" [SERVER 1] Received request for $n")
        val value = fib(n)
        println(" [SERVER 1] Returned $value")
        return """{ "num": $value, "str": "$s" }""".trimIndent()
    }

    @RabbitListener(bindings = [QueueBinding(
            value = Queue( ""),
            exchange = Exchange(name = "rpc", type = ExchangeTypes.DIRECT),
            key = ["second"])])
    fun second(message: String): String {
        val req = JSONObject(message)
        val n: Int = req["num"] as Int
        println(" [SERVER 2] Received request for $n")
        val value = fib(n)
        println(" [SERVER 2] Returned $value")
        return """{ "num": $value }""".trimIndent()
    }

    @RabbitListener(bindings = [QueueBinding(
            value = Queue( ""),
            exchange = Exchange(name = "rpc", type = ExchangeTypes.DIRECT),
            key = ["third"])])
    fun third(message: String): String {
        val req = JSONObject(message)
        val n = req["num"] as Int
        val s = req["s"] as String
        println(" [SERVER 3] Received request for $n")
        val value = fib(n)
        println(" [SERVER 3] Returned $value")
        return """{ "num": $value, "str": "$s" }""".trimIndent()
    }

    fun fib(n: Int): Int { // example method(message)
        return if (n == 0) 0 else if (n == 1) 1 else fib(n - 1) + fib(n - 2)
    }
}