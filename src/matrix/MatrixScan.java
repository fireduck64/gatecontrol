package duckutil.gatecontrol.matrix;

import duckutil.gatecontrol.gpio.GPIO;
import duckutil.gatecontrol.gpio.GPIOPin;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Random;

public class MatrixScan
{
  ArrayList<GPIOPin> cols;
  ArrayList<GPIOPin> rows;

  Map<String, String> keymap;
  
  public MatrixScan()
  {
    cols = new ArrayList<>();
    rows = new ArrayList<>();

    keymap = new TreeMap<String, String>();
    keymap.put("0,0", "1");
    keymap.put("1,0", "2");
    keymap.put("2,0", "3");
    keymap.put("0,1", "4");
    keymap.put("1,1", "5");
    keymap.put("2,1", "6");
    keymap.put("0,2", "7");
    keymap.put("1,2", "8");
    keymap.put("2,2", "9");
    keymap.put("0,3", "*");
    keymap.put("1,3", "0");
    keymap.put("2,3", "#");
    
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
    String f = null;
    Random rnd = new Random();
    for(int c_idx = 0; c_idx<cols.size(); c_idx++)
    {
      GPIOPin c = cols.get(c_idx);
      c.setHigh();
      int r_idx=0;
      for(GPIOPin r : rows)
      {
        if ((r.getValue()) || (rnd.nextDouble() < 0.001))
        {
          f=c_idx +"," + r_idx;
        }
        r_idx++;
      }
      c.setLow();
    }
    if (f != null)
    {
      f = keymap.get(f);
    }
    return f;

  }
}
