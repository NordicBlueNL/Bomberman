package Bomberman;

import java.awt.*;

//Klasse BomberBonus, ook deze heeft een thread. 
public class BomberBonus extends Thread
{

    private BomberMap map;
    private int x;
    private int y;
    private int frame;
    private boolean levend;
    private int type;
    private Image plaatjes[];
    private static Object hints = null;
    private static int VUUR = 0;
    private static int BOMB = 1;

    //BomberBonus, erft van BomberMap.
    public BomberBonus(BomberMap bombermap, int i, int j, int k)
    {
        map = null;
        x = 0;
        y = 0;
        frame = 0;
        levend = true;
        type = 0;
        plaatjes = null;
        map = bombermap;
        x = i;
        y = j;
        type = k;
        //BomberBonus haalt de bonusplaatjes uit de BomberMap
        plaatjes = BomberMap.bonusImages[k];
        setPriority(10);
        start();
    }
//Thread Run om te kijken of de persoon nog levend is of niet en wel of niet de bonus mag/kan pakken
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
            if(frame == 10)
            {
                break;
            }
        }
        map.removeBonus(x, y);
    }
//Sounds voor het vuur/etc als een bom ontploft
    public void giveToPlayer(int i)
    {
        BomberMain.sndEffectSpeler.speelmuziek("Bonus");
        if(type == VUUR)
        {
            BomberSpel.spelers[i - 1].fireLength++;
        } else
        if(type == BOMB)
        {
            BomberSpel.spelers[i - 1].totalBombs++;
        }
        kill();
    }

    //Kill laat zien of de persoon ontploft is, als dit zo is zet hij Alive op false. 
    public void kill()
    {
        levend = false;
        interrupt();
    }
//Paint van images
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
//Paint van Images
    public void paint2D(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setRenderingHints((RenderingHints)hints);
        graphics2d.drawImage(plaatjes[frame], x, y, 16, 16, null);
    }
//Static laat ook weer de keys zien die gebruikt worden in het spel, de huidige keys. 
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
