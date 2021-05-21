import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.InterruptedException;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;
import java.util.Scanner;
import java.awt.Dimension;
import java.awt.Toolkit;
//Threading

public class Fireworks extends Canvas implements Runnable{
	public static final String TITLE = "Fireworks";
	public static int WIDTH = 1280;
	public static int HEIGTH = WIDTH/16*9;
	private boolean running;
	private Firework[] points = new Firework[7];
	
	private void start(){
		if(running) 
			return;
		running = true;
		new Thread(this,"Main-Thread").start();
	}
	
	private void tick(){
		for(int i = 0 ; i < points.length ; i++){
			if(points[i]!=null){
				points[i].tick();
				if(!(points[i].getActive())){
					points[i] = new Firework((int)(Math.random()*WIDTH), (int)(Math.random()*HEIGTH),(int)(Math.random()*100),new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
				}
			}
		}
	}
	
	private void render(){
		BufferStrategy bs = getBufferStrategy();	//layers of frames, display frames from buffer
		
		if(bs==null){
			createBufferStrategy(3);  //trippe buffer
			return;
		}
		
		Graphics g = bs.getDrawGraphics();  //displaying from buffer
		
		//////////////////////////////////////time to paint the game
		
		//draw background
		g.setColor(Color.BLACK);
		g.fillRect(0,0,WIDTH,HEIGTH); 
		//System.out.println("Background added");
		
		//draw tree
		for(Firework items : points){
			if(items != null){
				items.draw(g);					
			}
		}
		
		//System.out.println("Tree length: "+tree.length);
		//////////////////////////////////////end of paint
		g.dispose();
		bs.show();
		//new Scanner(System.in).next();
	}
	
	public void run(){
		double target = 60.0; //ticks or frames per second
		double nsPerTick = 1000000000.0 / target;//nano sec per tick
		long lastTime = System.nanoTime();//start Time
		long timer = System.currentTimeMillis();
		double unprocessed = 0.0;
		int fps = 0;
		int tps = 0;
		boolean canRender = false;  //limits ticks
		System.out.println("Running...");
		//generate firepoints
		for(int i = 0 ; i < points.length; i++){
			points[i] = new Firework((int)(Math.random()*WIDTH), (int)(Math.random()*HEIGTH),(int)(Math.random()*100),new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)));
		}
		while(running){
			long now = System.nanoTime();
			unprocessed += (now - lastTime)/nsPerTick;
			lastTime = now;
			
			if(unprocessed>= 1.0){
				tick();
				unprocessed--;
				tps++;
				canRender=true; //we may now render, thanks to limmit
			}
			else
				canRender = false;
			
			try{
				Thread.sleep(1);
			}
			catch(InterruptedException e){
			}
			
			if(canRender){
				render();
				fps++;
				
			}
			
			//calculate fps tps
			if(System.currentTimeMillis() - 1000 > timer){
				timer += 1000;
				System.out.printf("FPS: %d | TPS: %d\n",fps,tps);
				fps=0;
				tps=0;
			}
		}
		stop();
	}
	
	private void stop(){
		if(!running) return;
		running = false;
		System.exit(0);
	}
	
	public static void main(String[] args){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH = (int)screenSize.getWidth();
		HEIGTH = (int)screenSize.getHeight();
		final Fireworks game = new Fireworks();
		JFrame frame = new JFrame(TITLE);
		frame.add(game);
		frame.setSize(WIDTH,HEIGTH);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
			public void windowsClosing(WindowEvent e){
				System.err.println(TITLE+" stopping");
				game.stop();
			}
		});
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);	
		frame.requestFocus();	
		game.start();
	} 
}
