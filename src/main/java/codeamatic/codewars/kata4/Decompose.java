package codeamatic.codewars.kata4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Decompose {

  public String decompose(long n) {
    long squared = (n * n);
    long result = 0;
    List<Long> longList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();
    int j = 1;
    boolean complete = false;

    while (! complete) {

      longList = new ArrayList<>();
      result = 0;

      for (long i = n - j; i > 0; i--) {
        longList.add(i);

        for (long q = i - 1; q > 0; q--) {
          result = calculate(longList) + q* q;
          if (result == squared) {
            //longList.add(1L);
            break;
          } else if (result < squared) {
            longList.add(q);
          } /*else if(result > squared) {
            contin;
          } */
        }
      }




      if (longList.isEmpty()) {
        complete = true;
      } else if(result == squared){
        Collections.sort(longList);
        String str = "";
        for (Long s : longList) {
          str += s + " ";
        }

        stringList.add(str.trim());
      }

      j++;

    }

    return "";
  }

  private long calculate(List<Long> longList) {
    long answer = 0;

    for(long term : longList) {
      answer += term * term;
    }

    return answer;
  }
}
