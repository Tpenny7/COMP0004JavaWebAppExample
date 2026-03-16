package uk.ac.ucl.main;

import java.io.IOException;
// File utilities used to validate that directories exist.
import java.nio.file.Files;
// Represents filesystem paths in a platform-independent way.
import java.nio.file.Path;
// Builds Path objects from Strings.
import java.nio.file.Paths;
// Java's built-in logging API.
import java.util.logging.*;

// Tomcat API: represents the web application context (similar to a deployed webapp in Tomcat).
import org.apache.catalina.Context;
// Tomcat API: abstraction for where Tomcat loads resources (classes, static files) from.
import org.apache.catalina.WebResourceRoot;
// Tomcat API: the main entry point for embedded Tomcat.
import org.apache.catalina.startup.Tomcat;
// Tomcat API: resource set backed by a directory on disk.
import org.apache.catalina.webresources.DirResourceSet;
// Tomcat API: default WebResourceRoot implementation.
import org.apache.catalina.webresources.StandardRoot;


public class Main
{
  private static final int DEFAULT_PORT = 8080;
  private static final String DEFAULT_WEBAPP_DIR = "src/main/webapp/";
  private static final String DEFAULT_TARGET_CLASSES = "target/classes";
  private static final String WEB_INF_CLASSES = "/WEB-INF/classes";
  private static final String LOGFILE = "logfile.txt";

  private static int getPort() {
    String port = System.getProperty("SERVER_PORT", System.getenv("SERVER_PORT"));
    return (port != null) ? Integer.parseInt(port) : DEFAULT_PORT;
  }

  private static String getWebappDir() {
    String dir = System.getProperty("WEBAPP_DIR", System.getenv("WEBAPP_DIR"));
    return (dir != null) ? dir : DEFAULT_WEBAPP_DIR;
  }

  private static String getClassesDir() {
    String dir = System.getProperty("CLASSES_DIR", System.getenv("CLASSES_DIR"));
    return (dir != null) ? dir : DEFAULT_TARGET_CLASSES;
  }

  public static Thread addShutdown(final Tomcat tomcat, final Logger logger)
  {
    Thread shutdownHook = new Thread(() -> {
      try
      {
        if (tomcat != null)
        {
          tomcat.stop();
          tomcat.destroy();
          logger.info("Tomcat has shut down normally.");
        }
      } catch (Exception e)
      {
        logger.log(Level.SEVERE, "Error shutting down Tomcat", e);
      }
    });
    Runtime.getRuntime().addShutdownHook(shutdownHook);
    return shutdownHook;
  }

  private static Logger initialiseLogger()
  {
    Logger logger = Logger.getLogger(Main.class.getName());
    logger.setUseParentHandlers(false);

    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.INFO);
    logger.addHandler(consoleHandler);

    try {
      FileHandler fileHandler = new FileHandler(LOGFILE);
      fileHandler.setFormatter(new SimpleFormatter());
      fileHandler.setLevel(Level.INFO);
      logger.addHandler(fileHandler);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to create log file", e);
    }

    logger.setLevel(Level.INFO);
    return logger;
  }

  private static Context getContext(Path webappDirectory, Tomcat tomcat)
  {
    if (!Files.exists(webappDirectory) || !Files.isDirectory(webappDirectory))
    {
      throw new IllegalArgumentException("Webapp directory does not exist: " + webappDirectory);
    }
    return tomcat.addWebapp("/", webappDirectory.toAbsolutePath().toString());
  }

  private static void setResources(Context context, Path targetClassesDirectory)
  {
    WebResourceRoot resources = new StandardRoot(context);
    resources.addPreResources(new DirResourceSet(resources, WEB_INF_CLASSES,
      targetClassesDirectory.toAbsolutePath().toString(), "/"));
    context.setResources(resources);
  }

  public static void main(String[] args)
  {
    // Set up logging first so subsequent steps can report progress and errors.
    final Logger logger = initialiseLogger();
    // Read configuration values (with defaults).
    final int port = getPort();
    // Resolve directories into Path objects.
    final Path webappDirectory = Paths.get(getWebappDir());
    final Path targetClassesDirectory = Paths.get(getClassesDir());
    // Create the embedded Tomcat instance.
    final Tomcat tomcat = new Tomcat();

    try
    {
      tomcat.setPort(port);
      tomcat.getConnector();
      addShutdown(tomcat, logger);

      Context context = getContext(webappDirectory, tomcat);
      setResources(context, targetClassesDirectory);

      tomcat.start();
      logger.info("Server started successfully on port " + port);
      tomcat.getServer().await();
    }
    catch (IllegalArgumentException e)
    {
      logger.log(Level.SEVERE, "Configuration error", e);
    }
    catch (Exception e)
    {
      logger.log(Level.SEVERE, "Error occurred while starting the server", e);
    }
  }
}