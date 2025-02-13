/**
 * A class to represent a currency.
 * Each currency has a name, a code, and a symbol.
 *
 * @author Mihailo Hranisavljevic
 * @version 2025.02.13
 */

public record Currency(String name, String code, String symbol) {

  /**
   * Returns a string representation of the currency.
   *
   * @return A string with currency details.
   */
  @Override
  public String toString() {
    return name + " (" + code + ") " + symbol;
  }
}
