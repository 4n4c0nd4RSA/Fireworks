import java.awt.Color;
import java.awt.Graphics;

public class Firework{
	private FirePoint[] points = new FirePoint[1500];
	private int x;
	private int y;
	private boolean active=true;
	private boolean test;
	private int fuse;
	private Color color;
	
	public Firework(){
		this(0,0,(int)(100*Math.random()),Color.WHITE);
	}
		
	public Firework(int x, int y, int fuse, Color color){
		this.x = x;
		this.y = y;
		this.fuse = fuse;
		for(int i = 0 ; i < points.length; i++){
			points[i] = new FirePoint(color,x,y);
		}
		this.color = color;
	}
	
	public boolean getActive(){
		return active;
	}
	
	public void tick(){
		test = false;
		fuse -= 1;
		if(!(fuse>0)){
			for(FirePoint item : points){
				item.tick();
				if(item.getFuel()>0.001){
					test = true;
				}
			}
			if(!test){
				active = false;
			}
		}
	}
	
	public void draw(Graphics g){		
		if(!(fuse > 0)){
			for(FirePoint item : points){
				item.draw(g);
			}
		}
	}
}
