package duckutil.gatecontrol.blue;

import duckutil.ConfigJson;
import duckutil.PeriodicThread;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.TreeMap;
import duckutil.gatecontrol.RelayControl;
import duckutil.gatecontrol.SoundPlayer;
import duckutil.gatecontrol.Notification;;
import java.util.logging.Logger;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;


public class BluetoothScanner extends PeriodicThread
{
  private Process proc;
  private ConfigJson config;

  private TreeMap<String, String> accept_ids;
  private final RelayControl relay_control;
  private final SoundPlayer sound_player;
  private final Notification note;
  private static final Logger logger = Logger.getLogger("duck.gate.blue");

  public BluetoothScanner(ConfigJson config, RelayControl relay_control, SoundPlayer sound_player, Notification note)
  {
    super(60000);

    this.relay_control = relay_control;
    this.sound_player = sound_player;
    this.note = note;

    if (!config.getBoolean("bluetooth_enabled"))
    {
      return;
    }
    accept_ids=new TreeMap<>();
    config.require("bluetooth_list");
    
    JSONArray lst_arr = config.getAsArray("bluetooth_list");
    for(Object o : lst_arr)
    {
      JSONObject jo = (JSONObject) o;
      String mac = (String) jo.get("mac");
      String label = mac;

      if (jo.containsKey("label")) label = (String) jo.get("label");
      accept_ids.put(mac, label);
    }

    start();
  }

  public void runPass()
    throws Exception
  {
    if ((proc == null) || (!proc.isAlive()))
    {
      //ProcessBuilder pb = new ProcessBuilder("hcitool", "lescan");
      ProcessBuilder pb = new ProcessBuilder("stdbuf", "-o", "L", "hcitool", "lescan", "--duplicates");
      //ProcessBuilder pb = new ProcessBuilder("stdbuf", "-o", "L", "hcitool", "lescan");
      pb.redirectErrorStream(true);
      proc = pb.start();
    }
    logger.info("Starting bluetooth scanner");

    //PrintStream cmd = new PrintStream(proc.getOutputStream());
    //proc.getOutputStream().close();

    Scanner scan = new Scanner(proc.getInputStream());

    while(scan.hasNextLine())
    {
      String line = scan.nextLine().trim();
      logger.info("bluescan: " + line);

      //if (line.contains("Device"))
      {
        String[] s=line.split(" ");
        String device=s[0];
        Integer power = null;
        detectedDevice(device, power);
      }
    }
  }

  private void detectedDevice(String id, Integer power)
  {
   // System.out.println("ID: " + id + " - " + power);
    if (accept_ids.containsKey(id))
    {
      String label = accept_ids.get(id);
      if (relay_control.connectRelay("blue_" + label, 40000L))
      {
        sound_player.playSuccess();
        note.sendNotification("open blue " + label);
      }

    }

  }

}
