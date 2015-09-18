package codeamatic.codewars.kata2;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kata 2
 *
 * http://www.codewars.com/kata/52a78825cdfc2cfc87000005/train/java
 */
public class MathEvaluator {

  private static final String ONLY_NUM_REGEX = "(\\d+\\.\\d+|-\\d+|\\d+)";

  private static final String PAREN_REGEX = "\\((.+?)\\)";

  private static final String ACTION_REGEX = "(\\+|-|\\/|\\*)";

  private static final String CLEAN_REGEX = "(\\+|-|\\/|\\*)-((\\(|\\d\\()(.+?)\\))";

  public double calculate(String expression) {
    expression = preprocessExpression(expression);

    // Get count of opening parentheses.  Kata said that all expressions will be valid
    // so we will assume all opening paren has closing paren
    Matcher m = Pattern.compile(PAREN_REGEX).matcher(expression);

    while (m.find()) {
      String grpContents = m.group(1);
      // If there is a parentheses inside of the contents, it means there
      // was a nested parentheses group.  We will handle it by breaking down
      // the parentheses individually outside of this while loop.
      if (grpContents.contains("(")) {
        continue;
      }
      String parseExpression = parseExpression(grpContents);
      expression = expression.replace("(" + grpContents + ")", parseExpression);
    }

    while (expression.contains("(")) {
      // recursively break down expression until no parentheses exist
      int ixLastOpenParen = expression.lastIndexOf("(");
      int ixFirstCloseParen = ixLastOpenParen + expression.substring(ixLastOpenParen).indexOf(")");

      String subExpression = expression.substring(ixLastOpenParen + 1, ixFirstCloseParen);
      String parsedExpression = parseExpression(subExpression);

      String parenSub = expression.substring(ixLastOpenParen, ixFirstCloseParen + 1);
      // reset expression with replaced parenthese subs
      expression = expression.replace(parenSub, parsedExpression);
    }

    Double finalResult = Double.parseDouble(parseExpression(expression));

    return Double.parseDouble(new DecimalFormat("#.00").format(finalResult));
  }

  /**
   * Recursively parses expressions down into their resulting answer.
   *
   * @return string result
   */
  public String parseExpression(String subExpression) {
    // check for only numbers (neg/pos)
    if (subExpression.matches(ONLY_NUM_REGEX)) {
      return subExpression;
    }

    double result = 0;
    StringTokenizer multiTokenizer = new StringTokenizer(subExpression, "*+-/", true);
    boolean answered = false;
    String stringToParse = "";
    List<String> stringList = new ArrayList<>();

    while (multiTokenizer.hasMoreTokens()) {
      stringList.add(multiTokenizer.nextToken());
    }

    stringList = sanitizeTokens(stringList);

    // determine order of operations, making multiplication and division calculations first
    if ((stringList.contains("*") || stringList.contains("/")) && stringList.size() > 3) {
      int ixDiv = stringList.indexOf("/");
      int ixMult = stringList.indexOf("*");

      // determine the string to be calculated
      if ((ixDiv < ixMult && ixDiv >= 0) || (ixDiv > 0 && ixMult < 0)) {
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

    return (answered && stringList.size() <= 3) ? Double.toString(result)
                                                : parseExpression(subExpression);
  }

  /**
   * Properly assigns negated values to a single token as opposed to having the - as one token and
   * the associated number as the next token.
   *
   * @param rawList the raw unsanitized list of tokens
   * @return sanitized ArrayList of string
   */
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
   * Breaks the expression down into a clean formatted mathematical expression.  This process will
   * attempt to add and remove characters and variables where necessary.
   *
   * <p>This method is most important when dealing with expressions that have a negated parentheses
   * expression or a parentheses expression that  gets multiplied by a number on the left outside of
   * it.  Example: 1 + -(3 +5) * 3(6 + 8)
   *
   * <p>NOTE: This does not check for numbers on the right outside of the parentheses (3 + 5)6.
   * Assumes those will not be present.
   *
   * @return a cleaner expression
   */
  public String preprocessExpression(String expression) {
    // remove all spaces
    expression = expression.replace(" ", "");

    Matcher m = Pattern.compile(CLEAN_REGEX).matcher(expression);

    // Build expression up into a more formal version
    while (m.find()) {
      String leftSideGrp = m.group(3);
      String
          leftSideExpression =
          (leftSideGrp.length() > 1) ? "-" + leftSideGrp.substring(0, leftSideGrp.length() - 1)
                                     : "-1";
      String insideGrp = m.group(4);

      String insideParsedExpression = parseExpression(insideGrp);
      String parsedExpression = parseExpression(leftSideExpression + "*" + insideParsedExpression);

      expression = expression.replace(m.group().substring(1), parsedExpression);
    }

    return expression;
  }

  /**
   * Determines the necessary function to be used to process the calculation and then calculates
   * accordingly.
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