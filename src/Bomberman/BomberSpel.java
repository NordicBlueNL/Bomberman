package Bomberman;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

//BomberGame Implements ActionListener en is een JPanel
public class BomberSpel extends JPanel
implements ActionListener
{

	private BomberMain main;
	private boolean gameOver;
	private BomberMap map;
	private int winnaar;
	//Timer om tijd bij te houden spel. 
	private Timer timer;
	private int verstrekenSeconden;
	private static Object hints = null;
	private static Image plaatjes[];
	public static int totaalSpelers;
	public static int spelerLinks;
	public static BomberPlayer spelers[] = null;
	MP3 gamesound = new MP3("./src/Sounds/BomberBGM/gamesound.mp3");
	MP3 oversound = new MP3("./src/Sounds/BomberBGM/oversound.mp3");

	//BomberGame erft van BomberMain en BomberMap.
	public BomberSpel(BomberMain bombermain, BomberMap bombermap, int i)
	{
		main = null;
		gameOver = false;
		map = null;
		winnaar = -1;
		timer = null;
		verstrekenSeconden = 0;
		main = bombermain;
		map = bombermap;
		totaalSpelers = spelerLinks = i;
		gamesound.play();
		try
		{
			MediaTracker mediatracker = new MediaTracker(this);
			for(int k = 0; k < 6; k++)
			{
				mediatracker.addImage(plaatjes[k], k);
			}

			mediatracker.waitForAll();
		}
		catch(Exception exception)
		{
			new ErrorDialog(exception);
		}
		spelers = new BomberPlayer[i];
		for(int j = 0; j < i; j++)
		{
			spelers[j] = new BomberPlayer(this, bombermap, j + 1);
		}

		setDoubleBuffered(true);
		setBounds(0, 0, 17 << 4, 17 << 4);
		setOpaque(false);
		bombermain.getLayeredPane().add(this, 0);
	}

	//Wanneer een keyevent is dan kan dit ook stop gezet worden als alle overige spelers overleden zijn
	public void keyPressed(KeyEvent keyevent)
	{
		if(!gameOver)
		{
			for(int i = 0; i < totaalSpelers; i++)
			{
				spelers[i].keyPressed(keyevent);
				oversound.close();
			}

		} else
			if(keyevent.getKeyCode() == 10)
			{
				oversound.close();
				timer.stop();
				timer = null;
				main.dispose();
				new BomberMain();
			}
	}
	// KeyReleased Event als de speler de toets loslaat gaat de toets weer terug naar het aantalspelers en controlinfo menu
	public void keyReleased(KeyEvent keyevent)
	{
		if(!gameOver)
		{
			for(int i = 0; i < totaalSpelers; i++)
			{
				spelers[i].keyReleased(keyevent);
			}

		}
	}
	//Tekenen van de aantal spelers en en wie er dood gaat// gewonnen heeft 
	public void paint(Graphics g)
	{
		Graphics g1 = g;
		if(!gameOver)
		{
			for(int i = 0; i < totaalSpelers; i++)
			{
				spelers[i].paint(g);
			}

		}
		if(Main.J2)
		{
			paint2D(g);
		} else
		{


			if(gameOver)
			{

				g1.drawImage(plaatjes[winnaar], 0, -25, 272, 272, this);
				if(verstrekenSeconden == 0)
				{
					g1.drawImage(plaatjes[5], 0, 272 - plaatjes[5].getHeight(this) / 2, plaatjes[5].getWidth(this) / 2, plaatjes[5].getHeight(this) / 2, this);
				} else
				{
					g1.fillRect(0, 272 - plaatjes[5].getHeight(this) / 2, plaatjes[5].getWidth(this) / 2, plaatjes[5].getHeight(this) / 2);
				}
			}
			if(spelerLinks <= 1 && timer == null)
			{
				for(int j = 0; j < totaalSpelers; j++)
				{
					spelers[j].deactivate();
				}

				timer = new Timer(1000, this);
				timer.start();
			}
		}
	}
	// teken gelijk public void, met de ints i, j, k en l. 
	public void pPaintImmediately(int i, int j, int k, int l)
	{
		paintImmediately(i, j, k, l);
	}
	//Paint 2D.de tekeningen Game Over en welke spelers er nog over blijven. 
	public void paint2D(Graphics g)
	{

		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.setRenderingHints((RenderingHints)hints);
		if(gameOver)
		{

			graphics2d.drawImage(plaatjes[winnaar], 0, -25, 272, 272, this);
			if(verstrekenSeconden == 0)
			{
				graphics2d.drawImage(plaatjes[5], 0, 272 - plaatjes[5].getHeight(this) / 2, plaatjes[5].getWidth(this) / 2, plaatjes[5].getHeight(this) / 2, this);
			} else
			{
				graphics2d.fillRect(0,272 - plaatjes[5].getHeight(this) / 2, plaatjes[5].getWidth(this) / 2, plaatjes[5].getHeight(this) / 2);
			}
		}
		if(spelerLinks <= 1 && timer == null)
		{
			for(int i = 0; i < totaalSpelers; i++)
			{
				spelers[i].deactivate();
			}

			timer = new Timer(1000, this);
			timer.start();
		}
	}
	//public ActionPerformed, laat de BackGroundManager bezig zijn. 
	public void actionPerformed(ActionEvent actionevent)
	{
		verstrekenSeconden++;
		if(verstrekenSeconden >= 4)
		{
			if(Main.J2)
			{
				BomberBGM.dempen();
			}
			winnaar = 4;
			for(int i = 0; i < totaalSpelers; i++)
			{
				if(spelers[i].isDead())
				{
					gamesound.close();
					continue;
				}
			
				winnaar = i;
				break; 
			}
			
			gameOver = true;
			map.setGameOver();
			timer.stop();
			timer = new Timer(500, this);
			timer.start();
		}

		if(gameOver)
		{	
			//timer overheen gooien?
			oversound.play();
			
			oversound.close();
			
			verstrekenSeconden %= 2;
			paintImmediately(0, 272 - plaatjes[5].getHeight(this) / 2, plaatjes[5].getWidth(this) / 2, plaatjes[5].getHeight(this) / 2);
		}
	
			//if( ! gameOver)
			//  { 
			//   oversound.close();
			//   } 
	}
	// Houd de keys in stand en laat zien wie er de winnaar is.(dmv een plaatje)
	static 
	{
		plaatjes = null;
		totaalSpelers = 4;
		spelerLinks = totaalSpelers;
		if(Main.J2)
		{
			RenderingHints renderinghints = null;
			renderinghints = new RenderingHints(null);
			renderinghints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			renderinghints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			renderinghints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			renderinghints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			renderinghints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			hints = renderinghints;
		}
		String s = BomberMain.RP + "src/Images/BomberEndGame/";
		plaatjes = new Image[6];
		try
		{
			for(int i = 0; i < 4; i++)
			{
				String s1 = s + "Player " + (i + 1) + " Wins.jpg";
				plaatjes[i] = Toolkit.getDefaultToolkit().getImage((new File(s1)).getCanonicalPath());
			}

			String s2 = s + "Draw.jpg";
			plaatjes[4] = Toolkit.getDefaultToolkit().getImage((new File(s2)).getCanonicalPath());
			s2 = s + "Enter to Continue.jpg";
			plaatjes[5] = Toolkit.getDefaultToolkit().getImage((new File(s2)).getCanonicalPath());

		}
		catch(Exception exception)
		{
			new ErrorDialog(exception);
		}
	}
}
