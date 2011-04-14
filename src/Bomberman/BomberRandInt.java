package Bomberman;

//BombermanRandint randen om het spel heen 
public class BomberRandInt
{
	//rand grootte en hoogte en locatie geven
	private int low;
	private int high;
	private static double buffer[];

	public BomberRandInt(int i, int j)
	{
		low = 0;
		high = 0;
		low = i;
		high = j;
	}

	public int draw()
	{
		int i = low + (int)((double)((high - low) + 1) * nextRandom());
		return i;
	}

	private static double nextRandom()
	{
		int i = (int)(Math.random() * 101D);
		if(i == 101)
		{
			i = 100;
		}
		double d = buffer[i];
		buffer[i] = Math.random();
		return d;
	}

	static 
	{
		buffer = new double[101];
		for(int i = 0; i < 101; i++)
		{
			buffer[i] = Math.random();
		}

	}
}

