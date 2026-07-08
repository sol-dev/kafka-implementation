Proyecto en Spring Boot implementando Kafka

Tecnologías:
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

