package developer.rpc

import org.json.JSONObject
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.rabbit.AsyncRabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Client { // Producer role

    @Autowired
    private val template: AsyncRabbitTemplate? = null

    @Autowired
    private val exchange: DirectExchange? = null

    fun send(req: JSONObject, routingKey: String): JSONObject { // callRPC role
        println(" [CLIENT] Request JSON object is $req")
        val response = template!!.convertSendAndReceive<String>(exchange!!.name, routingKey, req.toString())
        println(" [CLIENT] Waiting response from server")
        val result = JSONObject(response.get())
        println(" [CLIENT] got $result")
        return result
    }
}