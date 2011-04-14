package Bomberman;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

public class MP3 {

	private String filename;
	private Player player;
	// Constructor die de naam van de MP3 bestand overneemt
	public MP3(String filename) 
	{
		this.filename = filename;
	}
	public void close() { if (player != null) player.close(); }
	// Speelt het MP3 bestand af
	public void play() {
		try 
		{
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new Player(bis);
		}
		catch (Exception e) {
			System.out.println("Probleem met het afspelen van" + filename);
			System.out.println(e);
		}
		// Een nieuwe thread voor het afspelen in de achtergrond
		new Thread()
		{
			public void run()
			{
				try { player.play(); 
				}
				catch (Exception e) 
				{ 
					System.out.println(e);
				}
			}
		}.start();
	}
	// test client
	public static void main(String[] args) {
		String filename = args[0];
		MP3 mp3 = new MP3(filename);
		mp3.play();
		// do whatever computation you like, while music plays
		int N = 4000;
		double sum = 0.0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sum += Math.sin(i + j);
			}
		}
		System.out.println(sum);
		// when the computation is done, stop playing it
		mp3.close();
		// play from the beginning
		mp3 = new MP3(filename);
		mp3.play();
	}
}