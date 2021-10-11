package duckutil.gatecontrol;

import duckutil.gatecontrol.gpio.GPIO;
import duckutil.gatecontrol.gpio.GPIOPin;
import duckutil.gatecontrol.matrix.MatrixScan;

public class GateControl
{
  public static void main(String args[]) throws Exception
  {
    MatrixScan matrix = new MatrixScan();

    while(true)
    {
      long t1 = System.nanoTime();
      String f = matrix.scan();
      long t2 = System.nanoTime();
      long d = t2 - t1;
      d = d / 1000000;
      System.out.println("Scan took: " + d + " ms " + f);

      Thread.sleep(1);

    }

  }
}
