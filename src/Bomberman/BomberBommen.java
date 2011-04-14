package Bomberman;

import java.awt.*;

//BomberBomb, deze heeft een Thread. 
public class BomberBommen extends Thread
{
	//BomberMap linken aan BomberBommen
	private BomberMap map;
	private int x = 0;
	private int y = 0;
	private int frame = 0;
	//levend of niet levend?
	private boolean levend = true;
	//welke speler hoort bij welke eigenaar. 
	private int eigenaar = 0;
	//Tijd bijna over?
	private int aftellen = 3900;
	//Array plaatjes
	private static Image plaatjes[] = null;
	private static Object hints = null;

	public BomberBommen(BomberMap bombermap, int i, int j, int k)
	{
		map = null;
		this.map = bombermap;
		x = i;
		y = j;
		eigenaar = k - 1;
		plaatjes = BomberMap.bombPlaatjes;
		bombermap.rooster[i >> 4][j >> 4] = 3;
		setPriority(10);
		start();
	}
	//Run, als het allemaal aan is. met een meth. thread om de tijd bij te houden. 
	public synchronized void run()
	{
		while(levend) 
		{
			map.paintImmediately(x, y, 16, 16);
			frame = (frame + 1) % 2;
			try
			{
				Thread.sleep(130L);
			}
			catch(Exception exception) { }
			if(!levend)
			{
				break;
			}
			aftellen -= 130;
			if(aftellen <= 0)
			{
				break;
			}
		}
		map.rooster[x >> 4][y >> 4] = -1;
		BomberSpel.spelers[eigenaar].usedBombs--;
		map.bombGrid[x >> 4][y >> 4] = null;
		BomberSpel.spelers[eigenaar].bombGrid[x >> 4][y >> 4] = false;
		map.removeBomb(x, y);
		BomberMain.sndEffectSpeler.speelmuziek("Explosion");
		map.createFire(x, y, eigenaar, 0);
	}
	//shortBomb, kort maar krachtig korte bom inzetten
	public void shortBomb()
	{
		levend = false;
		interrupt();
	}
	//Paint images. 
	public void paint(Graphics g)
	{
		if(Main.J2)
		{
			paint2D(g);
		} else
		{
			g.drawImage(plaatjes[frame], x, y, 16, 16, null);
		}
	}
	//Paint images
	public void paint2D(Graphics g)
	{
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.setRenderingHints((RenderingHints)hints);
		//was 
		//graphics2d.drawImage(plaatjes[frame], x, y, 16, 16, null);
		graphics2d.drawImage(plaatjes[frame], x, y, 16, 16, null);
	}
	//Static Keys en Bepaalt het basistype van alle toetsen gebruikt om de verschillende aspecten van de rendering en imaging pijpleidingen controle
	static 
	{
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
	}
}
