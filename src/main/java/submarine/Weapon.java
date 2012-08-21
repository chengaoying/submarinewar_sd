package submarine;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import cn.ohyeah.stb.game.SGraphics;


/**
 * 武器技能类
 * @author xiaochen
 *
 */
public class Weapon implements Common {

	int id;					//武器ID (11-30)
	int objectId;			//武器所属者	ID
	int speedY;				//y轴移动速度
	int speedX;				//x轴移动速度
	int direction;			//移动方向 2下3上
	int harm;				//造成的伤害
	int mapx;				//X轴坐标
	int mapy;				//Y轴坐标
	int width;				//武器宽度
	int height;				//武器高度
	long startTime;			//开始时间
	long endTime;			//结束时间
	float terminalX;		//终点横坐标
	float terminalY;		//终点纵坐标(对象是水雷的话,表示水雷的起始坐标)
	float flagx;			//标识x
	float flagy;			//标识y
	int random;				//随机跟踪
	boolean isSingle;		//单个属性
	//boolean isHit;			//是否击中(用于计算命中率)
	
	private int tempx=0,tempy=0;
	private Image imgBomb, imgBomb2, imgBomb3, imgLaser, imgProtect;
	private Image imgAirDrop, imgAirDrop2, imgTorpedo, imgNet, imgNet2;
	public Vector bombs = new Vector();
	public Vector paraDrops = new Vector();
	public Vector dartles = new Vector();
	public Vector lasers = new Vector();
	public Vector energys = new Vector();
	private float velocity, velocity2;  
	public static float bombAmount;		//发射子弹数量
	public static float hitNumber;		//命中数量
	private int laserIndex, laserFlag, protectFlag, protectIndex;
	private int bombIndex, bombFlag;
	private Random ran = new Random();
	public Vector airDrops = new Vector(); //敌方BOSS技能
	public Vector torpedos = new Vector();
	public Vector nets = new Vector();
	public static int airDropFlag=0, airDropIndex=0;
	private int airDropFlag2=0, airDropIndex2=0, netFlag, netIndex;
	public static boolean isAirDrop=false;  //敌方BOSS空投位置显示标志
	public static boolean  isNet = false; 
	private long netTime, netTime2; //网在水面上停留时间
	
	/*武器参数*/
	private int para[][]={
			/*0-武器ID,1-武器宽度,2-武器高度,3-武器速度,4-武器伤害,5-移动方向(2下3上)*/
			{11,16,71,10,100,2},		//普通攻击1
			{12,46,36,10,150,2},		//普通攻击2
			{13,10,45,12,200,2},		//普通攻击3
			{14,16,71,15,300,2}, 	    //空投(技能)
			{15,46,36,10,100,2},		//连射(技能)
			{16,163,368,0,8,2},			//穿透激光弹
			{17,92,91,0,100,2},			//能量防护
			{18,30,125,10,30,3},		//空投(敌方BOSS)
			{19,23,14,8,30,3},	    	//水雷(敌方BOSS)
			{20,9,27,9,0,3}, 			//网(敌方BOSS)	
	};
	
	/**
	 * 创建普通武器
	 * @param own 被跟踪对象(如果有跟踪效果)
	 * @param objectId 发射者ID
	 * @param mapx 普通武器横坐标
	 * @param mapy 普通武器纵坐标
	 * @param direction 普通武器方向(2下3上)
	 * @param width 发射子弹者的宽(用于定位子弹的坐标)
	 * @param height 发射子弹者的高 (用于定位子弹的坐标)
	 */
	public void createBomb(Role own, int objectId, int mapx, int mapy, int direction, int width, int height){
		Weapon bomb = new Weapon();
		if(own!=null){
			bomb.terminalX = own.mapx + own.width/2;
			bomb.terminalY = own.mapy + own.height/2;
			bomb.id = para[own.id-100][0];
		}else{
			bomb.id=para[0][0];
		}
		bomb.width = para[bomb.id-11][1];
		bomb.height = para[bomb.id-11][2];
		bomb.objectId = objectId;
		bomb.mapx = mapx+width/2-bomb.width/2;
		bomb.mapy = mapy+height/2-bomb.height/2;
		bomb.flagx = mapx+width/2-bomb.width/2;
		bomb.flagy = mapy+height/2-bomb.height/2;
		bomb.speedY = para[bomb.id-11][3];
		bomb.direction = direction;
		bomb.harm = para[bomb.id-11][4];
		bomb.random =  Math.abs(ran.nextInt() % 50);
		bombs.addElement(bomb);
	}
	
