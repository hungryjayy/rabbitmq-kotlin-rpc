package developer.rpc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RpcApplication

fun main(args: Array<String>) {
	runApplication<RpcApplication>(*args)
}
