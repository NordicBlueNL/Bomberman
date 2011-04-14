package Bomberman;

import java.awt.*;
import java.io.File;
import java.util.Vector;
import javax.swing.*;
//BomberMap, de Map voor aan het begin. en alle plaatjes bijhoud in het spel. waar wat staat en welke plaatjes waar gebruikt worden

public class BomberMap extends JPanel
{
	private class Bonus
	{

		public int r;
		public int c;

		public Bonus(int i, int j)
		{
			r = 0;
			c = 0;
			r = i >> 4;
			c = j >> 4;
		}
	}

	private class Bomb
	{

		public int r;
		public int c;

		public Bomb(int i, int j)
		{
			r = 0;
			c = 0;
			r = i >> 4;
			c = j >> 4;
		}
	}


	//private BomberMain main;
	private boolean gameOver;
	private Color backgroundColor;
	public int rooster[][];
	public boolean fireRooster[][];
	public BomberBommen bombGrid[][];
	public BomberBonus bonusRooster[][];
	private Vector bombs;
	private Vector bonuses;
	private static Image mapImages[][];
	public static Image bombPlaatjes[];
	public static Image fireImages[][];
	public static Image fireBrickImages[][];
	public static Image bonusImages[][];
	public static final int FIRE_CENTER = 0;
	public static final int FIRE_VERTICAL = 1;
	public static final int FIRE_HORIZONTAL = 2;
	public static final int FIRE_NORTH = 3;
	public static final int FIRE_SOUTH = 4;
	public static final int FIRE_EAST = 5;
	public static final int FIRE_WEST = 6;
	public static final int FIRE_BRICK = 7;
	public static final int BONUS_FIRE = -4;
	public static final int BONUS_BOMB = -3;
	public static final int NOTHING = -1;
	public static final int WALL = 0;
	public static final int BRICK = 1;
	public static final int BOMB = 3;
	private static BomberRandInt levelRand = null;
	private static BomberRandInt bonusRand = null;
	public static int level = 0;
	private static Object hints = null;

	//Bombermap erft van BomberMain
	public BomberMap(BomberMain bombermain)
	{
		gameOver = false;
		backgroundColor = null;
		rooster = null;
		fireRooster = null;
		bombGrid = null;
		bonusRooster = null;
		bombs = null;
		bonuses = null;
		level = levelRand.draw() % 2;
		MediaTracker mediatracker = new MediaTracker(this);
		//Waar staan de images van de map/vuurstenen die ontploffen kunnen 
		try
		{
			int i = 0;
			for(int k = 0; k < 2; k++)
			{
				for(int i1 = 0; i1 < 3; i1++)
				{
					if(mapImages[k][i1] != null)
					{
						mediatracker.addImage(mapImages[k][i1], i++);
					}
				}

			}

			for(int k1 = 0; k1 < 2; k1++)
			{
				mediatracker.addImage(bombPlaatjes[k1], i++);
			}

			for(int i2 = 0; i2 < 8; i2++)
			{
				fireImages[7][i2] = fireBrickImages[level][i2];
			}

			for(int j2 = 0; j2 < 8; j2++)
			{
				for(int l2 = 0; l2 < 8; l2++)
				{
					mediatracker.addImage(fireImages[j2][l2], i++);
				}

			}

			mediatracker.waitForAll();
		}
		catch(Exception exception)
		{
			new ErrorDialog(exception);
		}
		bombs = new Vector();
		bonuses = new Vector();
		fireRooster = new boolean[17][17];
		bombGrid = new BomberBommen[17][17];
		bonusRooster = new BomberBonus[17][17];
		rooster = new int[17][17];
		for(int j = 0; j < 17; j++)
		{
			for(int l = 0; l < 17; l++)
			{
				if(j == 0 || l == 0 || j == 16 || l == 16)
				{
					rooster[j][l] = 0;
				} else
					if((j & 1) == 0 && (l & 1) == 0)
					{
						rooster[j][l] = 0;
					} else
					{
						rooster[j][l] = -1;
					}
				fireRooster[j][l] = false;
				bombGrid[j][l] = null;
				bonusRooster[j][l] = null;
			}

		}

		BomberRandInt bomberrandint = new BomberRandInt(1, 15);
		for(int k2 = 0; k2 < 384; k2++)
		{
			int j1 = bomberrandint.draw();
			int l1 = bomberrandint.draw();
			if(rooster[j1][l1] == -1)
			{
				rooster[j1][l1] = 1;
			}
		}

		rooster[1][1] = rooster[2][1] = rooster[1][2] = rooster[1][15] = rooster[2][15] = rooster[1][14] = rooster[15][1] = rooster[14][1] = rooster[15][2] = rooster[15][15] = rooster[15][14] = rooster[14][15] = -1;
		//achtergrondkleur
		backgroundColor = new Color(52, 108, 108);
		//was setPreferredSize(new Dimension(272, 272));
		setPreferredSize(new Dimension(272, 272));
		setDoubleBuffered(true);
		setBounds(0, 0, 17 << 4, 17 << 4);
		setOpaque(false);
		bombermain.getLayeredPane().add(this, 1000);
	}

