import java.util.logging.Logger;

/**
 * Utility class for handling application output.
 * <p>
 * This class provides methods for logging and printing messages to the console.
 * </p>
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.02.13
 */
public class OutputHandler {
  private static final Logger LOGGER = Logger.getLogger(OutputHandler.class.getName());

  // Prevents instantiation.
  private OutputHandler() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Logs an error message.
   *
   * @param message The error message to log.
   */
  public static void logError(String message) {
    LOGGER.severe(message);
  }

  /**
   * Logs a warning message.
   *
   * @param message The warning message to log.
   */
  public static void logWarning(String message) {
    LOGGER.warning(message);
  }

  /**
   * Logs an informational message.
   *
   * @param message The info message to log.
   */
  public static void logInfo(String message) {
    LOGGER.info(message);
  }

  /**
   * Prints a message to the console.
   *
   * @param message The message to print.
   */
  public static void print(String message) {
    System.out.println(message);
  }
}
