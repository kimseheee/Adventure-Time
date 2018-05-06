package adventure;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

class GameScreen extends Canvas
{
	GameFrame frame;
	int cnt, gameCnt;

	int x,y;

	Image doubleBuff;
	Graphics graphics;

	Image background1,background2, background3;
	Image titleImg, keyImg;

	Image characters[]=new Image[9];
	Image enemys[]=new Image[5];
	Image bullets[]=new Image[5];
	Image exp;
	Image items[]=new Image[3];
	
	Image num;
	Image ui;

	Image startLogo;
	Image endLogo;
	Image shield;

	Font font;

	int arr[]={-2,-1,0,1,2,1,0,-1};
	int arr2[]={-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7};
	int step=0;

	boolean boss=false;

	GameScreen(GameFrame frame){
		this.frame=frame;
		font=new Font("Default",Font.PLAIN,9);
	}

	public void paint(Graphics g){
		if(graphics==null) {
			doubleBuff=createImage(frame.gScreenWidth,frame.gScreenHeight);
			if(doubleBuff==null) {
				System.out.println("Error");
			} else {
				graphics=doubleBuff.getGraphics();
			}
			return;
		}
		update(g);
	}
	
	public void update(Graphics g){
		cnt=frame.cnt;
		gameCnt=frame.gameCnt;
		
		if(graphics==null) {
			return;
		}
		realPaint();
		g.drawImage(doubleBuff,0,0,this);
	}
	
	public void realPaint(){
		switch(frame.status){
		case 0:
			drawTitle();
			graphics.setColor(new Color(0));
			graphics.drawString("Education ver.", 10,40);
			break;
		case 1:
			drawBackground();
			draw();
			drawBackground2();
			drawImageAnc(startLogo, 120,240, 3);
			break;
		case 2:
		case 4:
			drawBackground();
			draw();
			drawEnemy();
			drawBullet();
			drawEffect();
			drawItem();
			drawBackground2();
			drawUI();
			break;
		case 3://게임오버
			drawBackground();
			drawEnemy();
			drawBullet();
			drawBackground2();
			drawImageAnc(endLogo, 320,240, 4);
			drawUI();
			break;
		default:
			break;
		}
	}

	public void drawTitle(){
		graphics.drawImage(titleImg,0,0,this);
		
		if(cnt%20<10) {
			graphics.drawImage(keyImg, 320-201,370, this);
		}
	}
	
	public void drawBackground(){
		int i;

		int temp1 = (cnt%2560)/2;
		temp1=temp1>=640?temp1-1280:temp1;
		int temp2 = temp1>=0?640-temp1:-(640+temp1);
		
		graphics.drawImage(background1,0-temp1,0,this);
		graphics.drawImage(background3,temp2,0,this);

	}
	
	public void drawBackground2(){
		int i;
		step=(cnt%(background2.getWidth(this)/frame.scrSpeed))*frame.scrSpeed;
		graphics.drawImage(background2,0-step,540-background2.getHeight(this)+arr[(cnt/20)%8]*2,this);
		//System.out.println("요기"+step);
		if(step>=background2.getWidth(this)-frame.gScreenWidth) {
			graphics.drawImage(background2,0-step+background2.getWidth(this),540-background2.getHeight(this)+arr[(cnt/20)%8]*2,this);
		}
	}
	
	public void drawEnemy(){
		int i;
		GameEnemy enemy;
		
		for(i=0;i<frame.enemies.size();i++){
			enemy=(frame.enemies.elementAt(i));
			switch(enemy.image){
			case 0:
				drawImageAnc(enemys[0], enemy.show.x, enemy.show.y, ((cnt/8)%7)*36,0, 36,90, 4);
				break;
			case 1:
				drawImageAnc(enemys[1], enemy.show.x, enemy.show.y, 4);//보스 출력
				break;
			case 2:
				switch(enemy.mode){
				case 0:
				case 1:
				case 2:
				case 3:
					drawImageAnc(enemys[2], enemy.show.x, enemy.show.y, 0,0,80,50,4);
					break;
				case 5:
					drawImageAnc(enemys[2], enemy.show.x, enemy.show.y, 80,0,80,50,4);
					break;
				case 4:
					drawImageAnc(enemys[2], enemy.show.x, enemy.show.y, 80,0,80,50,4);
					break;
				}
			default:
				break;
			}
		}
	}
	
	public void drawBullet(){
		GameBullet bullet;
		int i;
		
		for(i=0;i<frame.bullets.size();i++){
			bullet=(frame.bullets.elementAt(i));
			switch(bullet.bNum){
			case 0:
			case 1:
			case 2:
			case 3:
				drawImageAnc(bullets[bullet.bNum], bullet.show.x-6,bullet.show.y-6, 4);
				break;
			}
		}
	}
	
