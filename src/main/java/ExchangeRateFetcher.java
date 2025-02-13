import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import org.json.JSONObject;

/**
 * Fetches exchange rates from ExchangeRate-API.
 * This utility class retrieves conversion rates between currencies.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.02.13
 */
public class ExchangeRateFetcher {
  private static final String API_KEY = System.getenv("API_KEY");
  private static final String API_URL = "https://v6.exchangerate-api.com/v6/";
  private static final int TIMEOUT = 5000;

  private ExchangeRateFetcher() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
  }

  /**
   * Retrieves the exchange rate between two currencies.
   *
   * @param fromCurrency The base currency (e.g., "NOK").
   * @param toCurrency   The target currency (e.g., "EUR").
   * @return The exchange rate or -1 if an error occurs.
   */
  public static double getExchangeRate(String fromCurrency, String toCurrency) {
    if (API_KEY == null || API_KEY.isEmpty()) {
      OutputHandler.logError("API key is missing.");
      return -1;
    }

    String urlStr = API_URL + API_KEY + "/latest/" + fromCurrency;

    try {
      HttpURLConnection conn = (HttpURLConnection) new URI(urlStr).toURL().openConnection();
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(TIMEOUT);
      conn.setReadTimeout(TIMEOUT);

      int responseCode = conn.getResponseCode();
      if (responseCode == 429) {
        OutputHandler.logWarning("API rate limit exceeded.");
        return -1;
      } else if (responseCode != 200) {
        OutputHandler.logError("API request failed with response code: " + responseCode);
        return -1;
      }

      JSONObject jsonResponse = readJsonResponse(conn);
      if (jsonResponse == null) return -1;

      if (!jsonResponse.getJSONObject("conversion_rates").has(toCurrency)) {
        OutputHandler.logError("Invalid currency code: " + toCurrency);
        return -1;
      }

      double exchangeRate = jsonResponse.getJSONObject("conversion_rates").getDouble(toCurrency);
      OutputHandler.logInfo("Exchange rate: " + fromCurrency + " → " + toCurrency + " = " + exchangeRate);
      return exchangeRate;

    } catch (java.net.UnknownHostException e) {
      OutputHandler.logError("Network error: Unable to reach the API.");
    } catch (java.net.SocketTimeoutException e) {
      OutputHandler.logError("Request timed out.");
    } catch (Exception e) {
      OutputHandler.logError("Unexpected error: " + e.getMessage());
    }
    return -1;
  }

  /**
   * Reads and parses the JSON response from an API request.
   *
   * @param conn The HttpURLConnection instance.
   * @return A JSONObject containing the API response, or null if an error occurs.
   */
  private static JSONObject readJsonResponse(HttpURLConnection conn) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      JSONObject jsonResponse = new JSONObject(response.toString());

      if ("error".equals(jsonResponse.optString("result"))) {
        OutputHandler.logError("API error: " + jsonResponse.optString("error-type", "Unknown error"));
        return null;
      }

      return jsonResponse;
    } catch (Exception e) {
      OutputHandler.logError("Failed to read API response: " + e.getMessage());
      return null;
    }
  }
}
