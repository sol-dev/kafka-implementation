Proyecto en Spring Boot implementando Kafka y arquitectura Hexagonal

### Tecnologías:
- Spring Boot 3.5
- Apache Kafka con Apache Avro
- Zookeeper
- Confluent Schema Registry
- Confluent Control Center
- Java 21

Se utiliza la distribucion de Confluent de Kafka, en el ecosistema son necesarios los siguientes servicios:

1. **Zookeeper(Puerto 2181)** </br>
Encargado de coordinar el cluster. Guarda la configuración del estado del cluster, decide que broker es el líder de cada partición, detecta si un broker se cae y mantiene la sincronización con la metadata del sistema.
2. **Kafka Broker(Puerto 9092)** </br>
El servidor de Kafka. Recibe los mensajes de los Producers, los guarda en disco de forma ordenada en los topics y los entrega a los Consumidores.
3. **Schema Registry(Puerto 8081)** </br>
Es un servicio centralizado que almacena un Diccionario de esquemas Avro. Antes de que un productor envíe un mensaje, verifica que la estructura del mensaje sea válida, idem cuando un Consumer necesita leer un mensaje, se necesita el esquema del productor para poder deserializar.
4. **Confluent Control Center(Puerto 9021)** </br>
Es una herramienta visual de Confluent (una página web) que permite administrar y monitorear el clúster. Permite ver si se están creando bien los tópicos, inspeccionar mensajes, ver la salud del cluster y revisar los esquemas.

### Microservicios:
1. **order-service** [Puerto 8080 - MySQL puerto 3306]
2. **inventory-service** [Puerto 8082 - MySQL puerto 3307]

### Caso de uso:

#### A) At-Least-Once Delivery con Consumidor Idempotente </br>
topic: order.created.events </br>
<u>Producer</u>: order-service POST/orders/v1/orders </br>
acks: all -> Garantía de entrega a todas las réplicas </br>
enable.idempotence=true  </br>


<u>Consumer</u>: inventory-service </br>
ack-mode: manual_immediate -> Para hacer el commit manualmente tras verificar la idempotencia.  </br>
- El consumidor hace ack.acknowledge() después de procesar el mensaje.
- Si el consumidor se cae a mitad del proceso, Kafka reentregará el mensaje.
- No se pierden mensajes pero hay riesgo de duplicidad. Por este motivo se agrega la validación de idempotencia en el consumidor.

#### B) DeadLetterQueue At-Least-Once Delivery con Consumidor Idempotente </br>
**order-service** publica un mensaje en el topic order.created.events.
**inventory-service:**
1. Listener KafkaOrderConsumerAdapter escucha el mensaje e intenta procesarlo 3 veces sin éxito
2. El framework del consumidor actúa como productor, genera un mensaje DLQ y se envía al topic order.created.events-dlt
3. El listener marca el mensaje del tópico original como "consumido", envía ack y Kafka avanza el offset
4. Un segundo listener de la DLQ KafkaDLQConsumerAdapter escucha el tópico order.created.events-dlt
5. Guarda el error en la base de datos, ack para mover el offset de este tópico

### Pasos para levantar el proyecto
1. `mvn clean install`
2. `docker compose up -d` para generar los contenedores 
3. `cd order-service/ && mvn spring-boot:run`
4. `cd ../inventory-service/ && mvn spring-boot:run`