	public void drawItem(){
		int i;
		GameItem item;
		
		for(i=0;i<frame.items.size();i++){
			item=(frame.items.elementAt(i));
			drawImageAnc(items[item.kind], item.show.x, item.show.y, ((frame.cnt/4)%7)*36,0, 36,36, 4);
		}
	}
	
	public void drawEffect(){
		int i;
		GameEffect effect;
		
		for(i=0;i<frame.effects.size();i++){
			effect=(frame.effects.elementAt(i));
			drawImageAnc(exp, effect.show.x, effect.show.y, ((16-effect.cnt)%4)*64,((16-effect.cnt)/4)*64,64,64, 4);
		}
	}
	
	public void draw(){
		int myX,myY;
		myX=frame.pX/100;
		myY=frame.pY/100;
		
		switch(frame.pMode){
		case 0:
		case 1:
			if(frame.cnt%20<10) {
				drawImageAnc(characters[2+(frame.cnt/5)%2], myX, myY, 4);
			}
			break;
		case 2:
			if(frame.pImage==6) {
				drawImageAnc(characters[frame.pImage+(frame.cnt/3)%2], myX, myY, 4);
			} else if(frame.pImage!=8) {
				drawImageAnc(characters[frame.pImage+(frame.cnt/5)%2], myX, myY, 4);
			} else if(frame.pImage==8) {
				drawImageAnc(characters[frame.pImage], myX, myY, 4);
			}
			break;
		case 3:
			if(frame.cnt%6<3) {
				drawImageAnc(characters[8], myX, myY, 4);
			}
			break;
		}
		if(frame.myshield>2) {
			drawImageAnc(shield, (int)(Math.sin(Math.toRadians((cnt%72)*5))*16+myX), (int)(Math.cos(Math.toRadians((cnt%72)*5))*16+myY), (frame.cnt/6%7)*64,0, 64,64, 4);
		}
		else if(frame.myshield>0&&frame.cnt%4<2) {
			drawImageAnc(shield, (int)(Math.sin(Math.toRadians((cnt%72)*5))*16+myX), (int)(Math.cos(Math.toRadians((cnt%72)*5))*16+myY), (frame.cnt/6%7)*64,0, 64,64, 4);
		}
	}
	
	public void drawUI(){
		String string="[1] Speed down   [2] Speed up   [3] Pause";

		graphics.setColor(new Color(0));
		graphics.drawString(string, 20,frame.gScreenHeight-50);
		graphics.drawString(string, 21,frame.gScreenHeight-50);
		graphics.drawString(string, 20,frame.gScreenHeight-51);
		graphics.drawString(string, 20,frame.gScreenHeight-49);
		graphics.setColor(new Color(0xffffff));
		graphics.drawString(string, 20,frame.gScreenHeight-20);
		
		graphics.drawImage(ui, 16,35, this);
		
		drawImageNum(num, 320, 55, frame.score, 8);
		drawImageNum(num, 52, 55, frame.mylife, 2);
		drawImageNum(num, 576, 55, frame.level, 2);
		
	}
	
	public void drawImageNum(Image img, int x, int y, int value, int digit){
		
		int width = img.getWidth(this)/10;
		int height = img.getHeight(this);
		
		String valueStr = String.valueOf(value);
		
		if(valueStr.length()<digit)
			valueStr = "0000000000".substring(0, digit-valueStr.length()) + valueStr;
		int _xx = x-valueStr.length()*width/2;
		for(int i=0;i<valueStr.length();i++)
			drawImageAnc(num, _xx+i*width, y, (valueStr.charAt(i)-'0')*width, 0, width,height, 0);

	}

	
	public void drawImageAnc(Image img, int x, int y, int anc){
		int imgWidth, imgHeight;
		imgWidth=img.getWidth(this);
		imgHeight=img.getHeight(this);
		x=x-(anc%3)*(imgWidth/2);
		y=y-(anc/3)*(imgHeight/2);
		
		graphics.drawImage(img, x,y, this);
	}
	
	public void drawImageAnc(Image img, int x, int y, int sx,int sy, int wd,int ht, int anc){
		if(x<0) {
			wd+=x;
			sx-=x;
			x=0;
		}
		if(y<0) {
			ht+=y;
			sy-=y;
			y=0;
		}
		if(wd<0||ht<0) {
			return;
		}
		x=x-(anc%3)*(wd/2);
		y=y-(anc/3)*(ht/2);
		graphics.setClip(x, y, wd, ht);
		graphics.drawImage(img, x-sx, y-sy, this);
		graphics.setClip(0,0, frame.gScreenWidth+10,frame.gScreenHeight+30);
	}


}