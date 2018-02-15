package com.hello.world;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloWorldSmartServiceDAO {

  private static final String INSERT_INTO_HELLO_WORLD_TABLE = "INSERT INTO HelloWorldDB.helloWorldTable (name) VALUES (?);";
  private static final String REMOVE_FROM_HELLO_WORLD_TABLE = "DELETE FROM HelloWorldDB.helloWorldTable WHERE id = ?;";

  public static void insertHelloWorldInstance(HelloWorldObject helloWorldObjectInstance, Connection connection) throws Exception {
    if (helloWorldObjectInstance == null) {
      throw new Exception("Hello World object instance is null while inserting into HelloWorldDB.helloWorldTable!");
    }

    try (PreparedStatement ps = connection.prepareStatement(INSERT_INTO_HELLO_WORLD_TABLE)) {
      ps.setString(1, helloWorldObjectInstance.getName());
      ps.executeUpdate();
    }
  }

  public static void removeHelloWorldInstance(HelloWorldObject helloWorldObjectInstance, Connection connection) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(REMOVE_FROM_HELLO_WORLD_TABLE)) {
      ps.setLong(1, helloWorldObjectInstance.getId());
      ps.executeUpdate();
    }
  }

}
