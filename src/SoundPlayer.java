package duckutil.gatecontrol;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

import java.util.concurrent.LinkedBlockingQueue;
import duckutil.PeriodicThread;

public class SoundPlayer extends PeriodicThread
{
  public static final boolean sound_enabled=true;

  private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

  public SoundPlayer()
  {
    super(100);
  }


  @Override
  public void runPass()
    throws Exception
  {
    while(true)
    {
      String f = queue.take();
      playSound(f);
    }
  }





  public void playButtonPress()
  {
    queue.offer("LTTP_Magic.wav");
    wake();
  }

  private void playSound(String file)
  {
    try
    {
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(new File(file)));
      clip.start();
      Thread.sleep(10);
      while(clip.isActive())
      {
        Thread.sleep(10);
      }
      clip.close();
    }
    catch(Throwable e)
    {
      e.printStackTrace();
    }

  }

  public void playSuccess()
  {
    queue.offer("LTTP_Secret.wav");
    wake();
  }



}
