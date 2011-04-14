package Bomberman;
import java.io.*;

// keyopslaan in array gekoppeld aan de game. 
public abstract class BomberKeyConfig
{

	public static int toetsen[][] = null;
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

	public static boolean openBestand()
	{
		boolean flag = true;
		try
		{
			ObjectInputStream objectinputstream = new ObjectInputStream(new FileInputStream("BomberKeyConfig.dat"));
			toetsen = (int[][])objectinputstream.readObject();
			objectinputstream.close();
		}
		catch(Exception exception)
		{
			flag = false;
		}
		return flag;
	}

	public static void schrijfBestand()
	{
		try
		{
			ObjectOutputStream objectoutputstream = new ObjectOutputStream(new FileOutputStream("BomberKeyConfig.dat"));
			objectoutputstream.writeObject((int[][])toetsen);
			objectoutputstream.close();
		}
		catch(Exception exception)
		{
			new ErrorDialog(exception);
		}
	}

	public static void maakStandaardBestand()
	{
		if(toetsen == null)
		{
			toetsen = new int[4][5];
		}
		toetsen[0][0] = 38;
		toetsen[0][1] = 40;
		toetsen[0][2] = 37;
		toetsen[0][3] = 39;
		toetsen[0][4] = 96;
		toetsen[1][0] = 87;
		toetsen[1][1] = 83;
		toetsen[1][2] = 65;
		toetsen[1][3] = 68;
		toetsen[1][4] = 32;
		toetsen[2][0] = 73;
		toetsen[2][1] = 75;
		toetsen[2][2] = 74;
		toetsen[2][3] = 76;
		toetsen[2][4] = 92;
		toetsen[3][0] = 104;
		toetsen[3][1] = 101;
		toetsen[3][2] = 100;
		toetsen[3][3] = 102;
		toetsen[3][4] = 105;
		schrijfBestand();
	}

	static 
	{
		if(!openBestand())
		{
			maakStandaardBestand();
			openBestand();
		}
	}
}
