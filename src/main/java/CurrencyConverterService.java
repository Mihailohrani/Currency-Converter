/**
 * Service class responsible for currency conversion logic.
 * This class retrieves exchange rates from the API and performs currency conversions.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.02.13
 */
public class CurrencyConverterService {

  // Prevents instantiation.
  private CurrencyConverterService() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Converts an amount from one currency to another.
   *
   * @param amount The amount of money to convert.
   * @param fromCurrency The base currency code.
   * @param toCurrency The target currency code.
   * @return The converted amount, or -1 if an error occurs.
   */
  public static double convert(double amount, String fromCurrency, String toCurrency) {
    double exchangeRate = ExchangeRateFetcher.getExchangeRate(fromCurrency, toCurrency);

    if (exchangeRate == -1) {
      OutputHandler.logError("Failed to fetch exchange rate.");
      return -1;
    }

    double convertedAmount = amount * exchangeRate;
    OutputHandler.logInfo("Converted " + amount + " " + fromCurrency + " to " + convertedAmount + " " + toCurrency);
    return convertedAmount;
  }
}