	/*画普通攻击*/
	public void showBomb(SGraphics g, Role own){
		g.setClip(0, 0, gameMapX, gameMapY);
		Weapon bomb = null;
		for(int i=bombs.size()-1;i>=0;i--){
			bomb = (Weapon)bombs.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				bomb.speedY=0;
			}else{
				bomb.speedY = para[0][3];
			}
			if(bomb.direction==2){
				tempx = bomb.mapx;
				tempy = bomb.mapy;
				tempy += bomb.speedY;
				bomb.mapy = tempy;
				if(own.id==100){
					g.drawRegion(imgBomb, bombIndex*16, 0, 16, 71, 0, tempx, tempy, TopLeft);
					if(bombFlag==0){
						bombFlag++;
					}else{
						bombIndex=(bombIndex+1)%3;
						bombFlag=0;
					}
				}else if(own.id==101){
					g.drawRegion(imgBomb2, 0, 0, 46, 36, 0, tempx, tempy, TopLeft);
				}else if(own.id==102){
					g.drawRegion(imgBomb3, 0, 0, 13, 45, 0, tempx, tempy, TopLeft);
				}
				if(tempy >= 530){
					bombs.removeElement(bomb);
				}
			}
		}
		g.setClip(0, 0, screenW, screenH);
	}
	
	/*敌方潜艇普通攻击*/
	public void showBomb2(SGraphics g, Role own, int level){
		Weapon bomb = null;
		for(int i=bombs.size()-1;i>=0;i--){
			bomb = (Weapon)bombs.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				bomb.speedY=0;
			}else{
				bomb.speedY = para[0][3];
			}
			if(bomb.direction == 3) {
				if(bomb.objectId >= 31){//boss
					tempy = bomb.mapy;
					tempy -= bomb.speedY;
					bomb.mapy = tempy;
					tempx = bomb.mapx;
					if((level==1 && bomb.random<= 30) || (level==2 && bomb.random<= 30) || (level==3 && bomb.random<= 35) 
							|| (level==4 && bomb.random<= 35) || (level==5 && bomb.random<= 45)){
						if(bomb.flagx >= (bomb.terminalX+own.mapx/2)){
							velocity = (float) ((bomb.flagx-bomb.terminalX)/(bomb.flagy-bomb.terminalY)); //tan@
							bomb.speedX = (int) (velocity * bomb.speedY);
							tempx -= bomb.speedX;
							bomb.mapx = tempx;
						}else{
							velocity = (float) ((bomb.terminalX-bomb.flagx)/(bomb.flagy-bomb.terminalY)); //tan@
							bomb.speedX = (int) (velocity * bomb.speedY);
							tempx += bomb.speedX;
							bomb.mapx = tempx;
						}
					}
				}else{//npc
					tempx = bomb.mapx;
					tempy = bomb.mapy;
					tempy -= bomb.speedY;
					bomb.mapy = tempy;
				}
				if(bombFlag==0){
					bombFlag++;
				}else{
					bombIndex=(bombIndex+1)%3;
					bombFlag=0;
				}
				g.setClip(0, 0, screenW, gameMapY);
				g.drawRegion(imgBomb, bombIndex*16, 0, 16, 71, Sprite.TRANS_MIRROR_ROT180, tempx, tempy, TopLeft);
				g.setClip(0, 0, screenW, screenH);
				if(tempy<=own.mapy+own.height/2){
					/*SubmarineGameEngine.bombFlag = true;
					SubmarineGameEngine.bombX = bomb.mapx;
					SubmarineGameEngine.bombY = bomb.mapy;*/
					bombs.removeElement(bomb);
				}
			}	
		}
	}
	
	/*创建   穿透激光弹--己方*/
	public void createLaser(int objectId, int mapx, int mapy, int direction, int level, int diffcultlevel){
		Weapon w = new Weapon();
		w.id = 16;
		w.objectId = objectId;
		w.direction = direction;
		w.harm = para[w.id-11][4]+(level-1)*2 + diffcultlevel*3;
		w.mapx = mapx;
		w.mapy = mapy;
		w.width = para[w.id-11][1];
		w.height = para[w.id-11][2];
		w.speedY = para[w.id-11][3];
		lasers.addElement(w);
	}
	/*画  穿透激光弹*/
	public void showLaser(SGraphics g, Role own){
		Weapon laser=null;
		for(int i=lasers.size()-1;i>=0;i--){
			laser = (Weapon)lasers.elementAt(i);
			if(laser.direction==2){
				laser.mapx=own.mapx-40;
				laser.mapy=own.mapy+30;
				g.drawRegion(imgLaser, laserIndex*laser.width, 0, laser.width, laser.height, 0, laser.mapx, laser.mapy, TopLeft);
				if(laserFlag==0){
					laserFlag++;
				}else{
					if(SubmarineGameEngine.isMenu){
						laserIndex = 1;
					}else{
						laserIndex=(laserIndex+1)%4;
					}
					laserFlag=0;
				}
				if(!SubmarineGameEngine.isMenu){
					SubmarineGameEngine.endTime3 = System.currentTimeMillis()/1000;
				}
				System.out.println("(SubmarineGameEngine.endTime3-SubmarineGameEngine.startTime3)="+(SubmarineGameEngine.endTime3-SubmarineGameEngine.startTime3));
				if((SubmarineGameEngine.endTime3-SubmarineGameEngine.startTime3)>=6){
					lasers.removeElement(laser);
				}
			}
		}
	}
	
	/*创建   呼叫空投--己方*/
	public void createParaDrop(Role own, int mapx, int mapy, int direction){
		Weapon w = new Weapon();
		w.id = 14;
		w.objectId = own.id;
		w.direction = direction;
		w.harm = para[w.id-11][4];
		w.width = para[w.id-11][1];
		w.height = para[w.id-11][2];
		w.mapx = mapx+own.width/2-w.width/2;
		w.mapy =  mapy+own.height/2-w.height/2;
		w.speedY = para[w.id-11][3];
		paraDrops.addElement(w);
	}
	/*显示   呼叫空投--己方*/
	public void showParaDrop(SGraphics g){
		g.setClip(0, 0, gameMapX, gameMapY);
		Weapon w = null;
		for(int i=0;i<paraDrops.size();i++){
			w = (Weapon) paraDrops.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				w.speedY=0;
			}else{
				w.speedY = para[w.id-11][3];
			}
			if(w.direction == 2){
				int tempX = w.mapx;
				int tempY = w.mapy+w.speedY;
				w.mapy = tempY;
				
				if(bombFlag==0){
					bombFlag++;
				}else{
					bombIndex=(bombIndex+1)%3;
					bombFlag=0;
				}
				g.drawRegion(imgBomb, bombIndex*16, 0, 16, 71, 0, tempX, tempY, TopLeft);
			}
			
			if((w.mapy+w.height) >= 530){
				paraDrops.removeElement(w);
			}
		}
		g.setClip(0, 0, screenW, screenH);
	}
	/*创建   连射--己方*/
	public void createDartle(int objectId, int mapx, int mapy, int direction, int width, int height){
		Weapon w = new Weapon();
		w.id = 15;
		w.objectId = objectId;
		w.direction = direction;
		w.width = para[w.id-11][1];
		w.height = para[w.id-11][2];
		w.mapx = mapx+width/2-w.width/2;
		w.mapy = mapy+height/2;
		w.speedY = para[w.id-11][3];
		w.harm = para[w.id-11][4];
		dartles.addElement(w);
	}
	/*显示   连射--己方*/
	public void showDartle(SGraphics g, int id){
		g.setClip(0, 0, gameMapX, gameMapY);
		Weapon w = null;
		for(int i=0;i<dartles.size();i++){
			w = (Weapon) dartles.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				w.speedY=0;
			}else{
				w.speedY = para[w.id-11][3];
			}
			if(w.direction == 2){
				int tempX = w.mapx;
				int tempY = w.mapy+w.speedY;
				w.mapy = tempY;
				if(id==100){
					g.drawRegion(imgBomb, 0, 0, 16, 71, 0, tempX, tempY, TopLeft);
				}else if(id==101){
					g.drawRegion(imgBomb2, 0, 0, 46, 36, 0, tempX, tempY, TopLeft);
				}else if(id==102){
					g.drawRegion(imgBomb3, 0, 0, 17, 17, 0, tempX, tempY, TopLeft);
				}
				if((tempY+w.height) > 530){
					dartles.removeElement(w);
				}
			}
		}
		g.setClip(0, 0, screenW, screenH);
	}

	/*能量防护--技能(己方)*/
	public void createEnergyProtection(int objectId, int mapx, int mapy, int direction){
		Weapon w = new Weapon();
		w.id = 17;
		w.objectId = objectId;
		w.direction = direction;
		//w.harm = para[w.id-11][4];
		w.mapx = mapx;
		w.mapy = mapy;
		w.width = para[w.id-11][1];
		w.height = para[w.id-11][2];
		w.speedY = para[w.id-11][3];
		energys.addElement(w);
	}
	/*画被攻击时的能量防护*/
	public void showEnergyProtection(SGraphics g, Role own){
		Weapon w = null;
		for(int i=0;i<energys.size();i++){
			w = (Weapon) energys.elementAt(i);
			if(protectFlag<2){
				protectFlag++;
			}else{
				if(SubmarineGameEngine.isMenu){
					protectIndex = 1;
				}else{
					protectIndex=(protectIndex+1)%3;
				}
				protectFlag=0;
			}
			g.drawRegion(imgProtect, protectIndex*w.width, 0, w.width, w.height, 0, own.mapx, own.mapy-35, TopLeft);
			if(protectIndex==2){
				SubmarineGameEngine.pFlag=-1;//能量保护结束
				SubmarineGameEngine.protectionFlag=false;
				energys.removeElement(w);
			}
		}
	}
	/*没被攻击时的能量保护*/
	public void showEnergyProtection2(SGraphics g, Role own){
		g.drawRegion(imgProtect, 0, 0, 92, 91, 0, own.mapx, own.mapy-35, TopLeft);
	}
	
	/*BOSS技能--呼叫空投*/
	public void createBossSkill(int objectId, int mapx, int mapy, int direction){
		Weapon w = new Weapon();
		w.id = 18;
		w.objectId = objectId;
		w.direction = direction;
		w.harm = para[w.id-11][4];
		w.mapx = mapx;
		w.mapy = mapy;
		w.width = para[w.id-11][1];
		w.height = para[w.id-11][2];
		w.speedY = para[w.id-11][3];
		airDrops.addElement(w);
		
	}
	/*显示BOSS呼叫空投技能*/
	public void showBossSkill(SGraphics g){
		g.setClip(0, 0, gameMapX, gameMapY);
		Weapon w = null;
		for(int i=0;i<airDrops.size();i++){
			w = (Weapon) airDrops.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				w.speedY=0;
			}else{
				w.speedY = para[w.id-11][3];
			}
			if(w.direction == 3){
				int tempX = w.mapx;
				int tempY = w.mapy-w.speedY;
				w.mapy = tempY;
				g.drawRegion(imgAirDrop, airDropIndex2*w.width, 0, w.width, w.height, 0, tempX, tempY, TopLeft);
				if(airDropFlag2==0){
					airDropFlag2++;
				}else{
					airDropIndex2=(airDropIndex2+1)%3;
					airDropFlag2=0;
				}
			}
			if(w.mapy >= 530){
				airDrops.removeElement(w);
			}
		}
		g.setClip(0, 0, screenW, screenH);
	} 

	/*BOSS技能--水雷*/
	public void createTorpedo(int objectId, int mapx, int mapy, int direction){
		Weapon w = new Weapon();
		w.id = 19;
		w.objectId = objectId;
		w.direction = direction;
		w.harm = para[w.id-11][4];
		w.mapx = mapx;
		w.mapy = mapy;
		w.terminalY = mapy;
		w.width = para[w.id-11][1];
		w.height = para[w.id-11][2];
		w.speedY = para[w.id-11][3];
		w.startTime = System.currentTimeMillis()/1000;
		torpedos.addElement(w);
	}
	/*显示水雷技能*/
	public void showTorpedo(SGraphics g){
		Weapon w = null;
		for(int i=0;i<torpedos.size();i++){
			w = (Weapon) torpedos.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				w.speedY=0;
			}else{
				w.speedY = para[w.id-11][3];
			}
			if(w.direction == 3){
				int tempX = w.mapx;
				int tempY = w.mapy-w.speedY;
				if(tempY<=100){
					tempY=w.mapy;
					w.endTime = System.currentTimeMillis()/1000;
					//System.out.println("torpedoTime2-torpedoTime: "+(w.endTime-w.startTime));
					int time = (int) ((w.terminalY-100)/(w.speedY*10) + 3); //计算水雷爆炸的时间
					//System.out.println("time:"+time);
					if(w.endTime-w.startTime>=time){
						torpedos.removeElement(w);
						/*SubmarineGameEngine.bombFlag = true;
						SubmarineGameEngine.bombTime1=System.currentTimeMillis()/100;*/
						SubmarineGameEngine.bombX = w.mapx+w.width/2-SubmarineGameEngine.bombImgW;
						SubmarineGameEngine.bombY = w.mapy+w.height-SubmarineGameEngine.bombImgH;
						Exploder exploder = new Exploder(SubmarineGameEngine.bombX,SubmarineGameEngine.bombY);
						SubmarineGameEngine.exploders[SubmarineGameEngine.eIndex] = exploder;
						if(SubmarineGameEngine.eIndex < SubmarineGameEngine.exploders.length-1){
							SubmarineGameEngine.eIndex ++;
						}else{
							SubmarineGameEngine.eIndex=0;
						}
					}
				}
				w.mapy = tempY;
				g.drawRegion(imgTorpedo, 0, 0, 23, 14, 0, tempX, tempY, TopLeft);
			}
		}
	}
	
	/*BOSS技能--网*/
	public void createNet(Role own, int objectId, int mapx, int mapy, int direction, boolean single){
		Weapon w = new Weapon();
		w.id = 20;
		w.objectId = objectId;
		w.direction = direction;
		w.harm = para[w.id-11][4];
		w.mapx = mapx;
		w.mapy = mapy;
		w.width = para[w.id-11][1];
		w.height = para[w.id-11][2];
		w.speedY = para[w.id-11][3];
		w.terminalX = own.mapx + own.width/2;
		w.terminalY = own.mapy + own.height/2;
		w.flagx = w.mapx+width/2-w.width/2;
		w.flagy = mapy+height/2-w.height/2;
		w.isSingle = single;
		
		netTime = System.currentTimeMillis()/1000;
		nets.addElement(w);
	}
	/*显示BOSS技能--网*/
	public void showNet(SGraphics g, Role own){
		Weapon w = null;
		for(int i=0;i<nets.size();i++){
			w = (Weapon) nets.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				w.speedY=0;
			}else{
				w.speedY = para[w.id-11][3];
			}
			if(w.direction == 3){
				int tempX = w.mapx;
				int tempY = w.mapy-w.speedY;
				if(tempY<=100){
					if(!w.isSingle){
						nets.removeElement(w);
					}else{
						tempY=w.mapy;
						tempX = w.mapx;
						netTime2 = System.currentTimeMillis()/1000;
						//System.out.println("(netTime2-netTime)=="+(netTime2-netTime));
						if(netTime2-netTime>=10){
							nets.removeElement(w);
						}
					}
				}else{
					if(w.isSingle){
						if(w.flagx >= (w.terminalX+own.mapx/2)){
							velocity2 = (float) ((w.flagx-w.terminalX)/(w.flagy-w.terminalY)); //tan@
							w.speedX = (int) (velocity2 * w.speedY);
							tempX -= w.speedX;
							w.mapx = tempX;
						}else{
							velocity2 = (float) ((w.terminalX-w.flagx)/(w.flagy-w.terminalY)); //tan@
							w.speedX = (int) (velocity2 * w.speedY);
							tempX += w.speedX;
							w.mapx = tempX;
						}
					}
				}
				w.mapy = tempY;
				w.mapx = tempX;
				g.drawRegion(imgNet, netIndex*9, 0, 9, 27, 0, tempX, tempY, TopLeft);
				if(netFlag==0){
					netFlag++;
				}else{
					netIndex=(netIndex+1)%3;
					netFlag=0;
				}
			}
		}
	}
	/*画潜艇被网住的效果*/
	public void showNetLocation(SGraphics g, Role own){
		SubmarineGameEngine.netTime2 = System.currentTimeMillis()/1000;
		if((SubmarineGameEngine.netTime2-SubmarineGameEngine.netTime)<=3){
			g.drawImage(imgNet2, own.mapx, own.mapy, TopLeft);
			SubmarineGameEngine.isMove=false;
		}else{
			SubmarineGameEngine.isMove=true;
		}
	}
	
	/*显示呼叫空投位置*/
	public void showAirDropLocation(SGraphics g){
		g.drawRegion(imgAirDrop2, airDropIndex*40, 0, 40, 40, 0, 100, 85, TopLeft);
		g.drawRegion(imgAirDrop2, airDropIndex*40, 0, 40, 40, 0, 180, 85, TopLeft);
		g.drawRegion(imgAirDrop2, airDropIndex*40, 0, 40, 40, 0, 260, 85, TopLeft);
		g.drawRegion(imgAirDrop2, airDropIndex*40, 0, 40, 40, 0, 340, 85, TopLeft);
		g.drawRegion(imgAirDrop2, airDropIndex*40, 0, 40, 40, 0, 420, 85, TopLeft);
		
		if(airDropFlag==0){
			airDropFlag++;
		}else{
			airDropIndex=(airDropIndex+1)%3;
			airDropFlag=0;
		}
		if(airDropIndex==2){
			isAirDrop=true;
		}
	}
	/*加载图片*/
	public void loadImage(){
		if(imgBomb == null || imgBomb2==null || imgBomb3==null || imgLaser == null
				|| imgProtect==null || imgTorpedo==null || imgNet==null || imgNet2==null){
			try {
				imgBomb = Image.createImage("/bomb.png");
				imgBomb2 = Image.createImage("/bomb2.png");
				imgBomb3 = Image.createImage("/bomb3.png");
				imgLaser = Image.createImage("/laser.png");
				imgProtect = Image.createImage("/protect.png");
				imgAirDrop = Image.createImage("/airDrop_1.png");
				imgAirDrop2 = Image.createImage("/airDrop_2.png");
				imgTorpedo = Image.createImage("/torpedo.png");
				imgNet = Image.createImage("/net_1.png");
				imgNet2 = Image.createImage("/net_2.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/*清空图片*/
	public void clear(){
		if(imgBomb!=null || imgBomb2 != null || imgBomb3!=null || imgLaser!=null
				|| imgProtect!=null || imgAirDrop!=null || imgAirDrop2!=null || imgTorpedo!=null
				|| imgNet!=null || imgNet2!=null){
			imgBomb=null;  imgProtect=null;
			imgBomb2=null; imgAirDrop=null;
			imgBomb2=null; imgAirDrop2=null;
			imgLaser=null; imgTorpedo=null;
			imgNet=null;   imgNet2=null;
		}
	}
	
	/*清除内存中的对象*/
	public void clearObject(){
		bombs.removeAllElements();
		lasers.removeAllElements();
		paraDrops.removeAllElements();
		//dartles.removeAllElements();
		energys.removeAllElements();
		airDrops.removeAllElements();
		torpedos.removeAllElements();
		nets.removeAllElements();
	}
	/*死亡时清楚技能*/
	public void clearSkill(){
		lasers.removeAllElements();
	}
}
