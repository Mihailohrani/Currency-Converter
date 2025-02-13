import javax.swing.SwingUtilities;

public class CurrencyConverterApp {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      CurrencyConverterGUI gui = new CurrencyConverterGUI();
      gui.setVisible(true);
    });
  }
}
