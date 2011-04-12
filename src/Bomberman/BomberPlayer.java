package Bomberman;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

public class BomberPlayer extends Thread
{

    public BomberSpel game;
    private BomberMap map;
    public boolean bombGrid[][];
    private BomberKeyQueue keyQueue;
    private boolean bombKeyDown;
    private byte dirKeysDown;
    private byte currentDirKeyDown;

    private boolean isExploding;
    private boolean isDead;
    private boolean keyPressed;
    private int keys[];
    public int totalBombs;
    public int usedBombs;
    public int fireLength;
    public boolean isActive;
    public int x;
    public int y;
    private int playerNo;
    private int state;
    private boolean moving;
    private int frame;
    private boolean clear;
    private static Image sprites[][][];
    private static Object hints = null;

    //Bomberplayer erft van BomberGame. het daadwerkelijke lopen en veranderen van plaatje bij BombnerPlayer. 
    public BomberPlayer(BomberSpel bombergame, BomberMap bombermap, int i)
    {
        game = null;
        map = null;
        bombGrid = null;
        keyQueue = null;
        bombKeyDown = false;
        dirKeysDown = 0;
        currentDirKeyDown = 0;
        isExploding = false;
        isDead = false;
        keyPressed = false;
        keys = null;
        totalBombs = 5;
        usedBombs = 0;
        fireLength = 2;
        isActive = true;
        x = 0;
        y = 0;
        playerNo = 0;
        state = 1;
        moving = false;
        frame = 0;
        clear = false;
        game = bombergame;
        map = bombermap;
        playerNo = i;
        bombGrid = new boolean[17][17];
        for(int j = 0; j < 17; j++)
        {
            for(int k = 0; k < 17; k++)
            {
                bombGrid[j][k] = false;
            }

        }

        byte byte0 = 0;
        byte byte1 = 0;
        switch(playerNo)
        {
        case 1: // '\001'
            byte0 = byte1 = 1;
            break;

        case 2: // '\002'
            byte0 = byte1 = 15;
            break;

        case 3: // '\003'
            byte0 = 15;
            byte1 = 1;
            break;

        case 4: // '\004'
            byte0 = 1;
            byte1 = 15;
            break;
        }
        x = byte0 << 4;
        y = byte1 << 4;
        MediaTracker mediatracker = new MediaTracker(bombergame);
        try
        {
            int l = 0;
            for(int j1 = 0; j1 < 4; j1++)
            {
                for(int k1 = 0; k1 < 5; k1++)
                {
                    for(int l1 = 0; l1 < 5; l1++)
                    {
                        mediatracker.addImage(sprites[j1][k1][l1], l++);
                    }

                }

            }

            mediatracker.waitForAll();
        }
        catch(Exception exception)
        {
            new ErrorDialog(exception);
        }
        keyQueue = new BomberKeyQueue();
        keys = new int[5];
        for(int i1 = 0; i1 <= 4; i1++)
        {
            keys[i1] = BomberKeyConfig.toetsen[i - 1][i1];
        }

        setPriority(10);
        start();
    }
//reageren op keyevents, keypressed. 
    public void keyPressed(KeyEvent keyevent)
    {
        byte byte0 = 0;
        if(!isExploding && !isDead && keyevent.getKeyCode() == keys[0] || keyevent.getKeyCode() == keys[1] || keyevent.getKeyCode() == keys[2] || keyevent.getKeyCode() == keys[3])
        {
            if(keyevent.getKeyCode() == keys[1])
            {
                byte0 = 2;
                if((currentDirKeyDown & 1) > 0 || (currentDirKeyDown & 4) == 0 && (currentDirKeyDown & 8) == 0)
                {
                    currentDirKeyDown = 2;
                }
            } else
            if(keyevent.getKeyCode() == keys[0])
            {
                byte0 = 1;
                if((currentDirKeyDown & 2) > 0 || (currentDirKeyDown & 4) == 0 && (currentDirKeyDown & 8) == 0)
                {
                    currentDirKeyDown = 1;
                }
            } else
            if(keyevent.getKeyCode() == keys[2])
            {
                byte0 = 4;
                if((currentDirKeyDown & 8) > 0 || (currentDirKeyDown & 1) == 0 && (currentDirKeyDown & 2) == 0)
                {
                    currentDirKeyDown = 4;
                }
            } else
            if(keyevent.getKeyCode() == keys[3])
            {
                byte0 = 8;
                if((currentDirKeyDown & 4) > 0 || (currentDirKeyDown & 1) == 0 && (currentDirKeyDown & 2) == 0)
                {
                    currentDirKeyDown = 8;
                }
            }
            if(!keyQueue.contains(byte0))
            {
                keyQueue.push(byte0);
                dirKeysDown |= byte0;
                keyPressed = true;
                interrupt();
            }
        }
        if(!isExploding && !isDead && keyevent.getKeyCode() == keys[4] && !bombKeyDown && isActive)
        {
            bombKeyDown = true;
            interrupt();
        }
    }
//KeyReleased event. 
    public void keyReleased(KeyEvent keyevent)
    {
        if(!isExploding && !isDead && (keyevent.getKeyCode() == keys[0] || keyevent.getKeyCode() == keys[1] || keyevent.getKeyCode() == keys[2] || keyevent.getKeyCode() == keys[3]))
        {
            if(keyevent.getKeyCode() == keys[1])
            {
                dirKeysDown ^= 2;
                currentDirKeyDown ^= 2;
                keyQueue.verwijderItems((byte)2);
            } else
            if(keyevent.getKeyCode() == keys[0])
            {
                dirKeysDown ^= 1;
                currentDirKeyDown ^= 1;
                keyQueue.verwijderItems((byte)1);
            } else
            if(keyevent.getKeyCode() == keys[2])
            {
                dirKeysDown ^= 4;
                currentDirKeyDown ^= 4;
                keyQueue.verwijderItems((byte)4);
            } else
            if(keyevent.getKeyCode() == keys[3])
            {
                dirKeysDown ^= 8;
                currentDirKeyDown ^= 8;
                keyQueue.verwijderItems((byte)8);
            }
            if(currentDirKeyDown == 0)
            {
                boolean flag;
                for(flag = false; !flag && keyQueue.grootte() > 0;)
                {
                    if((keyQueue.haalLaatsteItemOp() & dirKeysDown) > 0)
                    {
                        currentDirKeyDown = keyQueue.haalLaatsteItemOp();
                        flag = true;
                    } else
                    {
                        keyQueue.pop();
                    }
                }

                if(!flag)
                {
                    keyQueue.verwijderAlles();
                    currentDirKeyDown = 0;
                    dirKeysDown = 0;
                    keyPressed = false;
                    interrupt();
                }
            }
        }
        if(!isExploding && !isDead && keyevent.getKeyCode() == keys[4])
        {
            bombKeyDown = false;
            interrupt();
        }
    }
//Deactiveren speler als deze is overleden. 
    public void deactivate()
    {
        isActive = false;
    }

