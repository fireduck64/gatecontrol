package duckutil.gatecontrol;

import duckutil.gatecontrol.gpio.GPIO;
import duckutil.gatecontrol.gpio.GPIOPin;

public class GateControl
{
  public static void main(String args[]) throws Exception
  {
    GPIOPin pin = GPIO.openPin(5);

    System.out.println("value: " + pin.getValue());
    pin.setDirectionOut();
    System.out.println("value: " + pin.getValue());
    pin.setHigh();
    System.out.println("value: " + pin.getValue());
  }
}
