package codeamatic.codewars.kata2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Kata 2
 *
 * http://www.codewars.com/kata/52a78825cdfc2cfc87000005/train/java
 */
public class MathEvaluator {
  private static final String ONLY_NUM_REGEX = "(-\\d*|\\d*)";
  private static final String ACTION_REGEX = "\\+|\\-|\\/|\\*";

  public double calculate(String expression) {
    // remove all spaces
    expression = expression.replace(" ", "");

    // check for only numbers (neg/pos)
    if(expression.matches(ONLY_NUM_REGEX)) {
      return Double.parseDouble(expression);
    }

    // Get count of opening parentheses.  Kata said that all expressions will be valid
    // so we will assume all opening paren has closing paren
    int parenCount = expression.length() - expression.replace("(", "").length();

    if (parenCount > 0) {
      // recursively break down expression until no parentheses exist
      for (int i = 0; i < parenCount; i++) {
        int ixLastOpenParen = expression.lastIndexOf("(");
        int ixFirstCloseParen = expression.indexOf(")");

        String subExpression = expression.substring(ixLastOpenParen + 1, ixFirstCloseParen);
        String parsedExpression = parseExpression(subExpression);

        String parenSub = expression.substring(ixLastOpenParen, ixFirstCloseParen + 1);
        // reset expression with replaced parenthese subs
        expression = expression.replace(parenSub,  parsedExpression);
      }
    }

    return Double.parseDouble(parseExpression(expression));
  }

  public String parseExpression(String subExpression) {
    double result = 0;
    StringTokenizer multiTokenizer = new StringTokenizer(subExpression, "*+-/", true);
    boolean answered = false;
    String stringToParse = "";
    List<String> stringList = new ArrayList<String>();

    while (multiTokenizer.hasMoreTokens()) {
      stringList.add(multiTokenizer.nextToken());
    }

    stringList = sanitizeTokens(stringList);

    if ((stringList.contains("*") || stringList.contains("/")) && stringList.size() > 3) {
      int ixDiv = stringList.indexOf("/");
      int ixMult = stringList.indexOf("*");

      if (ixDiv < ixMult && ixDiv >= 0) {
        stringToParse = stringList.get(ixDiv - 1) + "/" + stringList.get(ixDiv + 1);
      } else if (ixMult >= 0) {
        stringToParse = stringList.get(ixMult - 1) + "*" + stringList.get(ixMult + 1);
      }

      subExpression = subExpression.replace(stringToParse, parseExpression(stringToParse));
    } else {
      result = calculateTokens(stringList.get(0), stringList.get(2), stringList.get(1).charAt(0));
      answered = true;

      if (stringList.size() > 3) {
        stringToParse = stringList.get(0) + stringList.get(1) + stringList.get(2);
        subExpression = subExpression.replace(stringToParse, Double.toString(result));
      }
    }

    return (answered && stringList.size() <= 3) ? new DecimalFormat("#.00").format(result)
                         : parseExpression(subExpression);
  }

  public List<String> sanitizeTokens(List<String> rawList) {
    // Check for negative number at front of expression
    if (rawList.get(0).equals("-")) {
      rawList.remove(0);
      rawList.set(0, "-" + rawList.get(0));
    }

    for (int i = 0; i < rawList.size(); i++) {
      if (rawList.get(i).equals("-") && rawList.get(i - 1).matches(ACTION_REGEX)) {
        rawList.set(i + 1, "-" + rawList.get(i + 1));
        rawList.remove(i);
        // pre-increment i so that it catches up with the list size.
        i++;
      }
    }

    return rawList;
  }

  /**
   * Determines the necessary function to be used to process the calculation
   * and then calculates accordingly.
   *
   * @param param1 first parameter
   * @param param2 second parameter
   * @param action math action/function
   * @return the result of the math calculation
   */
  public double calculateTokens(String param1, String param2, char action) {
    double result = 0;

    switch (action) {
      case '+':
        result = Double.parseDouble(param1) + Double.parseDouble(param2);
        break;
      case '-':
        result = Double.parseDouble(param1) - Double.parseDouble(param2);
        break;
      case '/':
        result = Double.parseDouble(param1) / Double.parseDouble(param2);
        break;
      case '*':
        result = Double.parseDouble(param1) * Double.parseDouble(param2);
        break;
    }

    return result;
  }
}