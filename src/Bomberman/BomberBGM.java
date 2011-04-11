package Bomberman;

import java.io.File;
import java.util.Vector;

//Bomberman BackGroundManager 
public class BomberBGM
{
 
    private static Object player;
    private static int lastSelection = -1;

    //public BomberBGM
    public BomberBGM()
    {
    }

    //Change is voor de muziek(Muziek veranderen)
    public static void change(String s)
    {
        if(Main.J2)
        {
            int i;
            for(i = 0; i < ((SoundPlayer)player).sounds.size() && ((SoundPlayer)player).sounds.elementAt(i).toString().indexOf(s) < 0; i++) { }
            if(i != lastSelection && i < ((SoundPlayer)player).sounds.size())
            {
                lastSelection = i;
                ((SoundPlayer)player).change(lastSelection, true);
            }
        }
    }

    //Muziek stoppen
    public static void stop()
    {
        if(Main.J2)
        {
            ((SoundPlayer)player).controlStop();
        }
    }
    //Muziek niet meer luisteren
    public static void mute()
    {
        if(Main.J2)
        {
            ((SoundPlayer)player).mute();
        }
    }
    //Muziek weer luisteren
    public static void unmute()
    {
        if(Main.J2)
        {
            ((SoundPlayer)player).unmute();
        }
    }
//Algemeen einde om  o.a. de muziek te laden
    static 
    {
        if(Main.J2)
        {
            try
            {
                player = new SoundPlayer((new File(BomberMain.RP + "src/Sounds/BomberBGM/")).getCanonicalPath());
            }
            catch(Exception exception)
            {
                new ErrorDialog(exception);
            }
            ((SoundPlayer)player).open();
        }
    }
}
