package duckutil.gatecontrol.gpio;

public class GPIO
{

  public static GPIOPin openPin(int pin)
    throws Exception
  {
    return new GPIOPin(pin);
    
  }

}
