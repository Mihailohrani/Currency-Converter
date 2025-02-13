import javax.swing.SwingUtilities;

/**
 * The main class for the application.
 * Creates a new CurrencyConverterGUI object in order to run the app.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.02.13
 */

public class CurrencyConverterApp {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      CurrencyConverterGUI gui = new CurrencyConverterGUI();
      gui.setVisible(true);
    });
  }
}
