package developer.rpc

import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.AsyncRabbitTemplate
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean
    fun template(rabbitTemplate: RabbitTemplate?): AsyncRabbitTemplate? {
        return AsyncRabbitTemplate(rabbitTemplate)
    }

    @Bean
    fun queue(): Queue? {
        return Queue("")
    }

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange("rpc")
    }
}