    public void kill()
    {
        if(!isDead && !isExploding)
        {
            BomberSpel.spelerLinks--;
            frame = 0;
            state = 4;
            moving = true;
            isExploding = true;
            keyPressed = false;
            BomberMain.sndEffectSpeler.speelmuziek("Die");
            interrupt();
        }
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
//als de speler geraakt is gaat hij over op exploderen.
    public boolean isDead()
    {
        return isDead | isExploding;
    }

    public void run()
    {
        boolean flag5 = false;
        byte byte0 = 4;
        byte byte1 = 4;
        byte byte2 = 16;
        byte byte3 = 8;
        int i = 0;
        int j = 0;
        do
        {
            if(!isExploding && !isDead && bombKeyDown && isActive && totalBombs - usedBombs > 0 && map.rooster[x >> byte0][y >> byte0] != 3 && !bombGrid[x + byte3 >> 4][y + byte3 >> 4])
            {
                usedBombs++;
                bombGrid[x + byte3 >> 4][y + byte3 >> 4] = true;
                map.createBomb(x + byte3, y + byte3, playerNo);
            }
            if(!isExploding && !isDead && keyPressed)
            {
                flag5 = keyPressed;
                frame = (frame + 1) % 5;
                moving = true;
                if(dirKeysDown > 0)
                {
                    if((currentDirKeyDown & 4) > 0)
                    {
                        state = 2;
                        boolean flag1 = x % byte2 != 0 || y % byte2 == 0 && map.rooster[(x >> byte0) - 1][y >> byte0] <= -1;
                        if(!flag1)
                        {
                            int k = 0;
                            for(k = -byte1; k < 0; k += byte2 / 4)
                            {
                                if((y + k) % byte2 != 0 || map.rooster[(x >> byte0) - 1][y + k >> byte0] > -1)
                                {
                                    continue;
                                }
                                flag1 = true;
                                break;
                            }

                            if(!flag1)
                            {
                                for(k = byte2 / 4; k <= byte1; k += byte2 / 4)
                                {
                                    if((y + k) % byte2 != 0 || map.rooster[(x >> byte0) - 1][y + k >> byte0] > -1)
                                    {
                                        continue;
                                    }
                                    flag1 = true;
                                    break;
                                }

                            }
                            if(flag1)
                            {
                                clear = true;
                                game.paintImmediately(x, y - byte3, 16, 22);
                                y += k;
                                clear = false;
                                game.paintImmediately(x, y - byte3, 16, 22);
                            }
                        }
                        if(flag1)
                        {
                            clear = true;
                            game.paintImmediately(x, y - byte3, 16, 22);
                            x -= byte2 / 4;
                            clear = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        } else
                        {
                            moving = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        }
                    } else
                    if((currentDirKeyDown & 8) > 0)
                    {
                        state = 3;
                        boolean flag2 = false;
                        flag2 = x % byte2 != 0 || y % byte2 == 0 && map.rooster[(x >> byte0) + 1][y >> byte0] <= -1;
                        if(!flag2)
                        {
                            int l = 0;
                            for(l = -byte1; l < 0; l += byte2 / 4)
                            {
                                if((y + l) % byte2 != 0 || map.rooster[(x >> byte0) + 1][y + l >> byte0] > -1)
                                {
                                    continue;
                                }
                                flag2 = true;
                                break;
                            }

                            if(!flag2)
                            {
                                for(l = byte2 / 4; l <= byte1; l += byte2 / 4)
                                {
                                    if((y + l) % byte2 != 0 || map.rooster[(x >> byte0) + 1][y + l >> byte0] > -1)
                                    {
                                        continue;
                                    }
                                    flag2 = true;
                                    break;
                                }

                            }
                            if(flag2)
                            {
                                clear = true;
                                game.paintImmediately(x, y - byte3, 16, 22);
                                y += l;
                                clear = false;
                                game.paintImmediately(x, y - byte3, 16, 22);
                            }
                        }
                        if(flag2)
                        {
                            clear = true;
                            game.paintImmediately(x, y - byte3, 16, 22);
                            x += byte2 / 4;
                            clear = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        } else
                        {
                            moving = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        }
                    } else
                    if((currentDirKeyDown & 1) > 0)
                    {
                        state = 0;
                        boolean flag3 = false;
                        flag3 = y % byte2 != 0 || x % byte2 == 0 && map.rooster[x >> byte0][(y >> byte0) - 1] <= -1;
                        if(!flag3)
                        {
                            int i1 = 0;
                            for(i1 = -byte1; i1 < 0; i1 += byte2 / 4)
                            {
                                if((x + i1) % byte2 != 0 || map.rooster[x + i1 >> byte0][(y >> byte0) - 1] > -1)
                                {
                                    continue;
                                }
                                flag3 = true;
                                break;
                            }

                            if(!flag3)
                            {
                                for(i1 = byte2 / 4; i1 <= byte1; i1 += byte2 / 4)
                                {
                                    if((x + i1) % byte2 != 0 || map.rooster[x + i1 >> byte0][(y >> byte0) - 1] > -1)
                                    {
                                        continue;
                                    }
                                    flag3 = true;
                                    break;
                                }

                            }
                            if(flag3)
                            {
                                clear = true;
                                game.paintImmediately(x, y - byte3, 16, 22);
                                x += i1;
                                clear = false;
                                game.paintImmediately(x, y - byte3, 16, 22);
                            }
                        }
                        if(flag3)
                        {
                            clear = true;
                            game.paintImmediately(x, y - byte3, 16, 22);
                            y -= byte2 / 4;
                            clear = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        } else
                        {
                            moving = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        }
                    } else
                    if((currentDirKeyDown & 2) > 0)
                    {
                        state = 1;
                        boolean flag4 = false;
                        flag4 = y % byte2 != 0 || x % byte2 == 0 && map.rooster[x >> byte0][(y >> byte0) + 1] <= -1;
                        if(!flag4)
                        {
                            int j1 = 0;
                            for(j1 = -byte1; j1 < 0; j1 += byte2 / 4)
                            {
                                if((x + j1) % byte2 != 0 || map.rooster[x + j1 >> byte0][(y >> byte0) + 1] > -1)
                                {
                                    continue;
                                }
                                flag4 = true;
                                break;
                            }

                            if(!flag4)
                            {
                                for(j1 = byte2 / 4; j1 <= byte1; j1 += byte2 / 4)
                                {
                                    if((x + j1) % byte2 != 0 || map.rooster[x + j1 >> byte0][(y >> byte0) + 1] > -1)
                                    {
                                        continue;
                                    }
                                    flag4 = true;
                                    break;
                                }

                            }
                            if(flag4)
                            {
                                clear = true;
                                game.paintImmediately(x, y - byte3, 16, 22);
                                x += j1;
                                clear = false;
                                game.paintImmediately(x, y - byte3, 16, 22);
                            }
                        }
                        if(flag4)
                        {
                            clear = true;
                            game.paintImmediately(x, y - byte3, 16, 22);
                            y += byte2 / 4;
                            clear = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        } else
                        {
                            moving = false;
                            game.paintImmediately(x, y - byte3, 16, 22);
                        }
                    }
                }
            } else
            if(!isExploding && !isDead && flag5 != keyPressed)
            {
                frame = 0;
                moving = false;
                game.paintImmediately(x, y - byte3, 16, 22);
                flag5 = keyPressed;
            } else
            if(!isDead && isExploding)
            {
                if(frame >= 4)
                {
                    isDead = true;
                }
                game.paintImmediately(x, y - byte3, 16, 22);
                frame = (frame + 1) % 5;
            } else
            if(isDead)
            {
                clear = true;
                game.paintImmediately(x, y - byte3, 16, 22);
                break;
            }
            if(map.bonusRooster[x >> byte0][y >> byte0] != null)
            {
                i = x;
                j = y;
            } else
            if(map.bonusRooster[x >> byte0][y + byte3 >> byte0] != null)
            {
                i = x;
                j = y + byte3;
            } else
            if(map.bonusRooster[x + byte3 >> byte0][y >> byte0] != null)
            {
                i = x + byte3;
                j = y;
            }
            if(i != 0 && j != 0)
            {
                map.bonusRooster[i >> byte0][j >> byte0].giveToPlayer(playerNo);
                i = j = 0;
            }
            if(isDead)
            {
                break;
            }
            try
            {
                Thread.sleep(65L);
            }
            catch(Exception exception) { }
        } while(true);
        interrupt();
    }

    public void paint(Graphics g)
    {
        Graphics g1 = g;
        if(Main.J2)
        {
            paint2D(g);
        } else
        if(!isDead && !clear)
        {
            if(moving)
            {
            	//was g1.drawImage(sprites[playerNo - 1][state][frame], x, y - 8, 16, 22, null);
                g1.drawImage(sprites[playerNo - 1][state][frame], x, y - 8, 16, 22, null);
            } else
            {
            	//was g1.drawImage(sprites[playerNo - 1][state][frame], x, y - 8, 16, 22, null);
                g1.drawImage(sprites[playerNo - 1][state][0], x, y - 8, 16, 22, null);
            }
        }
    }

    public void paint2D(Graphics g)
    {
        Graphics2D graphics2d = (Graphics2D)g;
        graphics2d.setRenderingHints((RenderingHints)hints);
        if(!isDead && !clear)
        {
            if(moving)
            {
            	//was graphics2d.drawImage(sprites[playerNo - 1][state][frame], x, y - 8, 16, 22, null);
                graphics2d.drawImage(sprites[playerNo - 1][state][frame], x, y - 8, 16, 22, null);
            } else
            {
            	//was graphics2d.drawImage(sprites[playerNo - 1][state][0], x, y - 8, 16, 22, null);
                graphics2d.drawImage(sprites[playerNo - 1][state][0], x, y - 8, 16, 22, null);
            }
        }
    }

    static 
    {
        sprites = null;
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
        sprites = new Image[4][5][5];
        int ai[] = {
            0, 1, 2, 3, 4
        };
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        //bombermans images 
        try
        {
            for(int i = 0; i < 4; i++)
            {
                for(int j = 0; j < 5; j++)
                {
                    for(int k = 0; k < 5; k++)
                    {
                        String s1 = BomberMain.RP + "src/Images/";
                        s1 = s1 + "Bombermans/Player " + (i + 1) + "/";
                        s1 = s1 + ai[j] + "" + (k + 1) + ".gif";
                        sprites[i][j][k] = toolkit.getImage((new File(s1)).getCanonicalPath());
                    }

                }

            }

        }
        catch(Exception exception)
        {
            new ErrorDialog(exception);
        }
    }
}
