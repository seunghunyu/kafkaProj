import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/*
 1.Spring 환경이 아닌 일반 java 환경에서 테스트 할 경우에 log4j.properties 파일을 프로젝트 내에 만들어놔야 한다.
 @@log4j.properties 내용@@
    # Set root logger level to DEBUG and its only appender to A1.
    log4j.rootLogger=DEBUG, A1
    # A1 is set to be a ConsoleAppender.
    log4j.appender.A1=org.apache.log4j.ConsoleAppender
    # A1 uses PatternLayout.
    log4j.appender.A1.layout=org.apache.log4j.PatternLayout

 2.Windows 환경에서 컨슈머 코드 테스트 진행시에 Kafka producer 서버가 떠있는 즉 bootstrap.servers 의 IP를
   C:\Windows\System32\drivers\etc 경로의 hosts 파일에 hostname 과 함께 등록해줘야한다.
   ex)192.168.XX.XX  테스트-호스트

 */
public class SimpleKafkaConsumer {
    public static void main(String[] args){

        Properties props = new Properties();

        props.put("bootstrap.servers", "192.168.20.43:9092");
        props.put("session.timeout.ms", "10000"); // session 설정
        props.put("group.id", "test-consumer-group"); // 그룹아이디 설정
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); // key deserializer
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); // value deserializer
        props.put("auto.offset.reset", "latest"); // earliest(처음부터 읽음) | latest(현재부터 읽음)
        props.put("enable.auto.commit", false); //AutoCommit 여부

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("sample-topic")); // 구독할 topic 설정

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                for (ConsumerRecord<String, String> record : records)
                    System.out.println(record.value());
            }
        } finally {
            consumer.close();
        }

    }
}
