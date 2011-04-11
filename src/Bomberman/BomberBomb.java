package Bomberman;

import java.awt.*;
import javax.swing.JComponent;

//BomberBomb, deze heeft een Thread. 
public class BomberBomb extends Thread
{

    private BomberMap map;
    private int x;
    private int y;
    private int frame;
    //levend of niet levend?
    private boolean alive;
    private int owner;
    //Tijd bijna over?
    private int countDown;
    private static Image images[] = null;
    private static Object hints = null;

    public BomberBomb(BomberMap bombermap, int i, int j, int k)
    {
        map = null;
        x = 0;
        y = 0;
        frame = 0;
        alive = true;
        owner = 0;
        //tijd voordat het spel is afgelopen (miliseconde)(3minuten9seconden())
        countDown = 3900;
        map = bombermap;
        x = i;
        y = j;
        owner = k - 1;
        images = BomberMap.bombImages;
        bombermap.grid[i >> 4][j >> 4] = 3;
        setPriority(10);
        start();
    }

    //Run, als het allemaal aan is. met een meth. thread om de tijd bij te houden. 
    public synchronized void run()
    {
        while(alive) 
        {
            map.paintImmediately(x, y, 16, 16);
            frame = (frame + 1) % 2;
            try
            {
                Thread.sleep(130L);
            }
            catch(Exception exception) { }
            if(!alive)
            {
                break;
            }
            countDown -= 130;
            if(countDown <= 0)
            {
                break;
            }
        }
        map.grid[x >> 4][y >> 4] = -1;
        BomberGame.players[owner].usedBombs--;
        map.bombGrid[x >> 4][y >> 4] = null;
        BomberGame.players[owner].bombGrid[x >> 4][y >> 4] = false;
        map.removeBomb(x, y);
        BomberMain.sndEffectPlayer.playSound("Explosion");
        map.createFire(x, y, owner, 0);
    }

  //shortBomb, kort maar krachtig korte bom inzetten
    public void shortBomb()
    {
        alive = false;
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
            g.drawImage(images[frame], x, y, 16, 16, null);
        }
    }
//Paint images
    public void paint2D(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setRenderingHints((RenderingHints)hints);
        graphics2d.drawImage(images[frame], x, y, 16, 16, null);
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
