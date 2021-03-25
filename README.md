# RabbitMQ, Kotlin를 이용해 비동기 RPC 통신

## Rabbitmq란?
오픈소스 메시지 브로커이다. 가볍고, 쉬우며 다양한 messaging protocol을 지원한다.

* Async messaging을 지원한다.
* Docker 환경에 적합.
* 높은 성숙도(기능이 많다), 처리율 높은 편
* Authentication mechanisms (TLS, LDAP)
<br>

## 주요 개념
<img src = "./images/rabbitMQ.png">

#### Producer
- 메시지 생성, 발송하는 역할
- 메시지가 Queue에 저장되고, Producer는 Exchange를 통해 Queue에 접근.<br>

#### Consumer
- 메시지 수신하는 역할
- Queue에서 직접 가져옴.<br>

#### Queue
- Producer에 의해 발송된 메시지가 저장되는 곳
- 미들웨어 역할
- Server 종료시 queue 리셋 (유지하려면 Durable = true / PERSISTENT_TEXT_PLAIN옵션 설정)<br>

#### Exchange
- Queue에 종류가 있기 때문에 여기서 적절한 Queue로 메시지 전달(Routing)
- Router 역할.<br>

     ##### Exchange 종류
      * Direct
          Routing key를 통해 보냄. 하나의 큐에 여러 routing key 가능.
      * Topic
          Routing pattern을 이용.
      * Headers
          Topic과 비슷. header를 쓴다.
      * Fanout
          Exchange에 등록된 모든 Queue에 전송.

#### Binding
- Exchange에게 메시지를 routing 할 규칙 지정. <br>
<br>

# RabbitMQ - RPC

<img src = "./images/rpc.png">

- 서버와 서버를 연결하는 queue 역할이기 때문에 언제 죽을지 모름. 그러므로 HA를 위해 에러 처리를 잘해야 한다.
- 아래의 작업들은 모두 `RabbitTemplate`을 통해 자동으로 동작한다.

1. `Configuration`이 새로운 `DirectExchange` 와 client set up

2. client는 `convertSendAndReceive` 메서드를 통해 exchange name, routing Key, message를 queue(`rpc_queue`)로 보낸다.

3. RPC worker(server)는 request를 기다리다가 queue에서 request가 오면 작업을 수행하고, queue를 통해 결과 message를 `reply_to`에 명시되어있는 client에게 보낸다.

4. client는 callback queue를 기다리다가 message가 오면 `correlationId` 를 체크한다. request에서의 value와 일치한다면 application으로 response를 return한다.<br><br><br>


##### Reference
https://blog.dudaji.com/general/2020/05/25/rabbitmq.html

## Kotlin-RabbitMQ-RPC 이슈 및 기록

RabbitMQ를 통해 RPC 구현 <br><br>

## 201204 
### Kotlin<br>

* Kotlin 가변인수 vararg<br>
* Kotlin JSON - put을 사용?? receiver 입장에선 JSON의 구조를 모름.<br>

### RabbitMQ

* 핵심 연결 역할: `convertSendAndReceive()` JSON으로 전달 안 됨.<br>
  * `sendAndReceive`는 message로 자동 convert 지원하지 않는다.<br>
  * Kotlin -> JSON 쉽게 변환하는 방법 `""" """` 이용<br>
  * 가변인자로 받은 경우 각각 parsing시 `as Int`, `as String` 등 명시<br>
* `RabbitTemplate`vs `AsyncRabbitTemplate`<br>

## 201207 AsyncRabbit, Rabbit 차이 비교
<br>

* Queue name 설정하면 bind 안되는이유는?

좋은 예제 https://reflectoring.io/amqp-request-response/

https://cheese10yun.github.io/spring-rabbitmq/

https://devahea.github.io/2019/04/30/AMQ-%EB%AA%A8%EB%8D%B8%EA%B3%BC-Exchange-Queue-Binding-%EC%97%90-%EB%8C%80%ED%95%B4/

## 201210 연구 끝.<br>
* Service <-> MQ(broker) JSON
* MQ(broker)-> Agent(receiver) String
* Agent가 parsing 후 처리<br>

* `convertSendAndReceive()` JSON 전달 불가<br>
  * `JSONObject().toString()` 전달<br>

* Unit test에 Coroutine 도입해야 완전한 비동기 테스트<br>

  

 ### RabbitTemplate vs AsyncRabbitTemplate<br>
Test 1 ) RabbitTemplate<br>
 <img src = "./images/rabbitTemplate.png"><br>

 - Unit test, Client == broker, server == agent

 Test 2) AsyncRabbitTemplate<br>
 <img src = "./images/asyncRabbitTemplate.png"><br>

 - Unit test, Client == broker, server == agent

