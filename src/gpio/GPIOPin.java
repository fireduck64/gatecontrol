package duckutil.gatecontrol.gpio;

import java.io.File;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class GPIOPin
{
  private final int pin;
  private final File base_path; 
  private final File value_file;

  public GPIOPin(int pin)
  {
    this.pin = pin;
    base_path = new File("/sys/class/gpio/gpio" + pin);
    value_file = new File(base_path, "value");
    open();
  }
  public int getPin(){return pin;}

  private void open()
  {
    if (base_path.exists()) return;
    write(new File("/sys/class/gpio/export"),"" + pin);
    try
    {
      Thread.sleep(500);
    }
    catch(Throwable t)
    {
      
      throw new RuntimeException(t);
    }
  } 

  public void setDirectionOut()
  {
    File direction_file = new File(base_path, "direction");
    write(direction_file, "out");
  }

  public void setDirectionIn()
  {
    File direction_file = new File(base_path, "direction");
    write(direction_file, "in");
  }

  private void write(File f, String n)
  {
    try
    {
      PrintStream out = new PrintStream(new FileOutputStream(f));
      out.println(n);
      out.close();
    }
    catch(java.io.IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public void setHigh()
  {
    write(value_file, "1");
  }
  public void setLow()
  {
    write(value_file, "0");
  }

  /**
   * false = low
   * true = high
   */
  public boolean getValue()
  {
    try
    {
      FileInputStream in = new FileInputStream(value_file);
      byte[] b = new byte[64];
      in.read(b);
      in.close();
      
      if (b[0]=='0') return false;
      if (b[0]=='1') return true;
    }
    catch(java.io.IOException e)
    {
      throw new RuntimeException(e);
    }

    throw new RuntimeException("value nonsense");

  }
  
}
