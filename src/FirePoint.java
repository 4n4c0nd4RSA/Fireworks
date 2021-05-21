import java.awt.Color;
import java.awt.Graphics;

public class FirePoint{
	private double feul;
	private double xPos;
	private double yPos;
	private double direction;
	private double speed;
	private Color colour;
	private double size;
	private double gravityFactor;
	private double[] oldXPos = new double[25];
	private double[] oldYPos = new double[25];
	private int counter = 0;
	
	public FirePoint(){
		this(Color.WHITE,0,0,Math.random()*3,(Math.random()*360)+1.0,Math.random(),Math.random()*5+1.0);
	}
	
	public FirePoint(Color color, int x, int y){
		this(color,x,y,Math.random()*3,(Math.random()*360)+1.0,Math.random(),Math.random()*5+1.0);
	}
	
	public FirePoint(Color colour, int x, int y, double speed, double direction, double fuel,double size){
		this.feul = fuel;
		this.xPos = x;
		this.yPos = y;
		this.direction = direction;
		this.speed = speed;
		this.colour = new Color(colour.getRed(),colour.getGreen(),colour.getBlue(),(int)(255*fuel));
		this.size = size;
		gravityFactor = 0.75;
	}
	
	public double getFuel(){
		return feul;
	}
		
	public void tick(){
		oldXPos[counter] = xPos;
		oldYPos[counter] = yPos;
		counter++;
		if(counter > oldYPos.length-1){
			counter = 0;
		}
		xPos += speed*Math.cos(Math.toRadians(direction));
		yPos += speed*Math.sin(Math.toRadians(direction))+gravityFactor;
		speed -= speed*0.01;
		feul = (feul*0.96);
		colour = new Color(colour.getRed(),colour.getGreen(),colour.getBlue(),(int)(255*feul));
	}
	
	public void draw(Graphics g){
		g.setColor(colour);
		for(int i = 0; i < oldYPos.length ; i++){
			if(oldYPos[i]!=0.0 && oldXPos[i]!=0.0){
				g.fillRect((int)oldXPos[i],(int)oldYPos[i],(int)size,(int)size);
			}
		}
		double whiteAlphaValue = feul*2;
		if(whiteAlphaValue>1){
			whiteAlphaValue=1;
		}
		g.setColor(new Color(255,255,255,(int)(255*(whiteAlphaValue))));
		g.fillRect((int)xPos,(int)yPos,(int)size,(int)size);
	}
}