	public void setGameOver()
	{
		gameOver = true;
		//	paintImmediately(0, 0, 272, 272);
		paintImmediately(0, 0, 272, 272);
	}

	public synchronized void createBonus(int i, int j)
	{
		int k = (i >> 4) << 4;
		int l = (j >> 4) << 4;
		int i1 = bonusRand.draw();
		if(i1 == 0 || i1 == 1)
		{
			bonusRooster[k >> 4][l >> 4] = new BomberBonus(this, k, l, i1);
			bonuses.addElement(new Bonus(k, l));
		}
	}

	public synchronized void removeBonus(int i, int j)
	{
		int k = 0;
		int l = bonuses.size();
		int i1 = i >> 4;
		int j1 = j >> 4;
		Object obj = null;
		for(; k < l; l = bonuses.size())
		{
			Bonus bonus = (Bonus)bonuses.elementAt(k);
			if(bonus.r == i1 && bonus.c == j1)
			{
				bonuses.removeElementAt(k);
				bonusRooster[bonus.r][bonus.c].kill();
				bonusRooster[bonus.r][bonus.c] = null;
				//was paintImmediately(bonus.r << 4, bonus.c << 4, 16, 16);
				paintImmediately(bonus.r << 4, bonus.c << 4, 16, 16);
				break;
			}
			k++;
		}

	}

	public synchronized void createBomb(int i, int j, int k)
	{
		int l = (i >> 4) << 4;
		int i1 = (j >> 4) << 4;
		bombGrid[l >> 4][i1 >> 4] = new BomberBommen(this, l, i1, k);
		bombs.addElement(new Bomb(l, i1));
	}

	public synchronized void removeBomb(int i, int j)
	{
		int k = 0;
		int l = bombs.size();
		int i1 = i >> 4;
		int j1 = j >> 4;
			Object obj = null;
			for(; k < l; l = bombs.size())
			{
				Bomb bomb = (Bomb)bombs.elementAt(k);
				if((bomb.r == i1) & (bomb.c == j1))
				{
					bombs.removeElementAt(k);
					break;
				}
				k++;
			}

	}

	public void createFire(int i, int j, int k, int l)
	{
		int i1 = (i >> 4) << 4;
		int j1 = (j >> 4) << 4;
		boolean flag = false;
		if(rooster[i1 >> 4][j1 >> 4] == 3)
		{
			if(bombGrid[i1 >> 4][j1 >> 4] != null)
			{
				bombGrid[i1 >> 4][j1 >> 4].shortBomb();
			}
		} else
			if(!fireRooster[i1 >> 4][j1 >> 4])
			{
				flag = true;
				BomberVuur bomberfire = new BomberVuur(this, i1, j1, l);
			}
		if(flag && l == 0)
		{
			byte byte0 = 4;
			byte byte1 = 16;
			int k1 = 0;
			int l1 = 0;
			int i2 = 0;
			int j2 = 0;
			int k2 = 0;
			int l2 = 0;
			int i3 = 0;
			int j3 = 0;
			for(int k3 = 1; k3 <= BomberSpel.spelers[k].fireLength; k3++)
			{
				if(l1 == 0 && (j1 >> byte0) + k3 < 17)
				{
					if(rooster[i1 >> byte0][(j1 >> byte0) + k3] != 0)
					{
						if(rooster[i1 >> byte0][(j1 >> byte0) + k3] != -1)
						{
							l1 = rooster[i1 >> byte0][(j1 >> byte0) + k3];
						}
						l2++;
					} else
					{
						l1 = -1;
					}
				}
				if(k1 == 0 && (j1 >> byte0) - 1 >= 0)
				{
					if(rooster[i1 >> byte0][(j1 >> byte0) - k3] != 0)
					{
						if(rooster[i1 >> byte0][(j1 >> byte0) - k3] != -1)
						{
							k1 = rooster[i1 >> byte0][(j1 >> byte0) - k3];
						}
						k2++;
					} else
					{
						k1 = -1;
					}
				}
				if(j2 == 0 && (i1 >> byte0) + k3 < 17)
				{
					if(rooster[(i1 >> byte0) + k3][j1 >> byte0] != 0)
					{
						if(rooster[(i1 >> byte0) + k3][j1 >> byte0] != -1)
						{
							j2 = rooster[(i1 >> byte0) + k3][j1 >> byte0];
						}
						j3++;
					} else
					{
						j2 = -1;
					}
				}
				if(i2 == 0 && (i1 >> byte0) - k3 >= 0)
				{
					if(rooster[(i1 >> byte0) - k3][j1 >> byte0] != 0)
					{
						if(rooster[(i1 >> byte0) - k3][j1 >> byte0] != -1)
						{
							i2 = rooster[(i1 >> byte0) - k3][j1 >> byte0];
						}
						i3++;
					} else
					{
						i2 = -1;
					}
				}
			}

			for(int l3 = 1; l3 <= k2; l3++)
			{
				if(l3 == k2)
				{
					if(k1 == 1)
					{
						createFire(i1, j1 - l3 * byte1, k, 7);
					} else
					{
						createFire(i1, j1 - l3 * byte1, k, 3);
					}
				} else
				{
					createFire(i1, j1 - l3 * byte1, k, 1);
				}
			}

			for(int i4 = 1; i4 <= l2; i4++)
			{
				if(i4 == l2)
				{
					if(l1 == 1)
					{
						createFire(i1, j1 + i4 * byte1, k, 7);
					} else
					{
						createFire(i1, j1 + i4 * byte1, k, 4);
					}
				} else
				{
					createFire(i1, j1 + i4 * byte1, k, 1);
				}
			}

			for(int j4 = 1; j4 <= j3; j4++)
			{
				if(j4 == j3)
				{
					if(j2 == 1)
					{
						createFire(i1 + j4 * byte1, j1, k, 7);
					} else
					{
						createFire(i1 + j4 * byte1, j1, k, 5);
					}
				} else
				{
					createFire(i1 + j4 * byte1, j1, k, 2);
				}
			}

			for(int k4 = 1; k4 <= i3; k4++)
			{
				if(k4 == i3)
				{
					if(i2 == 1)
					{
						createFire(i1 - k4 * byte1, j1, k, 7);
					} else
					{
						createFire(i1 - k4 * byte1, j1, k, 6);
					}
				} else
				{
					createFire(i1 - k4 * byte1, j1, k, 2);
				}
			}

		}
	}

