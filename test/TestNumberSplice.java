
import org.junit.Test;
import org.junit.Assert;

import duckutil.gatecontrol.AccessCode;

public class TestNumberSplice
{

  @Test
  public void testPositive()
  {
    Assert.assertEquals(7, AccessCode.numberSplice("9#7#"));
    Assert.assertEquals(7, AccessCode.numberSplice("#7#"));
    Assert.assertEquals(70, AccessCode.numberSplice("#33222#70#"));
    Assert.assertEquals(170, AccessCode.numberSplice("#33222#170#"));

  }

 @Test
  public void testNegative()
  {
    Assert.assertEquals(-1, AccessCode.numberSplice(""));
    Assert.assertEquals(-1, AccessCode.numberSplice("1111"));
    Assert.assertEquals(-1, AccessCode.numberSplice("1111#"));
    Assert.assertEquals(-1, AccessCode.numberSplice("#"));
    Assert.assertEquals(-1, AccessCode.numberSplice("##"));
    Assert.assertEquals(-1, AccessCode.numberSplice("#7##"));
    Assert.assertEquals(-1, AccessCode.numberSplice("#7#MEOW#"));

  }



}
