import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

/**
 * Utility class for fetching exchange rates from the ExchangeRate-API.
 * Used to get the exchange rate from one currency to another.
 *
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.02.13
 */
public class ExchangeRateFetcher {
  private static final String API_KEY = System.getenv("API_KEY");
  private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

  /**
   * Private constructor to prevent instantiation of this utility class.
   */
  private ExchangeRateFetcher() {
    throw new UnsupportedOperationException("Utility class");
  }

  /**
   * Fetches the exchange rate from one currency to another using the ExchangeRate-API.
   *
   * @param fromCurrency The base currency code (e.g., "NOK").
   * @param toCurrency   The target currency code (e.g., "EUR").
   * @return The exchange rate as a double, or -1 if an error occurs.
   */
  public static double getExchangeRate(String fromCurrency, String toCurrency) {
    try {
      if (API_KEY == null || API_KEY.isEmpty()) {
        OutputHandler.logError("API key is missing.");
        return -1;
      }

      /*
      * Constructs the URL for the API request.
      * Makes the connection to the API and reads the response.
       */
      String urlStr = API_URL + API_KEY + "/latest/" + fromCurrency;
      URL url = new URI(urlStr).toURL();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      conn.setConnectTimeout(5000);
      conn.setReadTimeout(5000);

      if (conn.getResponseCode() == 429) {
        OutputHandler.logWarning("Rate limit exceeded! Too many API requests.");
        return -1;
      }

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();
      JSONObject jsonResponse = new JSONObject(response.toString());

      // Used to handle API errors.
      if (jsonResponse.has("result") && jsonResponse.getString("result").equals("error")) {
        OutputHandler.logError("API Error: " + jsonResponse.getString("error-type"));
        return -1;
      }


       //Checks to see if the currency code is valid.

      if (!jsonResponse.getJSONObject("conversion_rates").has(toCurrency)) {
        OutputHandler.logError("Invalid currency code: " + toCurrency);
        return -1;
      }

      double exchangeRate = jsonResponse.getJSONObject("conversion_rates").getDouble(toCurrency);
      OutputHandler.logInfo("Successfully fetched exchange rate: " + fromCurrency + " to " + toCurrency + " = " + exchangeRate);
      return exchangeRate;

    } catch (java.net.UnknownHostException e) {
      OutputHandler.logError("Network error: Unable to reach the API. Check your internet connection.");
      return -1;
    } catch (java.net.SocketTimeoutException e) {
      OutputHandler.logError("Request timed out. The API server is taking too long to respond.");
      return -1;
    } catch (Exception e) {
      OutputHandler.logError("An unexpected error occurred: " + e.getMessage());
      return -1;
    }
  }
}
