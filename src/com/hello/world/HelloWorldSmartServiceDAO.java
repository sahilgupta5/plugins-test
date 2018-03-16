package com.hello.world;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloWorldSmartServiceDAO {

  private static final String INSERT_INTO_HELLO_WORLD_TABLE = "INSERT INTO HelloWorldDB.helloWorldTable (name) VALUES (?);";
  private static final String REMOVE_FROM_HELLO_WORLD_TABLE = "DELETE FROM HelloWorldDB.helloWorldTable WHERE id = ?;";
  private static final String COUNT_HELLO_WORLD_TABLE = "SELECT COUNT(id) AS count FROM HelloWorldDB.helloWorldTable;";

  public static void insertHelloWorldInstance(HelloWorldObject helloWorldObjectInstance, Connection connection) throws Exception {
    if (helloWorldObjectInstance == null) {
      throw new Exception("Hello World object instance is null while inserting into HelloWorldDB.helloWorldTable!");
    }

    try (PreparedStatement ps = connection.prepareStatement(INSERT_INTO_HELLO_WORLD_TABLE)) {
      ps.setString(1, helloWorldObjectInstance.getName());
      ps.executeUpdate();
    }
  }

  public static Long getCountHelloWorld(Connection connection) throws SQLException {
    Long count = new Long(0);
    try (PreparedStatement ps = connection.prepareStatement(COUNT_HELLO_WORLD_TABLE)) {
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          count = rs.getLong("count");
        }
      }
    }
    return count;
  }

  public static void removeHelloWorldInstance(HelloWorldObject helloWorldObjectInstance, Connection connection) throws SQLException {
    try (PreparedStatement ps = connection.prepareStatement(REMOVE_FROM_HELLO_WORLD_TABLE)) {
      ps.setLong(1, helloWorldObjectInstance.getId());
      ps.executeUpdate();
    }
  }

}
