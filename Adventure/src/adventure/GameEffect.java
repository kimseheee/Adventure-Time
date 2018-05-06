package adventure;

import java.awt.Point;

class GameEffect
{
	Point show;
	Point position;
	Point prev;

	int image;
	int kind;
	int cnt;
	
	GameEffect(int image, int x, int y, int kind){
		position=new Point(x,y);
		prev=new Point(x,y);
		show=new Point(x/100,y/100);
		this.kind=kind;
		this.image=image;
		cnt=16;
	}
}