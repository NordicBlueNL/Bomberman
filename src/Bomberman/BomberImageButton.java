package Bomberman;

import java.awt.*;
import javax.swing.JPanel;

//BomberImageButton, de buttonsplaatjes zoals Controlsettings, aantal spelers die spelen en Exit menu. 
public class BomberImageButton
{
	//ImageButton JPanel 
	private JPanel paneel;
	private int x;
	private int y;
	private int ID;
	private int w;
	private int h;
	//plaatjes buttons
	private Image plaatjes[];
	private int staat;
	private static Object hints = null;
	//BomberImageButton laat de buttonsplaatjes zien. 
	public BomberImageButton(JPanel jpanel, Image aimage[])
	{
		staat = 0;
		paneel = jpanel;
		plaatjes = aimage;
		w = aimage[0].getWidth(jpanel);
		h = aimage[0].getHeight(jpanel);
	}
	//Setinfo, X,Y, ID en Rect. 
	public void setInfo(int i, int j, int k)
	{
		x = i;
		y = j;
		ID = k;
	}
	//GetID, Identification
	public int getID()
	{
		return ID;
	}
	//Set Bevel
	public void setBevel(boolean flag)
	{
		if(flag)
		{
			staat = 1;
		} else
		{
			staat = 0;
		}
		paneel.repaint();
		paneel.paintImmediately(x, y, w / 4, h / 4);
	}
	//Painten van de images
	public void paint(Graphics g)
	{
		if(Main.J2)
		{
			paint2D(g);
		} else
		{
			Graphics g1 = g;
			g1.drawImage(plaatjes[staat], x, y, w / 4, h / 4, null);
		}
	}

	public void paint2D(Graphics g)
	{
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.setRenderingHints((RenderingHints)hints);
		graphics2d.drawImage(plaatjes[staat], x, y, w / 4, h / 4, null);
	}
	//Houd de keys in stand op elke soort menu
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
