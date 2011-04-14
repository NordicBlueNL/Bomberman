package Bomberman;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JFrame;

//BomberMain de hoofdapp die dus JFrame gebruikt en de vormgeving van de app, het programma 
public class BomberMain extends JFrame
{

	public static String RP = "./";
	private BomberMenu menu;
	private BomberSpel game;
	public static BomberSndEffect sndEffectSpeler = null;
	public static final int shiftCount = 4;
	//was public static final int size = 16;
	public static final int size = 16;


	@SuppressWarnings("deprecation")
	public BomberMain()
	{
		menu = null;
		game = null;
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent windowevent)
			{
				System.exit(0);
			}

		});
		addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent keyevent)
			{
				if(menu != null)
				{
					menu.keyPressed(keyevent);
				}
				if(game != null)
				{
					game.keyPressed(keyevent);
				}
			}

			public void keyReleased(KeyEvent keyevent)
			{
				if(game != null)
				{
					game.keyReleased(keyevent);
				}
			}

		});
		setTitle("Bomberman 2.0, Pascal, Niels en Arne Inf1CD Groep 1");
		try
		{
			setIconImage(Toolkit.getDefaultToolkit().getImage((new File(RP + "src/Images/Bomberman.gif")).getCanonicalPath()));
		}
		catch(Exception exception)
		{
			new ErrorDialog(exception);
		}
		getContentPane().add(menu = new BomberMenu(this));
		setResizable(true);//was false (niet groter kunnen maken)
		pack();
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int i = (dimension.width - getSize().width) / 2;
		int j = (dimension.height - getSize().height) / 2;
		setLocation(i, j);
		show();
		toFront();
	}

	@SuppressWarnings("deprecation")
	public void newGame(int i)
	{
		JDialog jdialog = new JDialog(this, "Loading Game...", false);
		jdialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		jdialog.setSize(new Dimension(200, 0));
		jdialog.setResizable(false);
		//was int J = getLocation().x + (getSize().width - 200) / 2
		int j = getLocation().x + (getSize().width-200 ) / 2;
		int k = getLocation().y + getSize().height / 2;
		jdialog.setLocation(j, k);
		jdialog.show();
		getContentPane().removeAll();
		getLayeredPane().removeAll();
		menu = null;
		BomberMap bombermap = new BomberMap(this);
		game = new BomberSpel(this, bombermap, i);
		jdialog.dispose();
		show();
		if(Main.J2)
		{
			BomberBGM.dempen();
			BomberBGM.change("Battle" );
		}
	}

	public static void main(String []args)
	{
		new BomberMain();
		//hiermee kan je hem meerdere keeren openen tegelijk.... 
		// BomberMain bomberMain1 = new BomberMain();
	}

	static 
	{
		sndEffectSpeler = new BomberSndEffect();
	}


}
