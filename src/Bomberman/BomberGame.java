package Bomberman;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;

//BomberGame Implements ActionListener en is een JPanel
public class BomberGame extends JPanel
    implements ActionListener
{

    private BomberMain main;
    private boolean gameOver;
    private BomberMap map;
    private int winner;
    //Timer om tijd bij te houden spel. 
    private Timer timer;
    private int elapsedSec;
    private static Object hints = null;
    private static Image images[];
    public static int totalPlayers;
    public static int playersLeft;
    public static BomberPlayer players[] = null;

    //BomberGame erft van BomberMain en BomberMap. 
    public BomberGame(BomberMain bombermain, BomberMap bombermap, int i)
    {
        main = null;
        gameOver = false;
        map = null;
        winner = -1;
        timer = null;
        elapsedSec = 0;
        main = bombermain;
        map = bombermap;
        totalPlayers = playersLeft = i;
        try
        {
            MediaTracker mediatracker = new MediaTracker(this);
            for(int k = 0; k < 6; k++)
            {
                mediatracker.addImage(images[k], k);
            }

            mediatracker.waitForAll();
        }
        catch(Exception exception)
        {
            new ErrorDialog(exception);
        }
        players = new BomberPlayer[i];
        for(int j = 0; j < i; j++)
        {
            players[j] = new BomberPlayer(this, bombermap, j + 1);
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
            for(int i = 0; i < totalPlayers; i++)
            {
                players[i].keyPressed(keyevent);
            }

        } else
        if(keyevent.getKeyCode() == 10)
        {
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
            for(int i = 0; i < totalPlayers; i++)
            {
                players[i].keyReleased(keyevent);
            }

        }
    }
//Tekenen van de aantal spelers en en wie er dood gaat// gewonnen heeft 
    public void paint(Graphics g)
    {
        Graphics g1 = g;
        if(!gameOver)
        {
            for(int i = 0; i < totalPlayers; i++)
            {
                players[i].paint(g);
            }

        }
        if(Main.J2)
        {
            paint2D(g);
        } else
        {
            if(gameOver)
            {
                g1.drawImage(images[winner], 0, -25, 272, 272, this);
                if(elapsedSec == 0)
                {
                    g1.drawImage(images[5], 0, 272 - images[5].getHeight(this) / 2, images[5].getWidth(this) / 2, images[5].getHeight(this) / 2, this);
                } else
                {
                    g1.fillRect(0, 272 - images[5].getHeight(this) / 2, images[5].getWidth(this) / 2, images[5].getHeight(this) / 2);
                }
            }
            if(playersLeft <= 1 && timer == null)
            {
                for(int j = 0; j < totalPlayers; j++)
                {
                    players[j].deactivate();
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
            graphics2d.drawImage(images[winner], 0, -25, 272, 272, this);
            if(elapsedSec == 0)
            {
                graphics2d.drawImage(images[5], 0, 272 - images[5].getHeight(this) / 2, images[5].getWidth(this) / 2, images[5].getHeight(this) / 2, this);
            } else
            {
                graphics2d.fillRect(0, 272 - images[5].getHeight(this) / 2, images[5].getWidth(this) / 2, images[5].getHeight(this) / 2);
            }
        }
        if(playersLeft <= 1 && timer == null)
        {
            for(int i = 0; i < totalPlayers; i++)
            {
                players[i].deactivate();
            }

            timer = new Timer(1000, this);
            timer.start();
        }
    }
//public ActionPerformed, laat de BackGroundManager bezig zijn. 
    public void actionPerformed(ActionEvent actionevent)
    {
        elapsedSec++;
        if(elapsedSec >= 4)
        {
            if(Main.J2)
            {
                BomberBGM.mute();
            }
            winner = 4;
            for(int i = 0; i < totalPlayers; i++)
            {
                if(players[i].isDead())
                {
                    continue;
                }
                winner = i;
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
            elapsedSec %= 2;
            paintImmediately(0, 272 - images[5].getHeight(this) / 2, images[5].getWidth(this) / 2, images[5].getHeight(this) / 2);
        }
    }
// Houd de keys in stand en laat zien wie er de winnaar is.(dmv een plaatje)
    static 
    {
        images = null;
        totalPlayers = 4;
        playersLeft = totalPlayers;
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
        Object obj = null;
        images = new Image[6];
        try
        {
            for(int i = 0; i < 4; i++)
            {
                String s1 = s + "Player " + (i + 1) + " Wins.jpg";
                images[i] = Toolkit.getDefaultToolkit().getImage((new File(s1)).getCanonicalPath());
            }

            String s2 = s + "Draw.jpg";
            images[4] = Toolkit.getDefaultToolkit().getImage((new File(s2)).getCanonicalPath());
            s2 = s + "Enter to Continue.jpg";
            images[5] = Toolkit.getDefaultToolkit().getImage((new File(s2)).getCanonicalPath());
        }
        catch(Exception exception)
        {
            new ErrorDialog(exception);
        }
    }
}
