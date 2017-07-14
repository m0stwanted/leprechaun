package pro.sholokhov.config.impl;

import pro.sholokhov.config.Configuration;

public class DefaultConfiguration implements Configuration {

  @Override
  public int port() {
    return 7070;
  }

}
