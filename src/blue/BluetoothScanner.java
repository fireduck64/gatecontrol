package duckutil.gatecontrol.blue;

import duckutil.Config;
import duckutil.PeriodicThread;
import java.io.PrintStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.TreeSet;
import duckutil.gatecontrol.RelayControl;
import duckutil.gatecontrol.SoundPlayer;
import duckutil.gatecontrol.Notification;;
import java.util.logging.Logger;

public class BluetoothScanner extends PeriodicThread
{
  private Process proc;
  private Config config;

  private TreeSet<String> accept_ids;
  private final RelayControl relay_control;
  private final SoundPlayer sound_player;
  private final Notification note;
  private static final Logger logger = Logger.getLogger("duck.gate.blue");

  public BluetoothScanner(Config config, RelayControl relay_control, SoundPlayer sound_player, Notification note)
  {
    super(60000);

    this.relay_control = relay_control;
    this.sound_player = sound_player;
    this.note = note;

    if (!config.getBoolean("bluetooth_enabled"))
    {
      return;
    }
    accept_ids=new TreeSet<>();
    config.require("bluetooth_list");
    for(String lst : config.getList("bluetooth_list"))
    {
      accept_ids.add( lst.toUpperCase() );
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
    if (accept_ids.contains(id))
    {
      if (relay_control.connectRelay("blue_" + id, 40000L))
      {
        sound_player.playSuccess();
        note.sendNotification("open blue " + id);
      }

    }

  }

}
