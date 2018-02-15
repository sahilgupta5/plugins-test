package com.hello.world;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public abstract class MockObjectsFactory {
  private static final String TEST_DEFAULT_VALUES_PROPERTIES_FILE = "com/hello/world/plugins/TestDefaultProperties.json";
  private static JsonNode TEST_DEFAULT_PROPERTIES;
  private static final String TEST_DATABASE_CONNECTION_STRING_PROPERTY = "TEST_DATABASE_CONNECTION_STRING";
  protected static final boolean SUCCESS = true;
  protected static final int DEFAULT_REGION = 1; 
  /**
   * To account for the fact that PreparedStatements parameters (?) indices list are 1-based and not 0-based like Lists.
   */
  protected static final int PS_PARAMETERS_INDEX_OFFSET = 1;

  static {
    if (TEST_DEFAULT_PROPERTIES == null) {
      try {
        TEST_DEFAULT_PROPERTIES = getTestDefaultProperties();
      } catch (FileNotFoundException e) {
        //Should not happen. Properties file is always in the project.
        e.printStackTrace();
      }
    }
  }

  private static JsonNode getTestDefaultProperties() throws FileNotFoundException {       
    
    try (InputStream testDefaultProperties = MockObjectsFactory.class.getClassLoader().getResourceAsStream(TEST_DEFAULT_VALUES_PROPERTIES_FILE)) {      
    
      ObjectMapper jsonObjectMapper = new ObjectMapper();      
      return jsonObjectMapper.readTree(testDefaultProperties);                 
    
    } catch (IOException e) {
        throw new FileNotFoundException("Cannot find default unit test properties file: " + TEST_DEFAULT_VALUES_PROPERTIES_FILE);
    }    
  }
  
  public static String getDefaultProperty(String propertyName) {       
    JsonNode propertyValue = TEST_DEFAULT_PROPERTIES.get("testDefaultProperties").get(propertyName);
    if(propertyValue != null) {
      return propertyValue.textValue();
    }
    return null;
  }

  public static Connection getDefaultTestDatabaseConnection() throws Exception {
    return getTestDatabaseConnection(getDefaultProperty(TEST_DATABASE_CONNECTION_STRING_PROPERTY), getDefaultProperty("USR"), getDefaultProperty("PWD"));
  }

  private static Connection getTestDatabaseConnection(String connectionString, String username, String password) throws Exception {
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    return DriverManager.getConnection(connectionString, username, password);
  }
  
  public static boolean createTestDatabase(String databasename, Connection dbConnection) throws Exception {
    Statement createDatabaseStatement = dbConnection.createStatement();
    createDatabaseStatement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databasename);
    return SUCCESS;
  }  

  public static boolean dropTestDatabase(String databasename, Connection dbConnection) throws Exception {
    Statement createDatabaseStatement = dbConnection.createStatement();
    createDatabaseStatement.executeUpdate("DROP DATABASE IF EXISTS " + databasename);
    return SUCCESS;
  }
  
  public static boolean createTestTable(String tableName, Connection dbConnection) throws Exception {

    //Get the table definitions from the cache   
    JsonNode table = TEST_DEFAULT_PROPERTIES.get(tableName);
    
    if(table == null) {
      return !SUCCESS;
    }        
    //JSON cannot store multi-lines text, therefore each line of the DDL is a string in the JSON array of the table object
    ArrayNode createTableStatementLines = (ArrayNode)table.get("createTable");

    StringBuilder createTableStatement = new StringBuilder();

    for (JsonNode jsonNode : createTableStatementLines) {
      createTableStatement.append(jsonNode.textValue() + "\n");
    }
    
    executeSQLStatement(createTableStatement.toString(), dbConnection);

    return SUCCESS;

  }
  
  public static boolean truncateTables(String[] tableNames, String schema, Connection dbConnection)  {
    for (String tableName : tableNames) {
      String truncateStatement = (schema != null ? "TRUNCATE TABLE `" + schema + "`.`" + tableName + "`" : "TRUNCATE TABLE `" + tableName + "`");
      try {
        executeSQLStatement(truncateStatement, dbConnection);
      } catch (SQLException e) {
        // Don't do anything. We can disregard if we can't truncate a table that doesn't exist.
        continue;
      }
    }
    return SUCCESS;
  }

  public static boolean dropTables(String[] tableNames, String schema, Connection dbConnection) throws SQLException {
    for (String tableName : tableNames) {
      String dropStatement = (schema != null ? "DROP TABLE IF EXISTS  `" + schema + "`.`" + tableName + "`" : "DROP TABLE IF EXISTS  `" + tableName + "`");
      executeSQLStatement(dropStatement, dbConnection);
    }
    return SUCCESS;
  }
  
  protected static void executeSQLStatement(String sqlStatementString, Connection dbConnection) throws SQLException {
    try (Statement sqlStatement = dbConnection.createStatement()) {
      sqlStatement.execute(sqlStatementString);
    }
  }
}