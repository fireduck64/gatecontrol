package duckutil.gatecontrol;

import duckutil.jsonrpc.JsonRpcServer;
import duckutil.Config;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.Dispatcher;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;
import duckutil.jsonrpc.JsonRequestHandler;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONArray;
import java.util.Map;

public class RpcServer
{
  private final JsonRpcServer json_rpc_server;
  private final RelayControl relay_control;
  
  public RpcServer(Config config, RelayControl relay_control)
    throws Exception
  {
    this.relay_control = relay_control;
    json_rpc_server = new JsonRpcServer(config, true);

    json_rpc_server.register(new OpenHandler());
    json_rpc_server.register(new ListHandler());
    json_rpc_server.register(new CloseHandler());

  }

  public class OpenHandler extends JsonRequestHandler
  {
    public String[] handledRequests()
    {
      return new String[]{"open"};
    }

    protected JSONObject processRequest(JSONRPC2Request req, MessageContext ctx)
      throws Exception
    {
      
      String id = req.getID().toString();

      Object time_obj = req.getNamedParams().get("time");

      long time_ms=5000;
      if (time_obj instanceof Number)
      {
        Number n = (Number) time_obj;
        time_ms = n.longValue();
      }

      relay_control.connectRelay("rpc_" + id, time_ms);

      JSONObject reply = new JSONObject();

      reply.put("time", time_ms);

      
      return reply;
    }

  }

  public class ListHandler extends JsonRequestHandler
  {
    public String[] handledRequests()
    {
      return new String[]{"list"};
    }

    protected JSONObject processRequest(JSONRPC2Request req, MessageContext ctx)
      throws Exception
    {
      
      String id = req.getID().toString();

      JSONObject reply = new JSONObject();

      JSONArray holds_json = new JSONArray();

      for(Map.Entry<String, Long> me : relay_control.getHolds().entrySet())
      {
        JSONObject h = new JSONObject();
        h.put("id", me.getKey());

        h.put("expire", me.getValue());

        long ttl = me.getValue() - System.currentTimeMillis();
        h.put("ttl",ttl);

        holds_json.add(h);

      }

      reply.put("holds", holds_json);


      
      return reply;
    }

  }

  public class CloseHandler extends JsonRequestHandler
  {
    public String[] handledRequests()
    {
      return new String[]{"close"};
    }

    protected JSONObject processRequest(JSONRPC2Request req, MessageContext ctx)
      throws Exception
    {
      
      String id = req.getID().toString();

      JSONObject reply = new JSONObject();

      relay_control.close();

      
      return reply;
    }

  }

 



}
