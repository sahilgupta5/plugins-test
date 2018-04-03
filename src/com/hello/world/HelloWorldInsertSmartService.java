package com.hello.world;

import java.sql.Connection;

import com.appiancorp.suiteapi.common.Name;
import com.appiancorp.suiteapi.process.exceptions.SmartServiceException;
import com.appiancorp.suiteapi.process.framework.AppianSmartService;
import com.appiancorp.suiteapi.process.framework.Input;
import com.appiancorp.suiteapi.process.palette.PaletteCategoryConstants;
import com.appiancorp.suiteapi.process.palette.PaletteInfo;
import com.hello.world.plugins.DBConnectionPoolHelper;

@PaletteInfo(paletteCategory = PaletteCategoryConstants.INTEGRATION_SERVICES, palette = "Hello World Operations")

public class HelloWorldInsertSmartService extends AppianSmartService {

  private String name;

  @Input
  @Name("name")
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void run() throws SmartServiceException {
    try {
      try (Connection connection = DBConnectionPoolHelper.getConnection()) {
        HelloWorldObject object = new HelloWorldObject();
        object.setName(name);
        HelloWorldSmartServiceDAO.insertHelloWorldInstance(object, connection);
        System.out.println("Hello");
      }
    } catch (Exception e) {
      throw new SmartServiceException.Builder(getClass(), e).userMessage("error.exception", e.getMessage()).build();
    }
  }

}
