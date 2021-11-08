package IntegrationTest;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
class MariaDBTest {
    @Container
    static MariaDBContainer mariaDBContainer = new MariaDBContainer("mariadb:10.6.4");

    @Test
    void connection_test() {
        assertTrue(mariaDBContainer.isRunning());
    }
}
