package codeamatic.codewars.kata6;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PigLatinTest {

  private PigLatin p;

  @Before
  public void setUp() throws Exception {
    p = new PigLatin();

  }

  @Test
  public void testMap() {
    assertEquals("apmay", p.translate("map"));
  }

  @Test
  public void testegg() {
    assertEquals("eggway", p.translate("egg"));
  }

  @Test
  public void testspaghetti() {
    assertEquals("aghettispay", p.translate("spaghetti"));
  }
}