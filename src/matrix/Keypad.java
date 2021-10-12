package duckutil.gatecontrol.matrix;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import duckutil.PeriodicThread;

import duckutil.gatecontrol.SoundPlayer;

/**
 * Reads values from the matrix scan and when they change, calls
 * that a new press.
 */
public class Keypad extends PeriodicThread
{
  private MatrixScan matrix;
  private PipedInputStream key_stream;
  private PipedOutputStream key_out;
  private PrintStream print_out;

  private String last_read;

  public Keypad()
  {
    super(10);
    matrix = new MatrixScan();

    try
    {
      key_stream = new PipedInputStream();
      key_out = new PipedOutputStream(key_stream);
      print_out = new PrintStream(key_out);
    }
    catch(java.io.IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public InputStream getStream()
  {
    return key_stream;
  }

  public void runPass()
  {
    String read = matrix.scan();
    if (read == null)
    {
      last_read=null;
    }
    else
    {
      if (!read.equals(last_read))
      {
        print_out.print(read);
        print_out.flush();
        SoundPlayer.playButtonPress();
        last_read=read;
      }
    }
  }
}
