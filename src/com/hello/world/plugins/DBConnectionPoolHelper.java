package com.hello.world.plugins;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import org.apache.commons.dbcp2.BasicDataSource;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hello.world.MockObjectsFactory;

public class DBConnectionPoolHelper {
  
  private static BasicDataSource connectionPool = null;
  public static final String MANAGER_LOCAL_DATABASE_URL = "jdbc:mysql://localhost:3306/AppianAnywhere";
  private static final String DEFAULT_VALUES_PROPERTIES_FILE = "com/hello/world/plugins/TestDefaultProperties.json";
  private static JsonNode DEFAULT_PROPERTIES;

  static {
    if (DEFAULT_PROPERTIES == null) {
      try {
        DEFAULT_PROPERTIES = getDefaultProperties();
      } catch (FileNotFoundException e) {
        //Should not happen. Properties file is always in the project.
        e.printStackTrace();
      }
    }
  }

  public static synchronized void start() throws Exception {
    if(connectionPool == null) {
      connectionPool = new BasicDataSource();
      connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
      connectionPool.setMaxTotal(20);
      connectionPool.setMaxIdle(5);
      connectionPool.setUsername(MockObjectsFactory.getDefaultProperty("USR"));
      connectionPool.setPassword(MockObjectsFactory.getDefaultProperty("PWD"));
      connectionPool.setUrl(MANAGER_LOCAL_DATABASE_URL);
    }
  }

  public static synchronized void stop() throws Exception {
    if(connectionPool != null) {
      connectionPool.close();
    }
  }
  
  public static Connection getConnection() throws Exception{
    if(connectionPool == null) {
      start();
    }
    
    return connectionPool.getConnection();
  }
  
  private static JsonNode getDefaultProperties() throws FileNotFoundException {       
    try (InputStream defaultProperties = MockObjectsFactory.class.getClassLoader().getResourceAsStream(DEFAULT_VALUES_PROPERTIES_FILE)) {      
    
      ObjectMapper jsonObjectMapper = new ObjectMapper();      
      return jsonObjectMapper.readTree(defaultProperties);                 
    
    } catch (IOException e) {
        throw new FileNotFoundException("Cannot find default unit test properties file: " + DEFAULT_VALUES_PROPERTIES_FILE);
    }    
  }

}
