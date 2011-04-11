package Bomberman;

import java.awt.*;

//BomberFire Thread. Klasse Vuur na de Bom
public class BomberVuur extends Thread
{

    private BomberMap map;
    private int rooster[][];
    private int x;
    private int y;
    private int type;
    private int frame;
    private int eigenaar;
    private static Image plaatjes[][] = null;
    private static Object hints = null;

    public BomberVuur(BomberMap bombermap, int i, int j, int k)
    {
        map = null;
        rooster = null;
        x = 0;
        y = 0;
        type = 0;
        frame = 0;
        eigenaar = 0;
        map = bombermap;
        rooster = bombermap.rooster;
        x = i;
        y = j;
        type = k;
        eigenaar = eigenaar - 1;
        plaatjes = BomberMap.fireImages;
        if(k == 7)
        {
            rooster[i >> 4][j >> 4] = 7;
        }
        bombermap.fireRooster[i >> 4][j >> 4] = true;
        if(bombermap.bonusRooster[i >> 4][j >> 4] != null)
        {
            bombermap.removeBonus(i, j);
        }
        setPriority(10);
        start();
    }
//Run muziek van het vuur. 
    public void run()
    {
        do
        {
            paint();
            for(int i = 0; i < BomberSpel.totaalSpelers; i++)
            {
                if(BomberSpel.spelers[i].x >> 4 == x >> 4 && BomberSpel.spelers[i].y >> 4 == y >> 4)
                {
                    BomberSpel.spelers[i].kill();
                }
            }

            frame = frame + 1;
            try
            {
                Thread.sleep(65L);
            }
            catch(Exception exception) { }
        } while(frame <= 7);
        map.rooster[x >> 4][y >> 4] = -1;
        map.fireRooster[x >> 4][y >> 4] = false;
        map.paintImmediately(x, y, 16, 16);
        if(type == 7)
        {
            map.createBonus(x, y);
        }
    }
//teken plaatjes vuur (BomberMap)
    public void paint()
    {
        Graphics g = map.getGraphics();
        if(Main.J2)
        {
            paint2D(map.getGraphics());
        } else
        {
            g.drawImage(plaatjes[type][frame], x, y, 16, 16, null);
        }
        g.dispose();
    }
//Teken plaatejs Vuur
    public void paint2D(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setRenderingHints((RenderingHints)hints);
        graphics2d.drawImage(plaatjes[type][frame], x, y, 16, 16, null);
    }
    //toetshoudbaarheid
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
