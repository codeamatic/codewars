package codeamatic.codewars.kata6;

import java.util.ArrayList;
import java.util.List;

/**
 * Kata 6
 *
 * http://www.codewars.com/kata/54da5a58ea159efa38000836/train/java
 */
public class FindOdd {

  public static int findIt(int[] A) {
    List<Integer> integerArrayList = new ArrayList<>();
    int odd = 0;

    for(int value : A) {
      // if we've already seen a number, save time and move on
      if(integerArrayList.contains(value)) {
        continue;
      }

      if((howMany(A, value) % 2) > 0) {
        odd = value;
      }
      // keep trying
      integerArrayList.add(value);
    }

    return odd;
  }

  /**
   * Find the number of times a single value appears.
   *
   * @param haystack the array to be searched
   * @param needle the value to search for in the array
   * @return number of times the value appears in the haystack
   */
  private static int howMany(int[] haystack, int needle) {
    int counter = 0;
    for(int v : haystack) {
      if(v == needle) {
        counter++;
      }
    }

    return counter;
  }
}