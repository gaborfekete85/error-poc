package com.juliusbaer.codility.integration;

import com.github.dockerjava.api.command.CreateContainerCmd;
import lombok.SneakyThrows;
import org.junit.runner.RunWith;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@Testcontainers
@RunWith(SpringRunner.class)
@Configuration
@ContextConfiguration(initializers = {SpringIntegrationTest.TestEnvInitializer.class})
public  class SpringIntegrationTest {
    public static GenericContainer axualKafka;

    static {
//        axualKafka.start();
    }

    public static class TestEnvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @SneakyThrows
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
//                    "consumer.endpoint=http://" + axualKafka.getHost() + ":" + axualKafka.getMappedPort(8081),
//                    "producer.endpoint=http://" + axualKafka.getHost() + ":" + axualKafka.getMappedPort(8081)
            );
            values.applyTo(applicationContext);
        }
    }
}