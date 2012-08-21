package submarine;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import cn.ohyeah.stb.game.SGraphics;


/**
 * ����������
 * @author xiaochen
 *
 */
public class Weapon implements Common {

	int id;					//����ID (11-30)
	int objectId;			//����������	ID
	int speedY;				//y���ƶ��ٶ�
	int speedX;				//x���ƶ��ٶ�
	int direction;			//�ƶ����� 2��3��
	int harm;				//��ɵ��˺�
	int mapx;				//X������
	int mapy;				//Y������
	int width;				//�������
	int height;				//�����߶�
	long startTime;			//��ʼʱ��
	long endTime;			//����ʱ��
	float terminalX;		//�յ������
	float terminalY;		//�յ�������(������ˮ�׵Ļ�,��ʾˮ�׵���ʼ����)
	float flagx;			//��ʶx
	float flagy;			//��ʶy
	int random;				//�������
	boolean isSingle;		//��������
	//boolean isHit;			//�Ƿ����(���ڼ���������)
	
	private int tempx=0,tempy=0;
	private Image imgBomb, imgBomb2, imgBomb3, imgLaser, imgProtect;
	private Image imgAirDrop, imgAirDrop2, imgTorpedo, imgNet, imgNet2;
	public Vector bombs = new Vector();
	public Vector paraDrops = new Vector();
	public Vector dartles = new Vector();
	public Vector lasers = new Vector();
	public Vector energys = new Vector();
	private float velocity, velocity2;  
	public static float bombAmount;		//�����ӵ�����
	public static float hitNumber;		//��������
	private int laserIndex, laserFlag, protectFlag, protectIndex;
	private int bombIndex, bombFlag;
	private Random ran = new Random();
	public Vector airDrops = new Vector(); //�з�BOSS����
	public Vector torpedos = new Vector();
	public Vector nets = new Vector();
	public static int airDropFlag=0, airDropIndex=0;
	private int airDropFlag2=0, airDropIndex2=0, netFlag, netIndex;
	public static boolean isAirDrop=false;  //�з�BOSS��Ͷλ����ʾ��־
	public static boolean  isNet = false; 
	private long netTime, netTime2; //����ˮ����ͣ��ʱ��
	
	/*��������*/
	private int para[][]={
			/*0-����ID,1-�������,2-�����߶�,3-�����ٶ�,4-�����˺�,5-�ƶ�����(2��3��)*/
			{11,16,71,10,100,2},		//��ͨ����1
			{12,46,36,10,150,2},		//��ͨ����2
			{13,10,45,12,200,2},		//��ͨ����3
			{14,16,71,15,300,2}, 	    //��Ͷ(����)
			{15,46,36,10,100,2},		//����(����)
			{16,163,368,0,8,2},			//��͸���ⵯ
			{17,92,91,0,100,2},			//��������
			{18,30,125,10,30,3},		//��Ͷ(�з�BOSS)
			{19,23,14,8,30,3},	    	//ˮ��(�з�BOSS)
			{20,9,27,9,0,3}, 			//��(�з�BOSS)	
	};
	
	/**
	 * ������ͨ����
	 * @param own �����ٶ���(����и���Ч��)
	 * @param objectId ������ID
	 * @param mapx ��ͨ����������
	 * @param mapy ��ͨ����������
	 * @param direction ��ͨ��������(2��3��)
	 * @param width �����ӵ��ߵĿ�(���ڶ�λ�ӵ�������)
	 * @param height �����ӵ��ߵĸ� (���ڶ�λ�ӵ�������)
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
	
	/*����ͨ����*/
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
	
	/*�з�Ǳͧ��ͨ����*/
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
	
	/*����   ��͸���ⵯ--����*/
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
	/*��  ��͸���ⵯ*/
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
	
	/*����   ���п�Ͷ--����*/
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
	/*��ʾ   ���п�Ͷ--����*/
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
	/*����   ����--����*/
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
	/*��ʾ   ����--����*/
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

	/*��������--����(����)*/
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
	/*��������ʱ����������*/
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
				SubmarineGameEngine.pFlag=-1;//������������
				SubmarineGameEngine.protectionFlag=false;
				energys.removeElement(w);
			}
		}
	}
	/*û������ʱ����������*/
	public void showEnergyProtection2(SGraphics g, Role own){
		g.drawRegion(imgProtect, 0, 0, 92, 91, 0, own.mapx, own.mapy-35, TopLeft);
	}
	
	/*BOSS����--���п�Ͷ*/
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
	/*��ʾBOSS���п�Ͷ����*/
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

	/*BOSS����--ˮ��*/
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
	/*��ʾˮ�׼���*/
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
					int time = (int) ((w.terminalY-100)/(w.speedY*10) + 3); //����ˮ�ױ�ը��ʱ��
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
	
	/*BOSS����--��*/
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
	/*��ʾBOSS����--��*/
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
	/*��Ǳͧ����ס��Ч��*/
	public void showNetLocation(SGraphics g, Role own){
		SubmarineGameEngine.netTime2 = System.currentTimeMillis()/1000;
		if((SubmarineGameEngine.netTime2-SubmarineGameEngine.netTime)<=3){
			g.drawImage(imgNet2, own.mapx, own.mapy, TopLeft);
			SubmarineGameEngine.isMove=false;
		}else{
			SubmarineGameEngine.isMove=true;
		}
	}
	
	/*��ʾ���п�Ͷλ��*/
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
	/*����ͼƬ*/
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
	/*���ͼƬ*/
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
	
	/*����ڴ��еĶ���*/
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
	/*����ʱ�������*/
	public void clearSkill(){
		lasers.removeAllElements();
	}
}
