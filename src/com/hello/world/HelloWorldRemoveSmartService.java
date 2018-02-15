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

public class HelloWorldRemoveSmartService extends AppianSmartService {

  private Long id;

  @Input
  @Name("id")
  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public void run() throws SmartServiceException {
    try {
      try (Connection connection = DBConnectionPoolHelper.getConnection()) {
        HelloWorldObject object = new HelloWorldObject();
        object.setId(id);
        HelloWorldSmartServiceDAO.removeHelloWorldInstance(object, connection);
      }
    } catch (Exception e) {
      throw new SmartServiceException.Builder(getClass(), e).userMessage("error.exception", e.getMessage()).build();
    }
  }

}
