package adventure;
import java.awt.Point;

class GameBullet
{
	Point show;
	Point position;
	Point prev;
	
	int degree;
	int speed;
	int bNum;
	int p;
	
	GameBullet(int x, int y, int num, int p, int degree, int speed){
		position=new Point(x,y);
		show=new Point(x/100,y/100);
		prev=new Point(x,y);
		this.bNum=num;
		this.p=p;
		this.degree=degree;
		this.speed=speed;
	}
	
	public void move(){
		prev=position;
		position.x-=(speed*Math.sin(Math.toRadians(degree))*100);
		position.y-=(speed*Math.cos(Math.toRadians(degree))*100);
		show.x=position.x/100;
		show.y=position.y/100;
		}
}