	public synchronized void paint(Graphics g)
	{
		Graphics g1 = g;
		if(Main.J2)
		{
			paint2D(g);
		} else
			if(gameOver)
			{
				g1.setColor(Color.black);
				//was g1.fillRect(0,0,272,272)
				g1.fillRect(0, 0, 272, 272);
			} else
			{
				g1.setColor(backgroundColor);
				//was g1.fillRect(0, 0, 272, 272);
				g1.fillRect(0, 0, 272, 272);
				for(int i = 0; i < 17; i++)
				{
					for(int j = 0; j < 17; j++)
					{
						if(rooster[i][j] > -1 && rooster[i][j] != 3 && rooster[i][j] != 7 && mapImages[level][rooster[i][j]] != null)
						{
							// was g1.drawImage(mapImages[level][rooster[i][j]], i << 4, j << 4, 16, 16, null);
							g1.drawImage(mapImages[level][rooster[i][j]], i << 4, j << 4, 16, 16, null);
						} else
							if(mapImages[level][2] != null)
							{
								//was g1.drawImage(mapImages[level][2], i << 4, j << 4, 16, 16, null);
								g1.drawImage(mapImages[level][2], i << 4, j << 4, 16, 16, null);
							}
					}

				}

			}
		if(!gameOver)
		{
			Object obj = null;
			int k = 0;
			for(int l = bonuses.size(); k < l; l = bonuses.size())
			{
				Bonus bonus = (Bonus)bonuses.elementAt(k);
				if(bonusRooster[bonus.r][bonus.c] != null)
				{
					bonusRooster[bonus.r][bonus.c].paint(g1);
				}
				k++;
			}

			Object obj1 = null;
			k = 0;
			for(int i1 = bombs.size(); k < i1; i1 = bombs.size())
			{
				Bomb bomb = (Bomb)bombs.elementAt(k);
				if(bombGrid[bomb.r][bomb.c] != null)
				{
					bombGrid[bomb.r][bomb.c].paint(g1);
				}
				k++;
			}

		}
	}

