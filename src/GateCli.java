package duckutil.gatecontrol;

import duckutil.Config;
import duckutil.ConfigFile;
import duckutil.TimeUtil;
import java.util.ArrayList;
import java.util.Scanner;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;

import duckutil.jsonrpc.JsonRpcClient;

public class GateCli
{ 
  public static void main(String args[])
    throws Exception
  {
    ArrayList<String> args_list = new ArrayList<>();

    for(int i=1; i<args.length; i++)
    {
      args_list.add(args[i]);
    }

    new GateCli(new ConfigFile(args[0]), args_list);

  }
  public GateCli(Config config, ArrayList<String> args)
    throws Exception
  {
    config.require("gate_uri");
    config.require("rpc_username");
    config.require("rpc_password");

    JsonRpcClient client = new JsonRpcClient(config.get("gate_uri"), config.get("rpc_username"), config.get("rpc_password"));


    if (args.size() == 0)
    {
      printHelp();
      return;
    }
    String cmd = args.get(0);

    if (cmd.equals("list"))
    {
      JSONObject resp = client.request(cmd);
      JSONObject res = (JSONObject) resp.get("result");
      JSONArray holds = (JSONArray) res.get("holds");

      System.out.println(resp);
      long high=0;

      for(Object o : holds)
      {
        JSONObject o2 = (JSONObject)o;
        String id = o2.get("id").toString();
        long ttl = (int) o2.get("ttl");
        String dur = TimeUtil.durationToString(ttl);

        high = Math.max(ttl, high);

        System.out.println("Hold - " + id + " - " + dur);



      }
      System.out.println("Effective: " + TimeUtil.durationToString(high));
    }
    else if (cmd.equals("close"))
    {
      System.out.println(client.request(cmd));
    }
    else if (cmd.equals("open"))
    {
      String timespec = args.get(1);
      long mult = 1000L;
      long val = Long.parseLong( timespec.substring(0, timespec.length()-1));
      if (timespec.endsWith("m")) mult = 60000L;
      if (timespec.endsWith("h")) mult = 3600000L;
      if (timespec.endsWith("d")) mult = 86400000L;

      long total = mult * val;
      JSONObject o = new JSONObject();
      o.put("time", total);

      System.out.println("Keeping open for: " + TimeUtil.durationToString(total));

      System.out.println(client.request(cmd, o));


    }

  }

  public void printHelp()
  {
    System.out.println("Commands: ");
    System.out.println(" gate_cli list");
    System.out.println(" gate_cli close");
    System.out.println(" gate_cli open <time>");
    System.out.println("   time options:");
    System.out.println("     8h - 8 hours");
    System.out.println("     15m - 15 minutes");


  }


}
