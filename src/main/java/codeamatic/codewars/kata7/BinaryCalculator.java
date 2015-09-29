package codeamatic.codewars.kata7;

/**
 * Kata7
 *
 * http://www.codewars.com/kata/546ba103f0cf8f7982000df4/train
 */
public class BinaryCalculator {

  public static String calculate(final String n1, final String n2, final String o) {
    int num1 = Integer.parseInt(n1, 2);
    int num2 = Integer.parseInt(n2, 2);

    if(! o.equals("multiply")) {
      int as = (o.equals("add")) ? num1 + num2 : num1 - num2;
      return Integer.toBinaryString(as);
    } else {
      return Integer.toBinaryString(num1 * num2);
    }
  }
}