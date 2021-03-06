package homewatch.constants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtils {
  private LoggerUtils() {
  }

  public static void logException(Exception e) {
    Logger.getGlobal().log(Level.SEVERE, "An exception was thrown", e);
  }

  public static void logInfo(String s) {
    Logger.getGlobal().info(s);
  }
}
