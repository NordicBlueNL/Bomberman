package Bomberman;

import java.io.File;

//SoundEffect, bomberman. welke sounds gebruikt worden bij welke acties. 
public class BomberSndEffect extends Thread
{

    public BomberSndEffect()
    {
        start();
    }

    public void speelmuziek(String s)
    {
        if(Main.J2)
        {
            SoundPlayer soundplayer = null;
            try
            {
                soundplayer = new SoundPlayer((new File(BomberMain.RP + "src/Sounds/BomberSndEffects/" + s + ".mid")).getCanonicalPath());
            }
            catch(Exception exception) { }
            soundplayer.open();
            soundplayer.aanpassen(0, false);
        }
    }
}
