package duckutil.gatecontrol.matrix;

import duckutil.gatecontrol.gpio.GPIO;
import duckutil.gatecontrol.gpio.GPIOPin;

import java.util.ArrayList;

public class MatrixScan
{
  ArrayList<GPIOPin> cols;
  ArrayList<GPIOPin> rows;
  
  public MatrixScan()
  {
    cols = new ArrayList<>();
    rows = new ArrayList<>();

    
    cols.add(GPIO.openPin(17)); // C1
    cols.add(GPIO.openPin(22)); // C2
    cols.add(GPIO.openPin(25)); // C3

    rows.add(GPIO.openPin(27)); // r1
    rows.add(GPIO.openPin(16)); // r2
    rows.add(GPIO.openPin(24)); // r3
    rows.add(GPIO.openPin(23)); // r4

    for(GPIOPin pin : cols)
    {
      pin.setDirectionOut();
      pin.setLow();
    }
    for(GPIOPin pin : rows)
    {
      //pin.setDirectionOut();
      //pin.setLow();

      pin.setDirectionIn();
    }

  }
  public String scan()
  {
    String f = "";
    for(int c_idx = 0; c_idx<cols.size(); c_idx++)
    {
      GPIOPin c = cols.get(c_idx);
      c.setHigh();
      int r_idx=0;
      for(GPIOPin r : rows)
      {
        if (r.getValue())
        {
          f=c_idx +"," + r_idx;
        }
        r_idx++;
      }
      c.setLow();

    }
    return f;

  }
}
