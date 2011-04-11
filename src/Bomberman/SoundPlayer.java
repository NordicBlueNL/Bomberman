package Bomberman;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.EventObject;
import java.util.Vector;
import javax.sound.midi.*;
import javax.sound.sampled.*;
import javax.swing.*;

//SoundPlayer, hierin worden alle nummers geselecteerd wanneer wat welk nummer terecht komt en afgespeeld word
public class SoundPlayer extends JPanel
    implements Runnable, LineListener, MetaEventListener, ActionListener
{

    public Vector sounds;
    private Thread thread;
    private Sequencer sequencer;
    private boolean midiEOM;
    private boolean audioEOM;
    private Synthesizer synthesizer;
    private MidiChannel channels[];
    private Object currentSound;
    private String currentName;
    private int num;
    private boolean bump;
    private boolean paused;
    public boolean loop;
    private int volumn;
    JButton startB;
    JButton pauseB;
    JButton prevB;
    JButton nextB;

    public SoundPlayer(String s)
    {
        sounds = new Vector();
        num = -1;
        paused = false;
        loop = true;
        volumn = 100;
        if(s != null)
        {
            loadFile(s);
        }
        JPanel jpanel = new JPanel(new GridLayout(4, 1));
        startB = addButton("Start", jpanel, sounds.size() != 0);
        pauseB = addButton("Pause", jpanel, false);
        prevB = addButton("<<", jpanel, false);
        nextB = addButton(">>", jpanel, false);
    }

    private JButton addButton(String s, JPanel jpanel, boolean flag)
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

    public void close()
    {
        if(thread != null && startB != null)
        {
            startB.doClick(0);
        }
        if(sequencer != null)
        {
            sequencer.close();
        }
    }

    public void loadFile(String s)
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
                        loadFile(file1.getAbsolutePath());
                    } else
                    {
                        addSound(file1);
                    }
                }

            } else
            if(file != null && file.exists())
            {
                addSound(file);
            }
        }
        catch(Exception exception) { }
    }

    private void addSound(File file)
    {
        String s = file.getName();
        if(s.endsWith(".au") || s.endsWith(".rmf") || s.endsWith(".mid") || s.endsWith(".wav") || s.endsWith(".aif") || s.endsWith(".aiff"))
        {
            sounds.add(file);
        }
    }

    public boolean loadSound(Object obj)
    {
        if(obj instanceof URL)
        {
            currentName = ((URL)obj).getFile();
            try
            {
                currentSound = AudioSystem.getAudioInputStream((URL)obj);
            }
            catch(Exception exception)
            {
                try
                {
                    currentSound = MidiSystem.getSequence((URL)obj);
                }
                catch(InvalidMidiDataException invalidmididataexception1) { }
                catch(Exception exception5) { }
            }
        } else
        if(obj instanceof File)
        {
            currentName = ((File)obj).getName();
            try
            {
                currentSound = AudioSystem.getAudioInputStream((File)obj);
            }
            catch(Exception exception1)
            {
                try
                {
                    FileInputStream fileinputstream = new FileInputStream((File)obj);
                    currentSound = new BufferedInputStream(fileinputstream, 1024);
                }
                catch(Exception exception3) { }
            }
        }
        if(sequencer == null)
        {
            currentSound = null;
            return false;
        }
        if(currentSound instanceof AudioInputStream)
        {
            try
            {
                AudioInputStream audioinputstream = (AudioInputStream)currentSound;
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
                currentSound = clip;
            }
            catch(Exception exception2) { }
        } else
        if((currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream))
        {
            try
            {
                sequencer.open();
                if(currentSound instanceof Sequence)
                {
                    sequencer.setSequence((Sequence)currentSound);
                } else
                {
                    sequencer.setSequence((BufferedInputStream)currentSound);
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

    public void playSound()
    {
        setGain(volumn);
        setPan();
        midiEOM = audioEOM = bump = false;
        if((currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream) && thread != null)
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
        if((currentSound instanceof Clip) && thread != null)
        {
            Clip clip = (Clip)currentSound;
            clip.start();
            try
            {
                SoundPlayer _tmp1 = this;
                Thread.sleep(99L);
            }
            catch(Exception exception1) { }
            while((paused || clip.isActive()) && thread != null && !bump) 
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
        currentSound = null;
    }

    public void update(LineEvent lineevent)
    {
        if(lineevent.getType() == javax.sound.sampled.LineEvent.Type.STOP && !paused)
        {
            audioEOM = true;
        }
    }

    public void meta(MetaMessage metamessage)
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
            if(loadSound(sounds.elementAt(num)))
            {
                playSound();
            }
        } while(loop && thread != null);
        if(thread != null)
        {
            startB.doClick();
        }
        thread = null;
        currentName = null;
        currentSound = null;
    }

    public void setPan()
    {
        int i = 0;
        if(currentSound instanceof Clip)
        {
            try
            {
                Clip clip = (Clip)currentSound;
                FloatControl floatcontrol = (FloatControl)clip.getControl(javax.sound.sampled.FloatControl.Type.PAN);
                floatcontrol.setValue((float)i / 100F);
            }
            catch(Exception exception) { }
        } else
        if((currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream))
        {
            for(int j = 0; j < channels.length; j++)
            {
                channels[j].controlChange(10, (int)((((double)i + 100D) / 200D) * 127D));
            }

        }
    }

    public void setGain(double d)
    {
        if(currentSound instanceof Clip)
        {
            try
            {
                Clip clip = (Clip)currentSound;
                FloatControl floatcontrol = (FloatControl)clip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
                float f = (float)((Math.log(d != 0.0D ? d : 0.0001D) / Math.log(10D)) * 20D);
                floatcontrol.setValue(f);
            }
            catch(Exception exception) { }
        } else
        if((currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream))
        {
            for(int i = 0; i < channels.length; i++)
            {
                channels[i].controlChange(7, (int)(d * 127D));
            }

        }
    }

    public void mute()
    {
        volumn = 5;
        setGain(volumn);
        bump = true;
    }

    public void unmute()
    {
        volumn = 100;
        setGain(volumn);
        bump = true;
    }

    public void change(int i, boolean flag)
    {
        paused = false;
        pauseB.setText("Pause");
        loop = flag;
        num = i;
        bump = true;
        if(startB.getText().equals("Start"))
        {
            startB.doClick();
        }
    }

    public void controlPlay()
    {
        startB.doClick();
    }

    public void controlStop()
    {
        startB.doClick();
    }

    public void controlBack()
    {
        prevB.doClick();
    }

    public void controlNext()
    {
        nextB.doClick();
    }

    public void setComponentsEnabled(boolean flag)
    {
        pauseB.setEnabled(flag);
        prevB.setEnabled(flag);
        nextB.setEnabled(flag);
    }

    public boolean isPlaying()
    {
        return !startB.getText().equals("Start");
    }

    public void actionPerformed(ActionEvent actionevent)
    {
        JButton jbutton = (JButton)actionevent.getSource();
        if(jbutton.getText().equals("Start"))
        {
            paused = false;
            num = num != -1 ? num : 0;
            start();
            jbutton.setText("Stop");
            setComponentsEnabled(true);
        } else
        if(jbutton.getText().equals("Stop"))
        {
            paused = false;
            stop();
            jbutton.setText("Start");
            pauseB.setText("Pause");
            setComponentsEnabled(false);
        } else
        if(jbutton.getText().equals("Pause"))
        {
            paused = true;
            if(currentSound instanceof Clip)
            {
                ((Clip)currentSound).stop();
            } else
            if((currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream))
            {
                sequencer.stop();
            }
            pauseB.setText("Resume");
        } else
        if(jbutton.getText().equals("Resume"))
        {
            paused = false;
            if(currentSound instanceof Clip)
            {
                ((Clip)currentSound).start();
            } else
            if((currentSound instanceof Sequence) || (currentSound instanceof BufferedInputStream))
            {
                sequencer.start();
            }
            pauseB.setText("Pause");
        } else
        if(jbutton.getText().equals("<<"))
        {
            paused = false;
            pauseB.setText("Pause");
            num = num - 1 >= 0 ? num - 2 : sounds.size() - 1;
            bump = true;
        } else
        if(jbutton.getText().equals(">>"))
        {
            paused = false;
            pauseB.setText("Pause");
            num = num + 1 != sounds.size() ? num : -1;
            bump = true;
        }
    }
}
