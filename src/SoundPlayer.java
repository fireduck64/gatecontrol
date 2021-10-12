package duckutil.gatecontrol;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundPlayer
{
  public static final boolean sound_enabled=false;

  public static void playButtonPress()
  {
    if (!sound_enabled) return;
    try
    {
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(new File("LTTP_Magic.wav")));
      clip.start();
    }
    catch(Throwable e)
    {
      e.printStackTrace();
    }


  }

  public static void playSuccess()
  {
    if (!sound_enabled) return;
    try
    {
      Clip clip = AudioSystem.getClip();
      clip.open(AudioSystem.getAudioInputStream(new File("LTTP_Secret.wav")));
      clip.start();
    }
    catch(Throwable e)
    {
      e.printStackTrace();
    }


  }



}
