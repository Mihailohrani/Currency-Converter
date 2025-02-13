import javax.swing.*;
import java.awt.*;

/**
 * GUI for the Currency Converter application.
 * This class provides a simple GUI to convert currencies.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.02.13
 */
public class CurrencyConverterGUI extends JFrame {

  private final JTextField amountField;
  private final JComboBox<String> fromCurrency;
  private final JComboBox<String> toCurrency;
  private final JLabel resultLabel;

  public CurrencyConverterGUI() {
    super("Currency Converter");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(400, 200);
    setLayout(new GridLayout(4, 2));
    setLocationRelativeTo(null);

    JLabel amountLabel = new JLabel("Amount:");
    amountField = new JTextField();

    JLabel fromLabel = new JLabel("From:");
    fromCurrency = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "NOK", "RSD"});

    JLabel toLabel = new JLabel("To:");
    toCurrency = new JComboBox<>(new String[]{"USD", "EUR", "GBP", "NOK", "RSD"});

    JButton convertButton = new JButton("Convert");
    resultLabel = new JLabel("Converted Amount: ");

    convertButton.addActionListener(_ -> {
      double amount;
      try {
        amount = Double.parseDouble(amountField.getText());
      } catch (NumberFormatException ex) {
        OutputHandler.logError("Invalid number format.");
        resultLabel.setText("Invalid amount!");
        return;
      }

      String from = (String) fromCurrency.getSelectedItem();
      String to = (String) toCurrency.getSelectedItem();

      double convertedAmount = CurrencyConverterService.convert(amount, from, to);
      if (convertedAmount == -1) {
        resultLabel.setText("Conversion failed");
      } else {
        resultLabel.setText("Converted Amount: " + convertedAmount + " " + to);
      }
    });

    add(amountLabel);
    add(amountField);
    add(fromLabel);
    add(fromCurrency);
    add(toLabel);
    add(toCurrency);
    add(convertButton);
    add(resultLabel);
  }
}
