package Bomberman;

import javax.swing.UIManager;

//Main van Bomberman met veel javaclasses.  Deze kijkt of het Systeem dit kan openen, verder nog openen van BomberMain 
public class Main
{

	//Main classe aan BomberMain koppelen
	public static BomberMain bomberMain = null;
	//Plaatjespad waar de bombermanplaatjes zijn vastleggen tot src/(hetzelfde)
	public static final String plaatjepad = "./";
	//Boolean controle of Java2 geinstalleerd is. 
	public static boolean J2 = false;

	public Main()
	{

	}

	public static void startBomberman()
	{
		bomberMain = new BomberMain();
	}

	public static void main(String args[])
	{

		int i = 1;
		for(int j = 0; j < args.length; j++)
		{
			if(args[j].startsWith("-l"))
			{
				if(args[j].substring(2).equals("System"))
				{
					i = 0;
				} else
					if(args[j].substring(2).equals("Metal"))
					{
						i = 1;
					} else
						if(args[j].substring(2).equals("Windows"))
						{
							i = 2;
						} else
							if(args[j].substring(2).equals("Mac"))
							{
								i = 3;
							} else
								if(args[j].substring(2).equals("Motif"))
								{
									i = 4;
								}
			}
		}

		if(i != 1)
		{
			try
			{
				String s = "javax.swing.plaf.metal.MetalLookAndFeel";
				if(i == 0)
				{
					s = UIManager.getSystemLookAndFeelClassName();
				} else
					if(i == 1)
					{
						s = "javax.swing.plaf.metal.MetalLookAndFeel";
					} else
						if(i == 2)
						{
							s = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
						} else
							if(i == 3)
							{
								s = "javax.swing.plaf.mac.MacLookAndFeel";
							} else
								if(i == 4)
								{
									s = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
								}
				UIManager.setLookAndFeel(s);
			}
			catch(Exception exception)
			{
				new ErrorDialog(exception);
			}
		}
		startBomberman();
	}

	static 
	{
		String s = System.getProperty("java.version");
		int i = Integer.parseInt(s.substring(0, 1));
		int j = Integer.parseInt(s.substring(2, 3));
		if(i >= 1 && j >= 2)
		{
			J2 = true;
		}
	}
}

