package duckutil.gatecontrol;

import duckutil.PeriodicThread;

import duckutil.gatecontrol.gpio.GPIOPin;
import duckutil.gatecontrol.gpio.GPIO;
import java.util.TreeMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Allows multiple things to activate the relay.
 * If anything is asking for the relay to be active, it will be active.
 */
public class RelayControl extends PeriodicThread
{
  private GPIOPin pin;

  // Maps identifier to expiration time
  private TreeMap<String, Long> holds;

  boolean pin_active;

  public RelayControl(int pin_number)
  {
    super(100);

    holds =new TreeMap<String, Long>();

    pin = GPIO.openPin(pin_number);

    pin.setDirectionOut();
    pin.setLow();
    pin_active=false;
  }

 
  /**
   * If a caller wants to keep the relay open they either need
   * to either set a long time or call this again to reset the hold
   * open time.
   * @returns true if the relay wasn't already connected for this identifier
   */
  public boolean connectRelay(String identifier, long ms)
  {
    System.out.println("Connect relay: " + identifier + " for " + ms);
    long expire = System.currentTimeMillis() + ms;
    boolean existing = false;
    synchronized(holds)
    {
      if (holds.containsKey(identifier)) existing=true;

      holds.put(identifier, expire);
    }
    wake();

    return !existing;

  }
  public Map<String,Long> getHolds()
  {
    TreeMap<String, Long> hold_copy=new TreeMap<>();

    synchronized(holds)
    {
      hold_copy.putAll(holds);
    }

    return hold_copy;
  }
  public boolean isHoldOpen(String id)
  {
    synchronized(holds)
    {
      return holds.containsKey(id);
    }
  }

  public void close()
  {
    synchronized(holds)
    {
      holds.clear();
    }
    wake();
  }

  public void runPass()
  {
    int open_holds = 0;
    // Maintain holds
    synchronized(holds)
    {
      TreeSet<String> to_remove=new TreeSet<>();
      for(Map.Entry<String, Long> me : holds.entrySet())
      {
        if (me.getValue() < System.currentTimeMillis())
        {
          to_remove.add(me.getKey());
        }
      }
      for(String id : to_remove)
      {
        holds.remove(id);
      }
      open_holds = holds.size();
    }

    if (open_holds > 0)
    {
      if (!pin_active)
      {
        pin.setHigh();
        pin_active=true;
        System.out.println("Activating relay");
      }
    }
    else
    {
      if (pin_active)
      {
        pin.setLow();
        pin_active=false;
        System.out.println("Deactivating relay");
      }
    }

  }
}
