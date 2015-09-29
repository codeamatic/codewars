package codeamatic.codewars.kata4;

import static org.junit.Assert.*;
import org.junit.Test;

public class DecomposeTest {

  @Test
  public void test1() {
    Decompose d = new Decompose();
    long n = 625;
    assertEquals("1 2 3 7 9",  d.decompose(n));
  }
}
