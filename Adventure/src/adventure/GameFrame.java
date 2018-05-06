package adventure;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


class GameFrame extends JFrame implements KeyListener, Runnable, ActionListener
{
	public final static int UP		=0x001;
	public final static int DOWN	=0x002;
	public final static int LEFT	=0x004;
	public final static int RIGHT	=0x008;
	public final static int SPACE	=0x010;
	
	public static int WIDTH = 640;
	public static int HEIGHT = 480;
	
	GameScreen gameScreen;
	GameScore gameScore;

	Thread thread;
	boolean bl=true;
	Random random = new Random();

	int status;
	int cnt;
	int delayTime;
	long time;
	int buff;

	int score;
	int mylife;
	int gameCnt;
	int scrSpeed=16;
	int level;

	int pX,pY;
	int pSpeed;
	int pDegree;
	int pWidth, pHeight;
	int pMode=1;
	int pImage;
	int pCnt;
	boolean myshoot=false;
	int myshield;
	boolean inv=false;

	int gScreenWidth=640;
	int gScreenHeight=480;

	String name = "이름";
	
	Vector<GameBullet> bullets=new Vector<GameBullet>();
	Vector<GameEnemy> enemies=new Vector<GameEnemy>();
	Vector<GameEffect> effects=new Vector<GameEffect>();
	Vector<GameItem> items=new Vector<GameItem>();
	
	JTextField myScore; 
	JTextField myName;
	JPanel background;
	
	TreeMap<String, Integer> ranking;
	
	GameFrame(){
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		setIconImage(makeImage("./images/icon.png"));
		setBackground(new Color(0xffffff));
		setTitle("Adventure Time");
		setLayout(null);
		setBounds((screen.width-WIDTH)/2,(screen.height-HEIGHT)/2,WIDTH,HEIGHT);
		setResizable(false);
		setVisible(true);

		gameScreen=new GameScreen(this);
		gameScreen.setBounds(0,0,gScreenWidth,gScreenHeight);
		add(gameScreen);

		gameScore = new GameScore();
		ranking = new TreeMap<String, Integer>();
		
		initGame();
		initialize();
		
		gameScreen.addKeyListener(this);
		addWindowListener(new MyWindowAdapter());
	}

