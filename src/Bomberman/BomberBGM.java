package Bomberman;

import java.io.File;

//Bomberman BackGroundManager 
public class BomberBGM
{
   
    private static Object mspeler;
    private static int laatstGeselecteerd = -1;

    //public BomberBGM
    public BomberBGM()
    {
    	
    }
    static //Als je Java2 hebt
    {
        if(Main.J2)
        {
            try
            {
                mspeler = new SoundPlayer((new File(BomberMain.RP + "Sounds/BomberBGM/")).getCanonicalPath());
                System.out.println("mspeler");
            }
            catch(Exception exception)
            {
                new ErrorDialog(exception);
            }
            ((SoundPlayer)mspeler).open();
        }
    }

    //Change is voor de muziek(Muziek veranderen)
    public static void change(String s)
    {
        if(Main.J2)
        {
            int i;
            for(i = 0; i < ((SoundPlayer)mspeler).Muziek.size() && ((SoundPlayer)mspeler).Muziek.elementAt(i).toString().indexOf(s) < 0; i++) { }
            if(i != laatstGeselecteerd && i < ((SoundPlayer)mspeler).Muziek.size())
            {
                laatstGeselecteerd = i;
                ((SoundPlayer)mspeler).aanpassen(laatstGeselecteerd, true);
            }
        }
    }

    //Muziek stoppen
    public static void stop()
    {
        if(Main.J2)
        {
            ((SoundPlayer)mspeler).controlStop();
        }
    }
    //Muziek niet meer luisteren
    public static void dempen()
    {
        if(Main.J2)
        {
            ((SoundPlayer)mspeler).dempen();
           
        }
    }
    //Muziek weer luisteren
    public static void aanzetten()
    {
        if(Main.J2)
        {
            ((SoundPlayer)mspeler).dempenstoppen();
        }
    }
//Algemeen einde om  o.a. de muziek te laden

}
