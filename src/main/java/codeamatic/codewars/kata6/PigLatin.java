package codeamatic.codewars.kata6;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Kata 6
 *
 * http://www.codewars.com/kata/558878ab7591c911a4000007/train/java
 */
public class PigLatin {

  private final Pattern notAToZ = Pattern.compile("([^a-zA-Z])");
  private final Pattern consonants  = Pattern.compile("^([^a|e|i|o|u]+)");

  public String translate(String str){
    Matcher m;
    str = str.trim().toLowerCase();

    // remove non alphas
    if((m = notAToZ.matcher(str)).find()) {
      return null;
    } else if((m = consonants.matcher(str)).find()) {
      return str.replace(m.group(0), "") + m.group(0) + "ay";
    } else {
      return str + "way";
    }
  }
}
