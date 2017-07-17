package pro.sholokhov.handlers;

import com.google.inject.Singleton;
import pro.sholokhov.server.App;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.service.Service;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class HelpHandler implements Handler, Service {

  private FileSystem fs = null;
  private Path path = null;

  @Override
  public void onStart(StartEvent event) throws Exception {
    URI uri = this.getClass().getResource("/readme.md").toURI();
    if (isRunningInJar()) {
      Map<String, String> env = new HashMap<>();
      String[] array = uri.toString().split("!");
      fs = FileSystems.newFileSystem(URI.create(array[0]), env);
      path = fs.getPath(array[1]);
    } else {
      path = Paths.get(uri);
    }
  }

  @Override
  public void onStop(StopEvent event) throws Exception {
    if (fs != null) {
      fs.close();
    }
  }

  @Override
  public void handle(Context ctx) throws Exception {
    ctx.getResponse().sendFile(path);
  }

  private static boolean isRunningInJar() {
    File runningFile = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    return runningFile.isFile();
  }
}
