package duckutil.gatecontrol;

import duckutil.gatecontrol.matrix.Keypad;

import duckutil.PeriodicThread;
import java.util.Collection;
import java.util.TreeSet;
import java.util.TreeMap;
import java.io.InputStream;
import java.util.logging.Logger;


/**
 * Uses a set of access codes to allow the gate to open.
 */ 
public class AccessCode extends PeriodicThread
{
  private final Keypad keypad;
  private final RelayControl relay_control;
  private final TreeSet<String> access_code_set;
  private final SoundPlayer sound_player;

  private TreeMap<Long, String> recent_presses;

  private final long max_press_age_ns = 30L*1000L*1000L*1000L;
  private static final Logger logger = Logger.getLogger("duck.gate.code");

  public AccessCode(Keypad keypad, RelayControl relay_control, Collection<String> access_code_list, SoundPlayer sound_player)
  {
    super(1);
    this.keypad = keypad;
    this.relay_control = relay_control;
    this.sound_player = sound_player;

    this.access_code_set = new TreeSet<>();
    access_code_set.addAll(access_code_list);

    recent_presses = new TreeMap<>();


  }


  public void runPass() throws Exception
  {
    InputStream in = keypad.getStream();

    byte[] buff = new byte[32];
    int r = in.read(buff);

    if (r > 0)
    {
      String s=new String(buff,0,r);
      recent_presses.put(System.nanoTime(), s);
    }
    else
    {
      logger.warning("No input on keypad read");
    }

    long old = System.nanoTime() - max_press_age_ns;

    while((recent_presses.size() > 0) && (recent_presses.firstKey() < old))
    {
      recent_presses.pollFirstEntry();
    }
    if (recent_presses.size() > 0)
    {
      StringBuilder sb = new StringBuilder();
      for(String s : recent_presses.values())
      {
        sb.append(s);
      }
      String entered_string = sb.toString();
      logger.info("Entered string: " + entered_string);
      for(String code : access_code_set)
      {
        if (entered_string.endsWith(code))
        {
          relay_control.connectRelay("access_code_" + code, 5000L);
          sound_player.playSuccess();
        }
      }
    }

  }

}
