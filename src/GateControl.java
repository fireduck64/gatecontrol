package duckutil.gatecontrol;

import duckutil.gatecontrol.gpio.GPIO;
import duckutil.gatecontrol.gpio.GPIOPin;
import duckutil.gatecontrol.matrix.MatrixScan;
import duckutil.gatecontrol.matrix.Keypad;
import duckutil.gatecontrol.blue.BluetoothScanner;
import duckutil.Config;
import duckutil.ConfigFile;

public class GateControl
{
  public static void main(String args[]) throws Exception
  {
    new GateControl(new ConfigFile(args[0]));

  }

  private final Config config;
  private final RelayControl relay_control;
  private final Keypad keypad;
  private final AccessCode access_code;
  private final SoundPlayer sound_player;
  private final RpcServer rpc_server;
  private final BluetoothScanner blue_scanner;
  private final Notification note;

  public GateControl(Config config)
    throws Exception
  {
    this.config = config;

    config.require("relay_gpio_pin");
    config.require("access_code_list");

    sound_player = new SoundPlayer();
    if (config.getBoolean("sound_enabled"))
    {
      sound_player.start();
    }
    note = new Notification(config);
    note.start();

    relay_control = new RelayControl( config.getInt("relay_gpio_pin"));
    relay_control.start();

    keypad = new Keypad(sound_player);
    keypad.start();

    access_code = new AccessCode(keypad, relay_control, config.getList("access_code_list"), sound_player, note);
    access_code.start();

    blue_scanner = new BluetoothScanner(config, relay_control, sound_player, note);

    sound_player.playSuccess();
    note.sendNotification("startup");

    rpc_server = new RpcServer(config, relay_control);

    System.out.println("Gate Control Started");


  }


}