	public void actionPerformed(ActionEvent event) {
	    name = myName.getText();
	    JOptionPane.showMessageDialog(this, " 이름 : " + name +" 점수 : " + score ,"Msg", JOptionPane.INFORMATION_MESSAGE, null);
	    gameScore.setScore(name, score);
	    ranking = gameScore.rankScore(gameScore.getScore());
	    this.getContentPane().removeAll();
	    this.invalidate();
	    this.validate();
	    
    	gameScreen=new GameScreen(this);
		gameScreen.setBounds(0,-25,gScreenWidth,gScreenHeight);		
		add(gameScreen);
		initData();
//		init();
//		initPlayer();
//		
		initialize();
//		initGame();
		
		gameScreen.addKeyListener(this);	
//		gameScore.print();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(status==2){
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				buff|=SPACE;
				break;
			case KeyEvent.VK_LEFT:
				buff|=LEFT;
				break;
			case KeyEvent.VK_UP:
				buff|=UP;
				break;
			case KeyEvent.VK_RIGHT:
				buff|=RIGHT;
				break;
			case KeyEvent.VK_DOWN:
				buff|=DOWN;
				break;
			case KeyEvent.VK_1:
				if(pSpeed>1) pSpeed--;
				break;
			case KeyEvent.VK_2:
				if(pSpeed<9) pSpeed++;
				break;
			case KeyEvent.VK_3:
				if(status==2) status=4;
				break;
			default:
				break;
			}
		} else if(status!=2) buff=e.getKeyCode();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				buff&=~SPACE;
				myshoot=true;
				break;
			case KeyEvent.VK_LEFT:
				buff&=~LEFT;
				break;
			case KeyEvent.VK_UP:
				buff&=~UP;
				break;
			case KeyEvent.VK_RIGHT:
				buff&=~RIGHT;
				break;
			case KeyEvent.VK_DOWN:
				buff&=~DOWN;
				break;
			}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	public void initGame(){

		status=0;
		cnt=0;
		delayTime=17;
		buff=0;

		thread=new Thread(this);
		thread.start();
	}
	
	public void initialize(){
		initTitle();
		gameScreen.repaint();
	}
	
	public void run(){
		try
		{
			while(bl){
				time=System.currentTimeMillis();
				gameScreen.repaint();
				process();
				keyprocess();
				if(System.currentTimeMillis()-time<delayTime) {
					Thread.sleep(delayTime-System.currentTimeMillis()+time);
				}
				if(status!=4) {
					cnt++;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void process(){
		switch(status){
		case 0:
			break;
		case 1:
			myProcess();
			if(pMode==2) status=2;
			break;
		case 2:
			myProcess();
			enemyProcess();
			bulletProcess();
			effectProcess();
			gameFlow();
			itemProcess();
			break;
		case 3:
			enemyProcess();
			bulletProcess();
			gameFlow();
			break;
		case 4:
			break;
		default:
			break;
		}
		if(status!=4) gameCnt++;
	}

	private void keyprocess(){
		switch(status){
		case 0:
			if(buff==KeyEvent.VK_SPACE) {
				init();
				initPlayer();
				status=1;
			}
			break;

		case 2:
			if(pMode==2||pMode==0){
				switch(buff){
				case 0:
					pDegree=-1;
					pImage=0;
					break;
				case SPACE:
					pDegree=-1;
					pImage=6;
					break;
				case UP:
					pDegree=0;
					pImage=2;
					break;
				case UP|SPACE:
					pDegree=0;
					pImage=6;
					break;
				case LEFT:
					pDegree=90;
					pImage=4;
					break;
				case LEFT|SPACE:
					pDegree=90;
					pImage=6;
					break;
				case RIGHT:
					pDegree=270;
					pImage=2;
					break;
				case RIGHT|SPACE:
					pDegree=270;
					pImage=6;
					break;
				case UP|LEFT:
					pDegree=45;
					pImage=4;
					break;
				case UP|LEFT|SPACE:
					pDegree=45;
					pImage=6;
					break;
				case UP|RIGHT:
					pDegree=315;
					pImage=2;
					break;
				case UP|RIGHT|SPACE:
					pDegree=315;
					pImage=6;
					break;
				case DOWN:
					pDegree=180;
					pImage=2;
					break;
				case DOWN|SPACE:
					pDegree=180;
					pImage=6;
					break;
				case DOWN|LEFT:
					pDegree=135;
					pImage=4;
					break;
				case DOWN|LEFT|SPACE:
					pDegree=135;
					pImage=6;
					break;
				case DOWN|RIGHT:
					pDegree=225;
					pImage=2;
					break;
				case DOWN|RIGHT|SPACE:
					pDegree=225;
					pImage=6;
					break;
				default:
					buff=0;
					pDegree=-1;
					pImage=0;
					break;
				}
			}
			break;
		case 3:
			if(gameCnt>=200&&buff==KeyEvent.VK_SPACE){
				initrank();
				status=5;
				
			}
			break;
		case 4:
			if(gameCnt>=200&&buff==KeyEvent.VK_3) {
				status=2;
				gameCnt++;
			}
		case 5:
			if(buff==KeyEvent.VK_SPACE){
				initTitle();
				status=0;
				buff=0;
				gameCnt++;
			}
			break;
		default:
			break;
		}
	}
	public void initTitle(){
		int i;
	
		gameScreen.titleImg=makeImage("./images/title.png");
		gameScreen.keyImg=makeImage("./images/pushspace.png");

	}
	
	public void initrank(){
		gameScreen.setVisible(false);
		
		myScore = new JTextField();
		myName = new JTextField();
		
		final Image icon;
		icon = makeImage("./images/result.png");

        //배경 Panel 생성후 컨텐츠페인으로 지정      
        background = new JPanel() {
            public void paintComponent(Graphics g) {
                // Approach 1: Dispaly image at at full size
                g.drawImage(icon, 0, -40, null);           
                setOpaque(false); //그림을 표시하게 설정,투명하게 조절
                super.paintComponent(g);
            }
        };
       
        background.setBounds(0,0,gScreenWidth,gScreenHeight);		
        setContentPane(background);
    		  
		myScore.setBounds(230,263,265,50);
		background.add(myScore);
		
		myName.setBounds(230,370,265,50);
		background.add(myName);
        
		myName.requestFocus();
		myName.addActionListener(this);
	    myScore.setText(score+"");
	    myName.setText(name);
	}
	
	public void init(){
		int i;

		gameScreen.background1=makeImage("./images/background1.jpg");
		gameScreen.background3=makeImage("./images/background2.jpg");
		gameScreen.background2=makeImage("./images/bg_f.png");
		for(i=0;i<4;i++) gameScreen.bullets[i]=makeImage("./images/game/bullet_"+i+".png");
		gameScreen.enemys[0]=makeImage("./images/game/enemy0.png");
		gameScreen.exp=makeImage("./images/game/explode.png");
		gameScreen.items[0]=makeImage("./images/game/item0.png");
		gameScreen.items[1]=makeImage("./images/game/item1.png");
		gameScreen.items[2]=makeImage("./images/game/item2.png");
		gameScreen.startLogo=makeImage("./images/game/start.png");
		gameScreen.endLogo=makeImage("./images/game/gameover.png");
		gameScreen.shield=makeImage("./images/game/shield.png");
		gameScreen.enemys[1]=makeImage("./images/game/enemy1.png");
		gameScreen.enemys[2]=makeImage("./images/game/enemy2.png");
		
		gameScreen.num=makeImage("./images/gnum.png");
		gameScreen.ui=makeImage("./images/ui_up.png");
		
		buff=0;
		bullets.clear();
		enemies.clear();
		effects.clear();
		items.clear();
		level=0;
		gameCnt=0;
	}
	
	public void initPlayer(){
		for(int i=0;i<9;i++){
			if(i<10) {
				gameScreen.characters[i]=makeImage("./images/player/my_0"+i+".png");
			}				
			else {
				gameScreen.characters[i]=makeImage("./images/player/my_"+i+".png");
			}
		}
		initData();
	}
	
	public void initPlayer2(){
		for(int i=0;i<9;i++){
			if(i<10) {
				gameScreen.characters[i]=makeImage("./images/player2/my_0"+i+".png");
			}				
			else {
				gameScreen.characters[i]=makeImage("./images/player2/my_"+i+".png");
			}
		}
	}
	
	public void initData(){
		score=0;
		pX=0;
		pY=23000;
		pSpeed=4;
		pDegree=-1;
		pMode=1;
		pImage=2;
		pCnt=0;
		mylife=3;
		buff=0;
	}
	
	public void myProcess(){
		GameBullet shoot;
		switch(pMode){
		case 1:
			pX+=200;
			if(pX>20000) {
				pMode=2;
			}
			break;
		case 0:
			if(pCnt--==0) {
				pMode=2;
				pImage=0;
			}
		case 2:
			if(pDegree!=-1&&inv) {
				pDegree=(pDegree+180)%360;
			}
			if(pDegree>-1) {
				pX-=(pSpeed*Math.sin(Math.toRadians(pDegree))*100);
				pY-=(pSpeed*Math.cos(Math.toRadians(pDegree))*100);
			}
			if(pImage==6) {
				pX-=20;
				if(cnt%4==0||myshoot){
					myshoot=false;
					shoot=new GameBullet(pX+2500, pY+1500, 0, 0, RAND(245,265), 8);
					bullets.add(shoot);
					shoot=new GameBullet(pX+2500, pY+1500, 0, 0, RAND(268,272), 9);
					bullets.add(shoot);
					shoot=new GameBullet(pX+2500, pY+1500, 0, 0, RAND(275,295), 8);
					bullets.add(shoot);
				}
			}
			break;
		case 3:
			pImage=8;
			if(pCnt--==0) {
				pMode=0;
				pCnt=50;
			}
			break;
		}
		if(pX<2000) {
			pX=2000;
		}
		if(pX>62000) {
			pX=62000;
		}
		if(pY<3000) {
			pY=3000;
		}
		if(pY>45000) {
			pY=45000;
		}
	}
	
	public void enemyProcess(){
		int i;
		GameEnemy enemy;
		for(i=0;i<enemies.size();i++){
			enemy=(enemies.elementAt(i));
			if(!enemy.move()) enemies.remove(i);
		}
	}
	
	public void bulletProcess(){
		GameBullet bullet;
		GameEnemy enemy;
		GameEffect effect;
		int i,j, distance;
		for(i=0;i<bullets.size();i++){
			bullet=(bullets.elementAt(i));
			bullet.move();
			if(bullet.show.x<10||bullet.show.x>gScreenWidth+10||bullet.show.y<10||bullet.show.y>gScreenHeight+10) {
				bullets.remove(i);
				continue;
			}
			if(bullet.p==0) {
				for(j=0;j<enemies.size();j++){
					enemy=(enemies.elementAt(j));
					distance=getDistance(bullet.show.x,bullet.show.y, enemy.show.x,enemy.show.y);
					
					if(distance<enemy.range) {
						if(enemy.life--<=0){
							if(enemy.kind==1){
								if(gameCnt<2100) gameCnt=2100;
							}
							enemies.remove(j);
							effect=new GameEffect(0, enemy.position.x, bullet.position.y, 0);
							effects.add(effect);
							
							int itemKind=RAND(1,100);
							GameItem tem;
							if(itemKind<=70)
								tem=new GameItem(enemy.position.x, bullet.position.y,0);
							else if(itemKind<=95)
								tem=new GameItem(enemy.position.x, bullet.position.y,2);
							else
								tem=new GameItem(enemy.position.x, bullet.position.y,1);
							items.add(tem);
						}
					
						effect=new GameEffect(0, bullet.position.x, bullet.position.y, 0);
						effects.add(effect);
						score++;
						bullets.remove(i);
						break;
					}
				}
			} else {
				if(pMode!=2) continue;
				distance=getDistance(pX/100,pY/100, bullet.show.x,bullet.show.y);
				if(distance<500) {
					if(myshield==0){
						pMode=3;
						pCnt=30;
						bullets.remove(i);
						effect=new GameEffect(0, pX-2000, pY, 0);
						effects.add(effect);
						if(--mylife<=0) {
							status=3;
							gameCnt=0;
						}
					} else {
						myshield--;
						bullets.remove(i);
					}
				}
			}
		}
	}
	
	public void effectProcess(){
		int i;
		GameEffect effect;
		for(i=0;i<effects.size();i++){
			effect=(effects.elementAt(i));
			if(cnt%3==0) {
				effect.cnt--;
			}
			if(effect.cnt==0) {
				effects.remove(i);
			}
		}
	}
	
	public void gameFlow(){
		int control=0;
		int num=0, mode=0;
		if(gameScreen.boss){
			if(level>1){
				if(800<gameCnt&&gameCnt<1000){
					if(gameCnt%60==0) {
						num=RAND(30,gScreenHeight-30)*100;
						if(num<24000){
							mode=0;
						}else {
							mode=1;
						}
						GameEnemy en=new GameEnemy(this, 0, gScreenWidth*100, num, 0,mode);
						enemies.add(en);
					}
				}else
				if(1600<gameCnt&&gameCnt<2200){
					if(gameCnt%30==0) {
						GameEnemy enemy;
						num=RAND(30,gScreenHeight-30)*100;
						if(num<24000) {
							mode=0;
							}else {
								mode=1;
							}
						
						if(level>1&&RAND(1,100)<level*10)
							enemy=new GameEnemy(this, 2, gScreenWidth*100, num, 2,0);
						else
							enemy=new GameEnemy(this, 0, gScreenWidth*100, num, 0,mode);
						enemies.add(enemy);
					}
				}
			}
			if(gameCnt>2210){
				gameScreen.boss=false;
				gameCnt=0;
				System.out.println("보스 타임아웃");
			}
		}else{
			if(gameCnt<500) control=1;
			else if(gameCnt<1000) control=2;
			else if(gameCnt<1300) control=0;
			else if(gameCnt<1700) control=1;
			else if(gameCnt<2000) control=2;
			else if(gameCnt<2400) control=3;
			else {
				initPlayer2();
				gameScreen.repaint();
				System.out.println("보스 등장");
				gameScreen.boss=true;
				GameEnemy en=new GameEnemy(this, 1, gScreenWidth*100, 24000, 1, 0);// img 값이 1, kind 값이 1
				enemies.add(en);
				gameCnt=0;
				level++;
			}
			if(control>0) {
				num=RAND(30,gScreenHeight-30)*100;
				if(num<24000) mode=0; else mode=1;
			}
			GameEnemy enemy;
			switch(control){
			case 1:
				if(gameCnt%90==0) {
					if(RAND(1,3)!=3&&level>0){
						enemy=new GameEnemy(this, 2, gScreenWidth*100, num, 2,mode);
					} else {
						enemy=new GameEnemy(this, 0, gScreenWidth*100, num, 0,mode);
					}		
					enemies.add(enemy);
				}
				break;
			case 2:
				if(gameCnt%50==0) {
					if(RAND(1,3)!=3&&level>0) {
						enemy=new GameEnemy(this, 2, gScreenWidth*100, num, 2,mode);
					} else {
						enemy=new GameEnemy(this, 0, gScreenWidth*100, num, 0,mode);
					}
					enemies.add(enemy);
				}
				break;
			case 3:
				if(gameCnt%20==0) {
					if(RAND(1,3)!=3&&level>0) {
						enemy=new GameEnemy(this, 2, gScreenWidth*100, num, 2,mode);
					} else {
						enemy=new GameEnemy(this, 0, gScreenWidth*100, num, 0,mode);
					}
					enemies.add(enemy);
				}
				break;
			}
		}
	}
	
	public void itemProcess(){
		int i, distance;
		GameItem item;
		for(i=0;i<items.size();i++){
			item=(items.elementAt(i));
			distance=getDistance(pX/100,pY/100, item.show.x,item.show.y);
			if(distance<1000) {
				switch(item.kind){
				case 0:
					score+=100;
					break;
				case 1:
					myshield=5;
					break;
				case 2:
					int size=enemies.size();
					for(int k=0;k<size;k++){
						GameEnemy ebuff=(enemies.elementAt(k));
						if(ebuff==null) {
							continue;
						}
						if(ebuff.kind==1) {
							score+=300;
							ebuff.life=ebuff.life/2;
							if(ebuff.life<=1) {
								ebuff.life=1;
							}
							continue;
						}
						GameEffect expl=new GameEffect(0, ebuff.position.x,ebuff.position.y, 0);
						effects.add(expl);
						ebuff.position.x=-10000;
						score+=50;
					}
				
					size=bullets.size();
					for(int k=0;k<size;k++){
						GameBullet bbuff=(bullets.elementAt(k));
						if(bbuff.p!=0) {
							bbuff.position.x=-10000;
							score++;
						}
					}
					break;
				}
				items.remove(i);
			} else
				if(item.move()) {
					items.remove(i);
				}
		}
	}

	public Image makeImage(String furl){
		Image image;
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		image=toolkit.getImage(furl);
		try {
			MediaTracker mediaTracker = new MediaTracker(this);
			mediaTracker.addImage(image, 0);
			mediaTracker.waitForID(0);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
		return image;
	}
	public int getDistance(int x1,int y1,int x2,int y2){
		return Math.abs((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1));
	}
	
	public int RAND(int startnum, int endnum) 
	{
		int a, b;
		if(startnum<endnum)
			b = endnum - startnum;
		else
			b = startnum - endnum;
		a = Math.abs(random.nextInt()%(b+1));
		return (a+startnum);
	}
	
	int getAngle(int sx, int sy, int dx, int dy){
		int vx=dx-sx;
		int vy=dy-sy;
		double rad=Math.atan2(vx,vy);
		int degree=(int)((rad*180)/Math.PI);
		return (degree+180);
	}

	public boolean readGameFlow(String fname){
		String buff;
		try
		{
			BufferedReader fin=new BufferedReader(new FileReader(fname));
			if((buff=fin.readLine())!=null) {
				System.out.println(Integer.parseInt(buff));
			}
			fin.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
}
