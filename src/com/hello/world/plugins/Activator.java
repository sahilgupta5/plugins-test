package com.hello.world.plugins;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

  @Override
  public void start(BundleContext arg0) throws Exception {
    DBConnectionPoolHelper.start();
  }

  @Override
  public void stop(BundleContext arg0) throws Exception {
    DBConnectionPoolHelper.stop();
  }

}
