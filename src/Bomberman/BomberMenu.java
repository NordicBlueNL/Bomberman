package Bomberman;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.*;


//BomberMenu, het beginmenu van het spel. 
public class BomberMenu extends JPanel
{
	//BomberMenu linken met BomberMain
    private BomberMain main;
    //Array BomberImageButtons
    private BomberImageButton imageButtons[];
    //Int Selection Image
    private int selection;
    //Image BachgroundImage 
    private static Image backgroundImg = null;
    //Image Buttonimage down
    private static Image buttonImagesDown[];
    //Image Buttonimage Up
    private static Image buttonImagesUp[];
    //Object hints
    private static Object hints = null;
  
	
    public BomberMenu(BomberMain bombermain)
    {
    	
        main = null;
        imageButtons = null;
        selection = 0;
        main = bombermain;
        setPreferredSize(new Dimension(17 << 4, 17 << 4));
        setDoubleBuffered(true);
        MediaTracker mediatracker = new MediaTracker(this);
        try
        {
            int i = 0;
            mediatracker.addImage(backgroundImg, i++);
            for(int k = 0; k < 5; k++)
            {
                mediatracker.addImage(buttonImagesDown[k], i++);
                mediatracker.addImage(buttonImagesUp[k], i++);
            }

            mediatracker.waitForAll();
        }
        catch(Exception exception)
        {
            new ErrorDialog(exception);
        }
        imageButtons = new BomberImageButton[5];
        for(int j = 0; j < 5; j++)
        {
            Image aimage[] = {
                buttonImagesDown[j], buttonImagesUp[j]
            };
            imageButtons[j] = new BomberImageButton(this, aimage);
        }

        int l = buttonImagesDown[0].getHeight(this) / ((32 / 16) * 2);
        for(int i1 = 0; i1 <= 4; i1++)
        {
            imageButtons[i1].setInfo(0, 280 / (32 / 16) + l * i1, i1);
        }

        imageButtons[0].setBevel(true);
    }

    public void keyPressed(KeyEvent keyevent)
    {
        int i = selection;
        switch(keyevent.getKeyCode())
        {
        case 37: // '%'
        case 38: // '&'
            i--;
            break;

        case 39: // '\''
        case 40: // '('
            i++;
            break;

        case 10: // '\n'
            doCommand(selection);
            break;
        }
        if(selection != i)
        {
            if(i < 0)
            {
                i = 4;
            }
            imageButtons[selection].setBevel(false);
            selection = i;
            selection %= 5;
            imageButtons[selection].setBevel(true);
        }
    }

    @SuppressWarnings("deprecation")
	public void doCommand(int i)
    {
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
        case 1: // '\001'
        case 2: // '\002'
            main.newGame(selection + 2);
            break;

        case 3: // '\003'
            new BomberConfigDialog(main);
            break;

        case 4: // '\004'
            JOptionPane joptionpane = new JOptionPane("Are you sure you want to exit Bomberman?");
            joptionpane.setOptionType(0);
            joptionpane.setMessageType(2);
            javax.swing.JDialog jdialog = joptionpane.createDialog(this, "Exit Bomberman?");
            jdialog.setResizable(false);
            jdialog.show();
            Object obj = joptionpane.getValue();
            if(obj != null && obj.toString().equals("0"))
            {
                System.exit(0);
            }
            break;
        }
    }

    public void paint(Graphics g)
    {
        Graphics g1 = g;
        if(Main.J2)
        {
            paint2D(g);
        } else
        {
            g1.drawImage(backgroundImg, 0, 0, 17 << 4, 17 << 4, this);
        }
        for(int i = 0; i < 5; i++)
        {
            if(imageButtons[i] != null)
            {
                imageButtons[i].paint(g1);
            }
        }

    }

    public void paint2D(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setRenderingHints((RenderingHints)hints);
        graphics2d.drawImage(backgroundImg, 0, 0, 17 << 4, 17 << 4, this);
    }

    static 
    {
        buttonImagesDown = null;
        buttonImagesUp = null;
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
        
        //plaatjes laden BomberMenu
        buttonImagesDown = new Image[5];
        buttonImagesUp = new Image[5];
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        String s = BomberMain.RP + "src/Images/BomberMenu/";
        try
        {
            String s1 = s + "Background.jpg"; //achtergrond
            backgroundImg = toolkit.getImage((new File(s1)).getCanonicalPath());
            for(int i = 0; i < 5; i++)
            {
                if(i <= 2)
                {
                    s1 = s + (i + 2) + " Player Game";
                } else
                if(i == 3)
                {
                    s1 = s + "Control Setup";
                } else
                if(i == 4)
                {
                    s1 = s + "Exit";
                }
                buttonImagesDown[i] = toolkit.getImage((new File(s1 + " Down.gif")).getCanonicalPath());//geselecteerd of niet. Down is geen lichtwitgevend
                buttonImagesUp[i] = toolkit.getImage((new File(s1 + " Up.gif")).getCanonicalPath());//up is met een bommetje ervoor. lichtgevend. 
            }

        }
        catch(Exception exception)
        {
            new ErrorDialog(exception);
        }
    }
}