	public void paint2D(Graphics g)
	{
		Graphics2D graphics2d = (Graphics2D)g;
		graphics2d.setRenderingHints((RenderingHints)hints);
		if(gameOver)
		{
			graphics2d.setColor(Color.black);

			//was graphics2d.fillRect(0, 0, 272, 272);
			graphics2d.fillRect(0, 0, 272, 272);
		} else
		{
			graphics2d.setColor(backgroundColor);
			//was graphics2d.fillRect(0, 0, 272, 272);
			graphics2d.fillRect(0, 0, 272, 272);
			for(int i = 0; i < 17; i++)
			{
				for(int j = 0; j < 17; j++)
				{
					if(rooster[i][j] > -1 && rooster[i][j] != 3 && rooster[i][j] != 7 && mapImages[level][rooster[i][j]] != null)
					{
						//was graphics2d.drawImage(mapImages[level][rooster[i][j]], i << 4, j << 4, 16, 16, null);
						graphics2d.drawImage(mapImages[level][rooster[i][j]], i << 4, j << 4, 16, 16, null);
					} else
						if(mapImages[level][2] != null)
						{
							//was graphics2d.drawImage(mapImages[level][2], i << 4, j << 4, 16, 16, null);
							graphics2d.drawImage(mapImages[level][2], i << 4, j << 4, 16, 16, null);
						}
				}

			}

		}
	}

	static 
	{
		mapImages = null;
		bombPlaatjes = null;
		fireImages = null;
		fireBrickImages = null;
		bonusImages = null;
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
		//was levelRand = new BomberRandInt(0,100);
		levelRand = new BomberRandInt(0, 100);

		//was bonusRand = new BomberRandInt(0,7);
		bonusRand = new BomberRandInt(0, 7);

		mapImages = new Image[3][3];
		bombPlaatjes = new Image[2];
		fireImages = new Image[8][8];
		fireBrickImages = new Image[3][8];
		bonusImages = new Image[2][2];
		try
		{
			String as[] = new String[3];
			for(int i = 0; i < 2; i++)
			{
				as[0] = BomberMain.RP + "src/Images/BomberWalls/" + (i + 1);
				as[1] = BomberMain.RP + "src/Images/BomberBricks/" + (i + 1);
				as[2] = BomberMain.RP + "src/Images/BomberFloors/" + (i + 1);
				for(int j = 0; j < 3; j++)
				{
					if(i == 0)
					{
						as[j] += ".jpg";
					} else
					{
						as[j] += ".gif";
					}
				}

				mapImages[i][0] = Toolkit.getDefaultToolkit().getImage((new File(as[0])).getCanonicalPath());
				mapImages[i][1] = Toolkit.getDefaultToolkit().getImage((new File(as[1])).getCanonicalPath());
				if(i == 0)
				{
					mapImages[i][2] = null;
				} else
				{
					mapImages[i][2] = Toolkit.getDefaultToolkit().getImage((new File(as[2])).getCanonicalPath());
				}
			}

			Object obj = null;
			for(int k = 0; k < 2; k++)
			{
				String s = BomberMain.RP + "src/Images/BomberBombs/" + (k + 1) + ".gif";
				bombPlaatjes[k] = Toolkit.getDefaultToolkit().getImage((new File(s)).getCanonicalPath());
			}

			for(int l = 0; l < 7; l++)
			{
				for(int i1 = 0; i1 < 8; i1++)
				{
					String s1 = BomberMain.RP + "src/Images/BomberFires/";
					if(l == 0)
					{
						s1 = s1 + "C";
					} else
						if(l == 1)
						{
							s1 = s1 + "V";
						} else
							if(l == 3)
							{
								s1 = s1 + "N";
							} else
								if(l == 2)
								{
									s1 = s1 + "H";
								} else
									if(l == 5)
									{
										s1 = s1 + "E";
									} else
										if(l == 6)
										{
											s1 = s1 + "W";
										} else
											if(l == 4)
											{
												s1 = s1 + "S";
											}
					if(l == 7)
					{
						fireImages[l][i1] = null;
					} else
					{
						s1 = s1 + (i1 + 1) + ".gif";
						fireImages[l][i1] = Toolkit.getDefaultToolkit().getImage((new File(s1)).getCanonicalPath());
					}
				}

			}

			boolean flag = false;
			for(int l1 = 0; l1 < 2; l1++)
			{
				for(int j1 = 0; j1 < 8; j1++)
				{
					String s2 = BomberMain.RP + "src/Images/BomberFireBricks/" + (l1 + 1) + (j1 + 1) + ".gif";
					fireBrickImages[l1][j1] = Toolkit.getDefaultToolkit().getImage((new File(s2)).getCanonicalPath());
				}

			}

			for(int i2 = 0; i2 < 2; i2++)
			{
				for(int k1 = 0; k1 < 2; k1++)
				{
					String s3 = BomberMain.RP + "src/Images/BomberBonuses/" + (i2 != 0 ? "B" : "F") + (k1 + 1) + ".gif";
					bonusImages[i2][k1] = Toolkit.getDefaultToolkit().getImage((new File(s3)).getCanonicalPath());
				}

			}

		}
		catch(Exception exception)
		{
			new ErrorDialog(exception);
		}
	}
}
