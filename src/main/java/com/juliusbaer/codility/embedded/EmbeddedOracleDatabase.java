package com.juliusbaer.codility.embedded;


import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

class EmbeddedOracleDatabase implements EmeddedLocalDatabase {
    private OracleContainer oracleContainer;

    private PostgreSQLContainer postgreSQLContainer;

    EmbeddedOracleDatabase() {
        oracleContainer = new OracleContainer(DockerImageName.parse("gvenzl/oracle-xe").asCompatibleSubstituteFor("gvenzl/oracle-xe")) // https://hub.docker.com/r/gvenzl/oracle-xe
                .withUsername("fintama")
                .withPassword("fintama")
                .withInitScript("db/oracle/scripts/create_table.sql")
                .withExposedPorts(1521)
                .withCreateContainerCmdModifier(cmd -> ((CreateContainerCmd)cmd).withHostConfig(
                                new HostConfig().withPortBindings(
                                        new PortBinding(Ports.Binding.bindPort(1521), new ExposedPort(1521))
                                )
                        )
                );
//                .withClasspathResourceMapping("db/oracle/scripts", "/opt/oracle/scripts/startup", BindMode.READ_ONLY)
        oracleContainer.start();
    }

    @Override
    public String getUsername() {
        return oracleContainer.getUsername();
    }

    @Override
    public String getPassword() {
        return oracleContainer.getPassword();
    }

    @Override
    public String getUrl() {
        return oracleContainer.getJdbcUrl();
    }

    @Override
    public void close() {
        oracleContainer.stop();
    }
}
