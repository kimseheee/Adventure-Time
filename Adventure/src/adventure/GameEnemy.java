package adventure;
import java.awt.Point;

class GameEnemy
{
	GameFrame gameFrame;
	Point show;
	Point position;
	Point prev;
	
	int image;
	int kind;
	int life;
	int mode;
	int cnt;
	int type;
	int range;
	
	GameBullet bullet;
	GameEnemy(GameFrame frame, int image, int x, int y, int kind, int mode){
		this.gameFrame=frame;
		position=new Point(x,y);
		prev=new Point(x,y);
		show=new Point(x/100,y/100);
		
		this.kind=kind;
		this.image=image;
		this.mode=mode;
		
		life=6+frame.RAND(0,5)*frame.level;
		cnt=frame.RAND(frame.level*5,80);
		type=frame.RAND(0,4);
		range=1500;
		
		switch(kind){
		case 0:
			break;
		case 1:
			life=400+300*frame.level;
			mode=0;
			range=12000;
			break;
		case 2:
			life=20+frame.RAND(0,10)*frame.level;
			range=2000;
			cnt=-(frame.RAND(30,50));
			break;
		}
		
	}
	
	public boolean move(){
		boolean bl=true;
		
		switch(kind){
		case 2:
			if(mode!=4) break;
			if(cnt<30&&cnt%5==0){
				bullet=new GameBullet(position.x, position.y, 2, 1, 90, 5);
				gameFrame.bullets.add(bullet);
			}
			if(cnt>50){
				if(gameFrame.RAND(1,100)<50){
					mode=1;
					cnt=30;
				}else{
					mode=5;
					cnt=0;
				}
			}
			break;
		case 0:
			switch(type){
			case 0:
				if(cnt%100==0||cnt%103==0||cnt%106==0) {
					bullet=new GameBullet(position.x, position.y, 2, 1, gameFrame.getAngle(position.x,position.y,gameFrame.pX,gameFrame.pY), 3);
					gameFrame.bullets.add(bullet);
				}
				break;
			case 1:
				if(cnt%90==0||cnt%100==0||cnt%110==0) {
					bullet=new GameBullet(position.x, position.y, 2, 1, (0+(cnt%36)*10)%360, 3);
					gameFrame.bullets.add(bullet);
					bullet=new GameBullet(position.x, position.y, 2, 1, (30+(cnt%36)*10)%360, 3);
					gameFrame.bullets.add(bullet);
					bullet=new GameBullet(position.x, position.y, 2, 1, (60+(cnt%36)*10)%360, 3);
					gameFrame.bullets.add(bullet);
					bullet=new GameBullet(position.x, position.y, 2, 1, (90+(cnt%36)*10)%360, 3);
					gameFrame.bullets.add(bullet);
				}
				break;
			case 2:
				if(cnt%30==0||cnt%60==0||cnt%90==0||cnt%120==0||cnt%150==0||cnt%180==0) {
					bullet=new GameBullet(position.x, position.y, 2, 1, (gameFrame.getAngle(position.x,position.y,gameFrame.pX,gameFrame.pY)+gameFrame.RAND(-20,20))%360, 2);
					gameFrame.bullets.add(bullet);
				}
				break;
			case 3:
				if(cnt%90==0||cnt%110==0||cnt%130==0){
					bullet=new GameBullet(position.x, position.y, 2, 1, gameFrame.getAngle(position.x,position.y,gameFrame.pX,gameFrame.pY), 2);
					gameFrame.bullets.add(bullet);
					bullet=new GameBullet(position.x, position.y, 2, 1, (gameFrame.getAngle(position.x,position.y,gameFrame.pX,gameFrame.pY)-20)%360, 2);
					gameFrame.bullets.add(bullet);
					bullet=new GameBullet(position.x, position.y, 2, 1, (gameFrame.getAngle(position.x,position.y,gameFrame.pX,gameFrame.pY)+20)%360, 2);
					gameFrame.bullets.add(bullet);
				}
				break;
			case 4:
				break;
			}
			break;
		case 1:
			int lv,i;
			switch(mode){
			case 5:
				if(gameFrame.level>=10) lv=5; else lv=(10-gameFrame.level)*5;
				if(cnt%lv==0||cnt%(lv+5)==0||cnt%(lv+15)==0) {
					for(i=0;i<4+(50-lv)/5;i++){
						bullet=new GameBullet(position.x, position.y, 2, 1, (30*i+(cnt%36)*10)%360, 5);
						gameFrame.bullets.add(bullet);
					}
				
				}
				break;
			case 7:
				if(gameFrame.level>=10) lv=1; else lv=10-gameFrame.level;
				if(cnt%lv==0){
					bullet=new GameBullet(position.x-3000+gameFrame.RAND(-10,+10)*100, position.y+gameFrame.RAND(10,80)*100, 2, 1, 90, 5+(10-lv)/2);
					gameFrame.bullets.add(bullet);
				}
				break;
			}
			break;
		}

		switch(kind){
		case 2:
			switch(mode){
			case 0:
				position.x-=500;
				if(cnt>=0&&position.x<gameFrame.gScreenWidth*80) {
					mode=1;
					cnt=0;
				}
				break;
			case 1:
				if(position.x>gameFrame.gScreenWidth*80){
					mode=0;
					break;
				}
				if(cnt>=30) {
					if(position.y>gameFrame.pY) mode=3;
					else mode=2;
					cnt=0;
				}
				break;
			case 2:
				if(position.y<gameFrame.gScreenHeight*90&&cnt<20)
					position.y+=250;
				else {
					mode=4;
					cnt=0;
				}
				break;
			case 3:
				if(position.y>6400&&cnt<20)
					position.y-=250;
				else {
					mode=4;
					cnt=0;
				}
				break;
			case 5:
				position.x+=350;
				break;
			case 4:
				break;
			}
			break;
		case 0:
			switch(mode){
			case 0:
				position.x-=500;
				position.y+=80;
				if(position.x<gameFrame.pX) mode=2;
				break;
			case 1:
				position.x-=500;
				position.y-=80;
				if(position.x<gameFrame.pX) mode=3;
				break;
			case 2:
				position.x+=600;
				position.y+=240;
				break;
			case 3:
				position.x+=600;
				position.y-=240;
				break;
			}
			break;
		case 1:
			if(gameFrame.gameCnt==1200) mode=4;
			if(gameFrame.gameCnt==2210) mode=6;
			switch(mode){
			case 0:
				position.x-=100;
				if(position.x<53000) mode=1;
				break;
			case 1:
				if(cnt%30==0){
					if(position.y>gameFrame.pY) mode=3;
					else if(position.y<gameFrame.pY) mode=2;
					prev.x=position.x;
					prev.y=position.y;
				}
				break;
			case 2:
				if(position.y+400<42000)
					position.y+=400;
				else{
					cnt=0;
					mode=7;
				}
				if(position.y-prev.y>12000) {
					cnt=0;
					mode=7;
				}
				break;
			case 3:
				if(position.y-400>6000)
					position.y-=400;
				else{
					cnt=0;
					mode=7;
				}
				if(prev.y-position.y>12000) {
					cnt=0;
					mode=7;
				}
				break;
			case 4:
				position.x-=800;
				if(position.x<30000) {
					mode=8;
					cnt=0;
				}
				break;
			case 5:
				position.x+=350;
				if(position.x>53000) mode=1;
				break;
			case 6:
				position.x+=500;
				break;
			case 7:
				if(cnt>100) mode=1;
				break;
			case 8:
				if(cnt>90) mode=5;
				break;
			}
			break;
		}
		show.x=position.x/100;
		show.y=position.y/100;
		if(show.x<0||show.x>640||show.y<0||show.y>480) bl=false;

		cnt++;
		return bl;
	}
}