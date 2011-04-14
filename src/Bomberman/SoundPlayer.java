package Bomberman;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.Vector;
import javax.sound.midi.*;
import javax.sound.sampled.*;
import javax.swing.*;

//SoundPlayer wordt gebruikt voor de geluidseffecten
@SuppressWarnings("serial")
public class SoundPlayer extends JPanel
implements Runnable, LineListener, MetaEventListener, ActionListener
{

	public Vector<File> Muziek;
	private Thread thread;
	private Sequencer sequencer;
	private boolean midiEOM;
	private boolean audioEOM;
	private Synthesizer synthesizer;
	private MidiChannel channels[];
	private Object huidigemuziek;
	private String huidigenaam;
	private int num;
	private boolean bump;
	private boolean gepauzeerd;
	public boolean loop;
	private int volumn;
	JButton startknop;
	JButton pauseknop;
	JButton vorigeknop;
	JButton volgendeknop;

	public SoundPlayer(String s)
	{
		Muziek = new Vector<File>();
		num = -1;
		gepauzeerd = false;
		loop = true;
		volumn = 10;
		if(s != null)
		{
			laadbestand(s);
		}
		JPanel jpanel = new JPanel(new GridLayout(4, 1));
		startknop = voegknoptoe("Start", jpanel, Muziek.size() != 0);
		pauseknop = voegknoptoe("Pause", jpanel, false);
		vorigeknop = voegknoptoe("<<", jpanel, false);
		volgendeknop = voegknoptoe(">>", jpanel, false);
	}

	private JButton voegknoptoe(String s, JPanel jpanel, boolean flag)
	{
		JButton jbutton = new JButton(s);
		jbutton.addActionListener(this);
		jbutton.setEnabled(flag);
		if(jpanel != null)
		{
			jpanel.add(jbutton);
		}
		return jbutton;
	}

	public void open()
	{
		try
		{
			sequencer = MidiSystem.getSequencer();
			if(sequencer instanceof Synthesizer)
			{
				synthesizer = (Synthesizer)sequencer;
				channels = synthesizer.getChannels();
			}
		}
		catch(Exception exception)
		{
			return;
		}
		sequencer.addMetaEventListener(this);
	}

	public void sluit()
	{
		if(thread != null && startknop != null)
		{
			startknop.doClick(0);
		}
		if(sequencer != null)
		{
			sequencer.close();
		}
	}

	public void laadbestand(String s)
	{
		try
		{
			File file = new File(s);
			if(file != null && file.isDirectory())
			{
				String as[] = file.list();
				for(int i = 0; i < as.length; i++)
				{
					File file1 = new File(file.getAbsolutePath(), as[i]);
					if(file1.isDirectory())
					{
						laadbestand(file1.getAbsolutePath());
					} else
					{
						geluidtoevoegen(file1);
					}
				}

			} else
				if(file != null && file.exists())
				{
					geluidtoevoegen(file);
				}
		}
		catch(Exception exception) { }
	}

	private void geluidtoevoegen(File file)
	{
		String s = file.getName();
		if(s.endsWith(".au") || s.endsWith(".rmf") || s.endsWith(".mid") || s.endsWith(".wav") || s.endsWith(".aif") || s.endsWith(".aiff"))
		{
			Muziek.add(file);
		}
	}

	public boolean laadmuziek(Object obj)
	{
		if(obj instanceof URL)
		{
			huidigenaam = ((URL)obj).getFile();
			try
			{
				huidigemuziek = AudioSystem.getAudioInputStream((URL)obj);
			}
			catch(Exception exception)
			{
				try
				{
					huidigemuziek = MidiSystem.getSequence((URL)obj);
				}
				catch(InvalidMidiDataException invalidmididataexception1) { }
				catch(Exception exception5) { }
			}
		} else
			if(obj instanceof File)
			{
				huidigenaam = ((File)obj).getName();
				try
				{
					huidigemuziek = AudioSystem.getAudioInputStream((File)obj);
				}
				catch(Exception exception1)
				{
					try
					{
						FileInputStream fileinputstream = new FileInputStream((File)obj);
						huidigemuziek = new BufferedInputStream(fileinputstream, 1024);
					}
					catch(Exception exception3) { }
				}
			}
		if(sequencer == null)
		{
			huidigemuziek = null;
			return false;
		}
		if(huidigemuziek instanceof AudioInputStream)
		{
			try
			{
				AudioInputStream audioinputstream = (AudioInputStream)huidigemuziek;
				AudioFormat audioformat = audioinputstream.getFormat();
				if(audioformat.getEncoding() == javax.sound.sampled.AudioFormat.Encoding.ULAW || audioformat.getEncoding() == javax.sound.sampled.AudioFormat.Encoding.ALAW)
				{
					AudioFormat audioformat1 = new AudioFormat(javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED, audioformat.getSampleRate(), audioformat.getSampleSizeInBits() * 2, audioformat.getChannels(), audioformat.getFrameSize() * 2, audioformat.getFrameRate(), true);
					audioinputstream = AudioSystem.getAudioInputStream(audioformat1, audioinputstream);
					audioformat = audioformat1;
				}
				javax.sound.sampled.DataLine.Info info = new javax.sound.sampled.DataLine.Info(javax.sound.sampled.Clip.class, audioinputstream.getFormat(), (int)audioinputstream.getFrameLength() * audioformat.getFrameSize());
				Clip clip = (Clip)AudioSystem.getLine(info);
				clip.addLineListener(this);
				clip.open(audioinputstream);
				huidigemuziek = clip;
			}
			catch(Exception exception2) { }
		} else
			if((huidigemuziek instanceof Sequence) || (huidigemuziek instanceof BufferedInputStream))
			{
				try
				{
					sequencer.open();
					if(huidigemuziek instanceof Sequence)
					{
						sequencer.setSequence((Sequence)huidigemuziek);
					} else
					{
						sequencer.setSequence((BufferedInputStream)huidigemuziek);
					}
				}
				catch(InvalidMidiDataException invalidmididataexception)
				{
					return false;
				}
				catch(Exception exception4)
				{
					return false;
				}
			}
		return true;
	}

	public void speelMuziek()
	{
		setGain(volumn);
		setPan();
		midiEOM = audioEOM = bump = false;
		if((huidigemuziek instanceof Sequence) || (huidigemuziek instanceof BufferedInputStream) && thread != null)
		{
			sequencer.start();
			while(!midiEOM && thread != null && !bump) 
			{
				try
				{
					SoundPlayer _tmp = this;
					Thread.sleep(99L);
					continue;
				}
				catch(Exception exception) { }
				break;
			}
			sequencer.stop();
			sequencer.close();
		} else
			if((huidigemuziek instanceof Clip) && thread != null)
			{
				Clip clip = (Clip)huidigemuziek;
				clip.start();
				try
				{
					SoundPlayer _tmp1 = this;
					Thread.sleep(99L);
				}
				catch(Exception exception1) { }
				while((gepauzeerd || clip.isActive()) && thread != null && !bump) 
				{
					try
					{
						SoundPlayer _tmp2 = this;
						Thread.sleep(99L);
						continue;
					}
					catch(Exception exception2) { }
					break;
				}
				clip.stop();
				clip.close();
			}
		huidigemuziek = null;
	}

	public void update(LineEvent lineevent)
	{
		if(lineevent.getType() == javax.sound.sampled.LineEvent.Type.STOP && !gepauzeerd)
		{
			audioEOM = true;
		}
	}

	public void meta (MetaMessage metamessage)
	{
		if(metamessage.getType() == 47)
		{
			midiEOM = true;
		}
	}

	public Thread getThread()
	{
		return thread;
	}

	public void start()
	{
		thread = new Thread(this);
		thread.setName("SoundPlayer");
		thread.start();
	}

	public void stop()
	{
		if(thread != null)
		{
			thread.interrupt();
		}
		thread = null;
	}

	public void run()
	{
		do
		{
			if(laadmuziek(Muziek.elementAt(num)))
			{
				speelMuziek();
			}
		} while(loop && thread != null);
		if(thread != null)
		{
			startknop.doClick();
		}
		thread = null;
		huidigenaam = null;
		huidigemuziek = null;
	}

	public void setPan()
	{
		int i = 0;
		if(huidigemuziek instanceof Clip)
		{
			try
			{
				Clip clip = (Clip)huidigemuziek;
				FloatControl floatcontrol = (FloatControl)clip.getControl(javax.sound.sampled.FloatControl.Type.PAN);
				floatcontrol.setValue((float)i / 100F);
			}
			catch(Exception exception) { }
		} else
			if((huidigemuziek instanceof Sequence) || (huidigemuziek instanceof BufferedInputStream))
			{
				for(int j = 0; j < channels.length; j++)
				{
					channels[j].controlChange(10, (int)((((double)i + 100D) / 200D) * 127D));
				}

			}
	}

	public void setGain(double d)
	{
		if(huidigemuziek instanceof Clip)
		{
			try
			{
				Clip clip = (Clip)huidigemuziek;
				FloatControl floatcontrol = (FloatControl)clip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
				float f = (float)((Math.log(d != 0.0D ? d : 0.0001D) / Math.log(10D)) * 20D);
				floatcontrol.setValue(f);
			}
			catch(Exception exception) { }
		} else
			if((huidigemuziek instanceof Sequence) || (huidigemuziek instanceof BufferedInputStream))
			{
				for(int i = 0; i < channels.length; i++)
				{
					channels[i].controlChange(7, (int)(d * 127D));
				}

			}
	}

	public void dempen()
	{
		volumn = 9;
		setGain(volumn);
		bump = true;
	}

	public void dempenstoppen()
	{
		volumn = 10;
		setGain(volumn);
		bump = true;
	}

	public void aanpassen(int i, boolean flag)
	{
		gepauzeerd = false;
		pauseknop.setText("Pause");
		loop = flag;
		num = i;
		bump = true;
		if(startknop.getText().equals("Start"))
		{
			startknop.doClick();
		}
	}

	public void controlPlay()
	{
		startknop.doClick();
	}

	public void controlStop()
	{
		startknop.doClick();
	}

	public void controlvorige()
	{
		vorigeknop.doClick();
	}

	public void controlvolgende()
	{
		volgendeknop.doClick();
	}

	public void setComponentsEnabled(boolean flag)
	{
		pauseknop.setEnabled(flag);
		vorigeknop.setEnabled(flag);
		volgendeknop.setEnabled(flag);
	}

	public boolean afspelen()
	{
		return !startknop.getText().equals("Start");
	}

	public void actionPerformed(ActionEvent actionevent)
	{
		JButton jbutton = (JButton)actionevent.getSource();
		if(jbutton.getText().equals("Start"))
		{
			gepauzeerd = false;
			num = num != -1 ? num : 0;
			start();
			jbutton.setText("Stop");
			setComponentsEnabled(true);
		} else
			if(jbutton.getText().equals("Stop"))
			{
				gepauzeerd = false;
				stop();
				jbutton.setText("Start");
				pauseknop.setText("Pause");
				setComponentsEnabled(false);
			} else
				if(jbutton.getText().equals("Pause"))
				{
					gepauzeerd = true;
					if(huidigemuziek instanceof Clip)
					{
						((Clip)huidigemuziek).stop();
					} else
						if((huidigemuziek instanceof Sequence) || (huidigemuziek instanceof BufferedInputStream))
						{
							sequencer.stop();
						}
					pauseknop.setText("Resume");
				} else
					if(jbutton.getText().equals("Resume"))
					{
						gepauzeerd = false;
						if(huidigemuziek instanceof Clip)
						{
							((Clip)huidigemuziek).start();
						} else
							if((huidigemuziek instanceof Sequence) || (huidigemuziek instanceof BufferedInputStream))
							{
								sequencer.start();
							}
						pauseknop.setText("Pause");
					} else
						if(jbutton.getText().equals("<<"))
						{
							gepauzeerd = false;
							pauseknop.setText("Pause");
							num = num - 1 >= 0 ? num - 2 : Muziek.size() - 1;
							bump = true;
						} else 
							if(jbutton.getText().equals(">>"))
							{
								gepauzeerd = false;
								pauseknop.setText("Pause");
								num = num + 1 != Muziek.size() ? num : -1;
								bump = true;
							}
	}
}
