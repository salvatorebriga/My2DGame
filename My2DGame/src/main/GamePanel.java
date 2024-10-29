package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	// SCREEN SETTINGS
	// Scale the sprite for 3 and then build the window over the tileSize

	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;

	public final int tileSize = originalTileSize * scale; // 48 x 48 tile size
	public final int maxScreenCol = 26;
	public final int maxScreenRow = 16;
	public final int screenWidth = tileSize * maxScreenCol; // 768px
	public final int screenHeight = tileSize * maxScreenRow; // 420px

	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;

	int FPS = 60;

	TileManager tileM = new TileManager(this);
	KeyHandler keyH = new KeyHandler();
	Thread gameThread;
	public CollisionChecker cChecker = new CollisionChecker(this);
	public Player player = new Player(this, keyH);

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	/*
	 * @Override public void run() {
	 * 
	 * double drawInterval = 1000000000 / FPS; // 0.016666 SECS double nextDrawTime
	 * = System.nanoTime() + drawInterval;
	 * 
	 * while (gameThread != null) {
	 * 
	 * update();
	 * 
	 * repaint();
	 * 
	 * try {
	 * 
	 * double remainingTime = nextDrawTime - System.nanoTime();
	 * 
	 * remainingTime = remainingTime / 1000000;
	 * 
	 * if (remainingTime < 0) { remainingTime = 0; }
	 * 
	 * Thread.sleep((long) remainingTime);
	 * 
	 * nextDrawTime += drawInterval;
	 * 
	 * } catch (InterruptedException e) { e.printStackTrace(); }
	 * 
	 * } }
	 */

	public void run() {
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;

			timer += (currentTime - lastTime);

			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				System.out.println("FPS:" + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {
		player.update();
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		tileM.draw(g2);
		player.draw(g2);

		g2.dispose();
	}
}
