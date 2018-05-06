package adventure;
import java.awt.Point;

class GameItem
{
	Point position;
	Point show;
	
	int speed;
	int cnt;
	int kind;
	
	GameItem(int x, int y, int kind){
		this.kind=kind;
		position=new Point(x,y);
		show=new Point(x/100,y/100);
		speed=-200;
		cnt=0;
	}
	
	public boolean move(){
		boolean bl=false;
		position.x-=speed;
		cnt++;
		
		if(cnt>=50) {
			speed=200;
		} else if(cnt>=30) {
			speed=100;
		} else if(cnt>=20) {
			speed=-100;
		}
		
		show.x=position.x/100;
		if(position.x<0) {
			bl=true;
		}
		
		return bl;
	}
}