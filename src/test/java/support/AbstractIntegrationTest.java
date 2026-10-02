package support;

import com.redis.testcontainers.RedisContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

@Testcontainers
public abstract class AbstractIntegrationTest {

    private static final String MYSQL_DATABASE_NAME =
            "users_test";
    private static final MySQLContainer mysqlDb;

    private static final RedisContainer redisDB;

    protected static final KafkaContainer kafka;

    static {
        mysqlDb = new MySQLContainer("mysql:8.0")
                .withDatabaseName(MYSQL_DATABASE_NAME)
                .withUsername("test")
                .withPassword("test");
        redisDB = new RedisContainer(DockerImageName.parse("redis:6.2.6"));

        kafka = new KafkaContainer(
                DockerImageName.parse("apache/kafka:3.9.2")
        );

        Startables.deepStart(
                Stream.of(
                        mysqlDb,
                        redisDB,
                        kafka
                )
        ).join();
    }

    @DynamicPropertySource
    static void registerProperties(
            DynamicPropertyRegistry registry
    ) {
        registry.add(
                "spring.datasource.username",
                mysqlDb::getUsername
        );

        registry.add(
                "spring.datasource.password",
                mysqlDb::getPassword
        );

        registry.add(
                "spring.datasource.url",
                mysqlDb::getJdbcUrl
        );

        registry.add(
                "spring.data.redis.host",
                redisDB::getHost
        );

        registry.add(
                "spring.data.redis.port",
                () -> redisDB.getRedisPort()
        );

        registry.add(
                "spring.data.redis.database",
                () -> 0
        );

        registry.add(
                "spring.data.redis.connect-timeout",
                () -> "2s"
        );

        registry.add(
                "spring.data.redis.timeout",
                () -> "2s"
        );

        registry.add(
                "spring.kafka.bootstrap-servers",
                kafka::getBootstrapServers
        );
    }
}