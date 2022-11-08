package duckutil.gatecontrol;


import java.util.concurrent.LinkedBlockingQueue;
import duckutil.PeriodicThread;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import duckutil.Config;


public class Notification extends PeriodicThread
{
  private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	private final String topic_arn;
  private final AmazonSNSClient sns;

  public Notification(Config config)
  {
    super(100);
    topic_arn = config.get("alert_topic_arn");
    sns = new AmazonSNSClient(new BasicAWSCredentials(config.get("aws_key_id"), config.get("aws_secret")));
    sns.setEndpoint("sns.us-west-2.amazonaws.com");
  }


  @Override
  public void runPass()
    throws Exception
  {
    while(true)
    {
      String f = queue.take();
      sendNotificationInternal(f);
    }
  }

  public void sendNotification(String msg)
  {
    queue.offer(msg);
  }

  private void sendNotificationInternal(String msg)
  {
		sns.publish(topic_arn, msg, "gate control");

  }




}
