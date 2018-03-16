package com.hello.world;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class HelloWorldSmartServiceDAOTest {

  @BeforeClass
  public static void setupHelloWorldDatabase() throws Exception {
    try (Connection defaultDBConnection = MockObjectsFactory.getDefaultTestDatabaseConnection()) {
      MockObjectsFactory.createTestDatabase("HelloWorldDB", defaultDBConnection);
    }
    try (Connection helloWorldDBMockConnection = MockObjectsFactory.getHelloWorldDBMockConnection()) {
      MockObjectsFactory.createTestTable("helloWorldTable", helloWorldDBMockConnection);
    }
  }

  @AfterClass
  public static void dropHelloWorldSchema() throws Exception {
    try (Connection helloWorldDBMockConnection = MockObjectsFactory.getDefaultTestDatabaseConnection()) {
      MockObjectsFactory.dropTestDatabase("HelloWorldDB", helloWorldDBMockConnection);
    }
  }

  @Test
  public void helloWorldInstanceTest() throws Exception {
    try (Connection helloWorldDBMockConnection = MockObjectsFactory.getDefaultTestDatabaseConnection()) {
      // Arrange
      HelloWorldObject mockObject = new HelloWorldObject();
      mockObject.setName("Hello");
      mockObject.setId(1L);
      // Act
      HelloWorldSmartServiceDAO.insertHelloWorldInstance(mockObject, helloWorldDBMockConnection);
      HelloWorldSmartServiceDAO.removeHelloWorldInstance(mockObject, helloWorldDBMockConnection);

      // Assert
      assertTrue(HelloWorldSmartServiceDAO.getCountHelloWorld(helloWorldDBMockConnection) == 0L);
    }
  }

}
