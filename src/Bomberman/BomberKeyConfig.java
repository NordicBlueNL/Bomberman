package Bomberman;
import java.io.*;

// keyopslaan in array gekoppeld aan de game. 
public abstract class BomberKeyConfig
{

    public static int keys[][] = null;
    public static final int P1 = 0;
    public static final int P2 = 1;
    public static final int P3 = 2;
    public static final int P4 = 3;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int BOMB = 4;

    public BomberKeyConfig()
    {
    }

    public static boolean openFile()
    {
        boolean flag = true;
        try
        {
            ObjectInputStream objectinputstream = new ObjectInputStream(new FileInputStream("BomberKeyConfig.dat"));
            keys = (int[][])objectinputstream.readObject();
            objectinputstream.close();
        }
        catch(Exception exception)
        {
            flag = false;
        }
        return flag;
    }

    public static void writeFile()
    {
        try
        {
            ObjectOutputStream objectoutputstream = new ObjectOutputStream(new FileOutputStream("BomberKeyConfig.dat"));
            objectoutputstream.writeObject((int[][])keys);
            objectoutputstream.close();
        }
        catch(Exception exception)
        {
            new ErrorDialog(exception);
        }
    }

    public static void createDefaultFile()
    {
        if(keys == null)
        {
            keys = new int[4][5];
        }
        keys[0][0] = 38;
        keys[0][1] = 40;
        keys[0][2] = 37;
        keys[0][3] = 39;
        keys[0][4] = 96;
        keys[1][0] = 87;
        keys[1][1] = 83;
        keys[1][2] = 65;
        keys[1][3] = 68;
        keys[1][4] = 32;
        keys[2][0] = 73;
        keys[2][1] = 75;
        keys[2][2] = 74;
        keys[2][3] = 76;
        keys[2][4] = 92;
        keys[3][0] = 104;
        keys[3][1] = 101;
        keys[3][2] = 100;
        keys[3][3] = 102;
        keys[3][4] = 105;
        writeFile();
    }

    static 
    {
        if(!openFile())
        {
            createDefaultFile();
            openFile();
        }
    }
}
