package com.juliusbaer.codility.embedded;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

class EmbeddedPostgresDatabase implements EmeddedLocalDatabase {
  private PostgreSQLContainer postgreSQLContainer;
  EmbeddedPostgresDatabase() {

      int containerPort = 5432 ;
      int localPort = 5432 ;
      postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(
                DockerImageName.parse("postgres").asCompatibleSubstituteFor("postgres"))
                .withDatabaseName("postgres")
                .withUsername("fintama")
                .withPassword("fintama")
                .withReuse(true)
                .withExposedPorts(containerPort)
                .withClasspathResourceMapping("db/postgres/init.sql",
                    "/docker-entrypoint-initdb.d/init.sql",
                    BindMode.READ_ONLY)
              .withExposedPorts(5432)
              .withCreateContainerCmdModifier(cmd -> ((CreateContainerCmd)cmd).withHostConfig(
                      new HostConfig().withPortBindings(
                              new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432))
                      )
                )
              );
      postgreSQLContainer.start();
  }

  @Override
  public String getUsername() {
    return postgreSQLContainer.getUsername();
  }

  @Override
  public String getPassword() {
    return postgreSQLContainer.getPassword();
  }

  @Override
  public String getUrl() {
    return postgreSQLContainer.getJdbcUrl();
  }

  @Override
  public void close() {
    postgreSQLContainer.stop();
  }
}
