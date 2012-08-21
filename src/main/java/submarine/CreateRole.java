package submarine;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.util.RandomValue;

/**
 * ����ս��
 * @author xiaochen
 *
 */
public class CreateRole implements Common {
	
	private int tempx,tempy, bossTempx,bossTempy;
	private float temp1;
	public static Image imgOwn, imgOwn2;
	private Image imgYellow, imgRed, imgBlue, imgBlack, imgJisu, imgLife, imgBoss;
	private Image imgBossHide;
	String[] boss = { "/boss1.png", "/boss2.png", "/boss3.png", "/boss4.png", "/boss5.png"  };
	String[] bossHide = {"/boss4hide.png","/boss5hide.png"};
	public static int count = 1;  //��Ļ��npc������
	public static float amount = 0, amount2 = 0; //����Ǳͧ������
	public static Vector npcs = new Vector();
    private int bossFlag=0,bossInterval, boss4Flag, boss4Index;
    private long sTime1, sTime4, eTime, torpedoTime, torpedoTime2, torpedoTime3,torpedoTime4;
    private long jiasuTime, jiasuTime2, wangTime, wangTime2, yinshenTime, yinshenTime2, hideFlag, hideIndex;
    private long netTime, netTime2, kongtouTime,kongtouTime2, shuileiTime, shuileiTime2;
    private int bossF=0;
    private float bossSpeed=0;
	
    /*ˢ�ַ�ʽ*/
    public static int npcId[][] = {
	  /*ÿ�ض�Ӧ��npc��ID (1-10)*/
			{1},  
			{1, 2, 3},
			{1, 2, 3, 5},
			{2, 3, 4, 5},
			{2, 3, 4, 5},
    };
	
    /*own���� */
    public static int[][] ownPara = {
	  /*0-ͼƬ��,1-ͼƬ��,2-����ֵ,3-�ƶ��ٶ�,4-������,5-������,6-�˺�*/
   	  {90, 34, 60, 10, 185, 85, 100},
	  {90, 34, 100, 13, 185, 85, 120},
	  {90, 34, 60, 16, 185, 85, 200}
    };
	
   /*npc����*/
    public static int npcPara[][] = {
	  /*0-ͼƬ���,1-ͼƬ�߶�,2-NPC�ƶ��ٶ�,3-�ṩ�Ļ���, 4-Ѫ��*/
	  {85, 28, 8, 80, 80},  	 //yellow
	  {85, 28, 10, 100, 100},	 //red
	  {85, 28, 10, 100, 100},	 //blue
	  {85, 28, 12, 220, 220},	 //black
	  {85, 28, 15, 180, 180},	 //gray
    };
    
	/*BOOS���� (boss ID 31-40)*/
    public static int bossPara[][] = {
    	/*0-ͼƬ���,1-ͼƬ�߶�,2-Ѫ��,3-������,4-�ƶ��ٶ�,5-�����ӵ���Ƶ��, 6-�ṩ�Ļ���, 7-ֹͣʱ��*/
    	{88, 34, 2000, 30, 6, 25, 1000, 60},
    	{87, 44, 2500, 35, 4, 25, 1500, 50},
    	{95, 32, 3000, 40, 6, 20, 2500, 50},
    	{89, 40, 3500, 45, 8, 20, 3500, 40},
    	{140, 53, 4000, 50, 6, 15, 5000, 40},
    };
    
	/*�����û���ͧ*/
	public Role createOwn(int difficult){
		Role own = new Role();
		own.id = SubmarineGameEngine.submarineId;
		own.lifeNum = 3;
		own.direction = 1;
		own.eatCount = 0;
		own.eatCount2=0;
		own.eatBlue=0;
		own.eatRed=0;
		own.eatYellow=0;
		own.frame = 0;
		//own.money = SubmarineGameEngine.money;
		own.status = 0;
		own.scores = 0;
		own.scores2=0;
		own.attack = ownPara[own.id-100][6];
		own.width = ownPara[own.id-100][0];
		own.height = ownPara[own.id-100][1];
		own.nonceLife = ownPara[own.id-100][2];
		own.limitLife = ownPara[own.id-100][2];
		own.speed = ownPara[own.id-100][3];
		own.mapx = ownPara[own.id-100][4];
		own.mapy = ownPara[own.id-100][5];
		return own;
	}
	
	/*�û���ͧ����*/
	public Role revive(){
		Role own = new Role();
		own.id = SubmarineGameEngine.submarineId;
		own.lifeNum = SubmarineGameEngine.lifeNum;
		own.direction = 1;
		own.eatCount = SubmarineGameEngine.eatCount;
		own.eatCount2 = SubmarineGameEngine.eatCount2;
		own.eatBlue=SubmarineGameEngine.eatBlue;
		own.eatRed=SubmarineGameEngine.eatRed;
		own.eatYellow=SubmarineGameEngine.eatYellow;
		own.frame = 0;
		own.harm = SubmarineGameEngine.harm;
		//own.money = SubmarineGameEngine.money;
		own.status = 0;
		own.scores = SubmarineGameEngine.scores;
		own.scores2 = SubmarineGameEngine.scores2;
		own.attack = ownPara[own.id-100][6];
		own.width = ownPara[own.id-100][0];
		own.height = ownPara[own.id-100][1];
		own.nonceLife = SubmarineGameEngine.limitLife;
		own.limitLife = SubmarineGameEngine.limitLife;
		/*if(SubmarineGameEngine.id==SubmarineGameEngine.submarineId){ //��¼���û��õ�Ǳͧ�͵�ǰѡ���Ǳͧһ��
			own.nonceLife = SubmarineGameEngine.limitLife;//SubmarineGameEngine.nonceLife==0?ownPara[own.id-100][2]:SubmarineGameEngine.nonceLife; 
			own.limitLife = SubmarineGameEngine.limitLife;
		}else{
			own.nonceLife = ownPara[own.id-100][2];
			own.limitLife = ownPara[own.id-100][2];
			SubmarineGameEngine.limitLife = own.limitLife;
		}*/
		own.speed = ownPara[own.id-100][3];
		own.mapx = ownPara[own.id-100][4];
		own.mapy = ownPara[own.id-100][5];
		return own;
	}
	
	/*��ʾ�û���ͧ*/
	public void showOwn(SGraphics g, Role own){
		if(own.status==0){
			g.drawRegion(imgOwn, ownPara[0][0]*(own.id-100), 0, ownPara[0][0], ownPara[0][1], 
					own.direction == 0 ? Sprite.TRANS_MIRROR : 0, own.mapx, own.mapy, TopLeft);
		} else if(own.status==2){
			g.drawRegion(imgOwn2, ownPara[0][0]*(own.id-100), 0, ownPara[0][0], ownPara[0][1], 
					own.direction == 0 ? Sprite.TRANS_MIRROR : 0, own.mapx, own.mapy, TopLeft);
		}
		
	}
	
	/*�����״�������y������ֵ*/
	public int yaxis(int y){
		Integer t1 = new Integer(y); 
		float t2 = t1.floatValue();
		int _y = (int) (t2/5.3);
		return _y;
	}
	
	/*ת����float����*/
	public float parseFloat(int sp){
		Integer temp = new Integer(sp);
		float f = temp.floatValue();
		return f;
	}
	
	/*����NPC*/
	public void createNpc(int num, int currLevel, int difficultLevel){
		Role npc = null;
		if(count <= num+difficultLevel*2){
			npc = new Role();
			npc.id = npcId[currLevel-1][RandomValue.getRandInt(0, npcId[currLevel-1].length)];
			npc.direction = RandomValue.getRandInt(2);
			npc.width = npcPara[npc.id-1][0];
			npc.height = npcPara[npc.id-1][1];
			npc.mapy = RandomValue.getRandInt(0,200) + 200;
			npc.speed = npcPara[npc.id-1][2] + currLevel+difficultLevel*3;
			npc.scores = npcPara[npc.id-1][3]+difficultLevel*100;
			npc.nonceLife = npcPara[npc.id-1][4]+difficultLevel*20;
			npc.role = new Role();  //�״�������npc
			if(npc.direction == 0){
				npc.mapx = gameMapX + 200;
			} else{
				npc.mapx = -npcPara[npc.id-1][0] -150;
			}
			amount++;
			count++;
			npcs.addElement(npc);
		}
	}
	
	/*��ʾNPC*/
	public void showNpc(SGraphics g, int difficultLevel){
		eTime = System.currentTimeMillis()/1000;
		torpedoTime = System.currentTimeMillis()/1000;
		torpedoTime3 = System.currentTimeMillis()/1000;
		Role npc = null;
		for(int i=npcs.size()-1;i>=0;i--){
			npc = (Role)npcs.elementAt(i);
			if(SubmarineGameEngine.isMenu){
				npc.speed=0;
			}else{
				if(SubmarineGameEngine.slowFlag){ //����
					npc.speed = npcPara[npc.id-1][2]/3;
				}else{
					npc.speed = npcPara[npc.id-1][2];
				}
			}
			if(npc.direction == 0){
				tempx = npc.mapx;
				tempx -= npc.speed;
				npc.mapx = tempx;
			} else {
				tempx = npc.mapx;
				tempx += npc.speed;
				npc.mapx = tempx;
			}
			tempy = npc.mapy;
			/*��ɫǱͧ*/
			g.setClip(0, 0, gameMapX, gameMapY);
			if(npc.id==1){
				g.drawRegion(imgYellow, 0, 0, 85, 28, npc.direction == 0 ? 0 : Sprite.TRANS_MIRROR, tempx, tempy, TopLeft);
				if((tempx + npcPara[npc.id-1][0] < 0) && npc.direction == 0){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if((npc.direction==1) && (tempx > gameMapX)){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
			}
			
			/*��ɫǱͧ*/
			if(npc.id==2){
				g.drawRegion(imgRed, 0, 0, npcPara[npc.id-1][0], npcPara[npc.id-1][1], 
						npc.direction == 0 ? 0 : Sprite.TRANS_MIRROR, tempx, tempy, TopLeft);
				if((tempx + npcPara[npc.id-1][0] < 0) && npc.direction == 0){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if((npc.direction==1) && (tempx > gameMapX)){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if(!SubmarineGameEngine.isMenu){
					if(eTime-sTime1>=3 && (npc.mapx+npc.width)<gameMapX && (npc.mapx+npc.width)>0){
						weapon.createBomb(null, npc.id, tempx, tempy, 3, npc.width, npc.height); //�����ӵ�
						sTime1 = System.currentTimeMillis()/1000;
					}
				}
			}
			
			/*��ɫǱͧ*/
			if(npc.id==3){
				g.drawRegion(imgBlue, 0, 0, npcPara[npc.id-1][0], npcPara[npc.id-1][1], 
						npc.direction == 0 ? 0 : Sprite.TRANS_MIRROR, tempx, tempy, TopLeft);
				if((tempx + npcPara[npc.id-1][0] < 0) && npc.direction == 0){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if((npc.direction==1) && (tempx > gameMapX)){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if(!SubmarineGameEngine.isMenu){
					if(difficultLevel==0){
						if(eTime-sTime1>=3 && (npc.mapx+npc.width)<gameMapX && (npc.mapx+npc.width)>0){
							weapon.createBomb(null, npc.id, tempx, tempy, 3, npc.width, npc.height); //�����ӵ�
							sTime1 = System.currentTimeMillis()/1000;
						}
					}else{
						if(torpedoTime-torpedoTime2>=3 && (npc.mapx+npc.width)<gameMapX && (npc.mapx+npc.width)>0){
							weapon.createTorpedo(npc.id, tempx, tempy, 3); //ˮ��
							torpedoTime2 = System.currentTimeMillis()/1000;
						}
					}
				}
			}
			/*��Ǳͧ*/
			if(npc.id==4){
				g.drawRegion(imgBlack, 0, 0, npcPara[npc.id-1][0], npcPara[npc.id-1][1], 
						npc.direction == 0 ? 0 : Sprite.TRANS_MIRROR, tempx, tempy, TopLeft);
				if((tempx + npcPara[npc.id-1][0] < 0) && npc.direction == 0){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if((npc.direction==1) && (tempx > gameMapX)){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if(!SubmarineGameEngine.isMenu){
					if(difficultLevel==0){ //���Ѷ�ֻ�����ӵ�
						if(eTime-sTime1>=3 && (npc.mapx+npc.width)<gameMapX && (npc.mapx+npc.width)>0){
							weapon.createBomb(null, npc.id, tempx, tempy, 3, npc.width, npc.height); //�����ӵ�
							sTime1 = System.currentTimeMillis()/1000;
						}
					}else{
						if(torpedoTime3-torpedoTime4>=3 && (npc.mapx+npc.width)<gameMapX && (npc.mapx+npc.width)>0){
							weapon.createTorpedo(npc.id, tempx, tempy, 3); //ˮ��
							torpedoTime4 = System.currentTimeMillis()/1000;
						}
					}
				}
			}
			/*����Ǳͧ*/
			if(npc.id==5){
				g.drawRegion(imgJisu, 0, 0, npcPara[npc.id-1][0], npcPara[npc.id-1][1], 
						npc.direction == 0 ? 0 : Sprite.TRANS_MIRROR, tempx, tempy, TopLeft);
				if((tempx + npcPara[npc.id-1][0] < 0) && npc.direction == 0){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if((npc.direction==1) && (tempx > gameMapX)){
					npcs.removeElement(npc);
					amount--;
					count--;
				}
				if(!SubmarineGameEngine.isMenu){
					if(eTime-sTime4>=3 && (npc.mapx+npc.width)<gameMapX && (npc.mapx+npc.width)>0){
						weapon.createBomb(null, npc.id, tempx, tempy, 3, npc.width, npc.height); //�����ӵ�
						sTime4 = System.currentTimeMillis()/1000;
					}
				}
			}
			g.setClip(0, 0, screenW, screenH);
		
			/*�״������е�npc*/
			Role role = npc.role; 
			temp1 = parseFloat(npc.mapx+150)/5;
			role.mapx = (int) temp1;
			role.direction = npc.direction;
			int color = returnColor(npc.id);
			if((role.mapx+495)<634 && (role.mapx+495)>495){
				g.setColor(color);
				if(role.direction==0){
					g.fillRect(role.mapx+480, yaxis(npc.mapy)+48, 5, 5);
				}else{
					g.fillRect(role.mapx+495, yaxis(npc.mapy)+48, 5, 5);
				}
			}
		}
	}
	
	/*������ɫֵ*/
	private int returnColor(int id){
		if(id==1){
			return 0Xc6c737;
		}else if(id==2){
			return 0Xff0000;
		}else if(id==3){
			return 0X00a4fd;
		}else if(id==4){
			return 0X3f3f40;
		}else if(id==5){
			return 0X7e7d7;
		}
		return -1;
	}
	
	/*����BOSS*/
	public Role createBoss(int gateId,int difficultLevel){
		Role boss = new Role();
		boss.id = 31;
		boss.status=0;
		//boss.mapx = 300;
		//boss.mapy = 530;
		boss.mapx=-100;
		boss.mapy=410;
		boss.direction = RandomValue.getRandInt(2);
		boss.width = bossPara[gateId-1][0];
		boss.height = bossPara[gateId-1][1];
		boss.nonceLife = bossPara[gateId-1][2]+difficultLevel*1500;
		boss.attack = bossPara[gateId-1][3]+difficultLevel*10;
		boss.speed = bossPara[gateId-1][4]+difficultLevel*5;
		boss.scores = bossPara[gateId-1][6]+difficultLevel*500;
		
		kongtouTime = System.currentTimeMillis()/1000;
		shuileiTime = System.currentTimeMillis()/1000;
		yinshenTime = System.currentTimeMillis()/1000;
		jiasuTime = System.currentTimeMillis()/1000;
		wangTime = System.currentTimeMillis()/1000;
		netTime = System.currentTimeMillis()/1000;
		return boss;
	}
	
	/*��ʾBOSS*/
	public void showBoss(Role own, Role boss, SGraphics g, int gateId, int difficultLevel){
		if(SubmarineGameEngine.isMenu){
			boss.speed=0;
		}else{
			if(SubmarineGameEngine.slowFlag){
				boss.speed = bossPara[boss.id-31][4]*2/5;
			} else {
				boss.speed = bossSpeed==0?bossPara[boss.id-31][4]:bossSpeed;
			}
			/*����BOSS���к�ֹͣ*/
			SubmarineGameEngine.bossTime2 = System.currentTimeMillis()/1000;
			if((SubmarineGameEngine.bossTime2-SubmarineGameEngine.bossTime)>=15){//boss����ʱ��
				if(bossF<=bossPara[boss.id-31][7]){
					bossF++;
					boss.speed=0;
				}else{
					bossF=0;
					boss.speed = bossSpeed==0?bossPara[boss.id-31][4]:bossSpeed;
					SubmarineGameEngine.bossTime=System.currentTimeMillis()/1000;
				}
			}
		}
		
		bossTempx = boss.mapx;
		bossTempy = boss.mapy;
		if(bossTempx+boss.width<= 0){
			g.setClip(0, 0, screenW, gameMapY);
			bossTempx += 2;//boss.speed;
			boss.mapx = bossTempx;
		} else {
			g.setClip(0, 0, screenW, screenH);
			if(boss.direction == 0){
				bossTempx -= boss.speed;
				boss.mapx = bossTempx;
				if(boss.mapx <= 0){
					boss.direction = 1;
				}
			} else{
				bossTempx += boss.speed;
				boss.mapx = bossTempx;
				if((boss.mapx + boss.width) >= gameMapX){
					boss.direction = 0;
				}
			}
		}
		if(boss.status!=2){
			g.drawRegion(imgBoss, 0, 0, imgBoss.getWidth(), imgBoss.getHeight(), 
					boss.direction == 0 ? 0 : Sprite.TRANS_MIRROR, bossTempx, bossTempy, TopLeft);
			drawNonceLife(g, boss, gateId, difficultLevel);
		} else {
			if(boss4Flag  <= 4){
				boss4Flag ++;
				boss4Index = 0;
		    } else {
		    	boss4Flag = 1;
		    	boss4Index = (boss4Index+1)%8;
		    }
			g.drawRegion(imgBossHide, boss4Index*(imgBossHide.getWidth()/8), 0, imgBossHide.getWidth()/8, imgBossHide.getHeight(), 
					boss.direction == 0 ? 0 : Sprite.TRANS_MIRROR, bossTempx, bossTempy, TopLeft);
			drawNonceLife(g, boss, gateId, difficultLevel);
		}
		if(bossFlag  <= bossPara[0][5]){
			bossFlag ++;
	    	bossInterval = 0;
	    } else {
	    	bossFlag = 1;
	    	bossInterval = 1;
	    }
		if(bossInterval == 1){
			if(!SubmarineGameEngine.isMenu){
				weapon.createBomb(own, boss.id, bossTempx, bossTempy, 3, boss.width, boss.height); //�����ӵ�
			}
		} 
		
		if(!SubmarineGameEngine.isMenu){  //������ͣ״̬
			kongtouTime2 = System.currentTimeMillis()/1000;
			shuileiTime2 = System.currentTimeMillis()/1000;
			yinshenTime2 = System.currentTimeMillis()/1000;
			jiasuTime2 = System.currentTimeMillis()/1000;
			wangTime2 = System.currentTimeMillis()/1000;
			netTime2 = System.currentTimeMillis()/1000;
			
			/*���п�Ͷ*/
			if((kongtouTime2-kongtouTime>(25-difficultLevel*4) && (gateId==5)) 
					|| (kongtouTime2-kongtouTime>35 && difficultLevel==1 && gateId ==4)
					|| ((kongtouTime2-kongtouTime>30 && difficultLevel==2 && gateId ==4)
					|| (kongtouTime2-kongtouTime>35 && difficultLevel==2 && gateId ==3))){
				if(!Weapon.isAirDrop){
					weapon.showAirDropLocation(g);
				}else{
					int x = 100;
					int y = 530;
					for(int i=0;i<5;i++){
						weapon.createBossSkill(boss.id, x, y, 3);
						x += 80;
					}
					kongtouTime = System.currentTimeMillis()/1000;
					Weapon.isAirDrop=false;
					Weapon.airDropFlag=0;
					Weapon.airDropIndex=0;
				}
			}
			
			/*����*/
			if(yinshenTime2-yinshenTime >(20-difficultLevel*2) && (gateId==4)
					|| (yinshenTime2-yinshenTime >(20-difficultLevel*2) && /*difficultLevel==2 &&*/ gateId ==5)){
				boss.status = 2; //����״̬
				if(hideFlag  <= 150){ //15��
					hideFlag ++;
			    	hideIndex = 0;
			    } else {
			    	hideFlag = 1;
			    	hideIndex = 1;
			    }
				if(hideIndex==1){
					boss.status=0;
					yinshenTime = System.currentTimeMillis()/1000;
				}
			}
			/*����*/
			if(jiasuTime2-jiasuTime >(20-difficultLevel*3) && (gateId==5)
					|| (jiasuTime2-jiasuTime >30 && difficultLevel==1 && gateId ==4)
					|| (jiasuTime2-jiasuTime >40 && difficultLevel==1 && gateId ==1)
					|| (jiasuTime2-jiasuTime >30 && difficultLevel==2 && gateId ==2)){
				boss.speed = bossPara[boss.id-31][4]+5; 
				bossSpeed = boss.speed;
				if(hideFlag  <= 100){
					hideFlag ++;
			    	hideIndex = 0;
			    } else {
			    	hideFlag = 1;
			    	hideIndex = 1;
			    }
				if(hideIndex==1){
					boss.speed = bossPara[gateId-1][4];
					jiasuTime = System.currentTimeMillis()/1000;
				}
			}
			
			/*ˮ��*/
			if((shuileiTime2-shuileiTime>(30-difficultLevel*5) && (gateId==3 || gateId==5 || gateId==4))
					|| (shuileiTime2-shuileiTime>30 && difficultLevel==2 && gateId ==1)
					|| (shuileiTime2-shuileiTime>30 && difficultLevel==1 && gateId ==2)){
				int m = 10;
				int n = boss.mapy;
				for(int i=0;i<4;i++){
					weapon.createTorpedo(boss.id, m, n, 3);
					m += 140;
				}
				shuileiTime = System.currentTimeMillis()/1000;
			}
			/*ȫ����*/
			if(wangTime2-wangTime>15 && (gateId==2 || gateId==5)){
				int m = 50;
				int n = boss.mapy;
				for(int i=0;i<4;i++){
					weapon.createNet(own, boss.id, m, n, 3, false);
					m += 120;
				}
				wangTime = System.currentTimeMillis()/1000;
			}
			/*һ����*/
			if((netTime2-netTime>10 && (gateId==4))
					|| (netTime2-netTime>10 && difficultLevel==2 && gateId ==3)
					|| (netTime2-netTime>25 && difficultLevel==1 && gateId ==1)
					|| (netTime2-netTime>20 && difficultLevel==2 && gateId ==1)
					|| (netTime2-netTime>20 && difficultLevel==1 && gateId ==3)){
				weapon.createNet(own, boss.id, boss.mapx, boss.mapy, 3, true);
				netTime = System.currentTimeMillis()/1000;
			}
		}
	}
	
	private void drawNonceLife(SGraphics g, Role role, int gateId, int difficultLevel){
		g.drawRegion(imgLife, 0, 0, imgLife.getWidth(), imgLife.getHeight(), 0, role.mapx+8, role.mapy-10, TopLeft);
		g.setColor(255, 0, 0);
		g.fillRect(role.mapx+8, role.mapy+1-10, role.nonceLife*75/(bossPara[gateId-1][2]+difficultLevel*1500), 5);
	}
	
	/*����ͼƬ*/
	public void loadImage(int currLevel){
		if(imgBoss == null || imgOwn==null || imgYellow==null || imgRed==null || imgOwn2==null
				|| imgLife==null || imgBlue==null || imgBlack==null || imgJisu==null || imgBossHide==null){
			try {
				imgBoss = Image.createImage(boss[currLevel-1]);
				if(currLevel>=4){
					imgBossHide = Image.createImage(bossHide[currLevel-4]);
				}
				imgOwn = Image.createImage("/own.png");
				imgOwn2 = Image.createImage("/hiding.png");
				imgYellow = Image.createImage("/yellow.png");
				imgRed = Image.createImage("/red.png");
				imgLife = Image.createImage("/nonceLife.jpg");
				imgBlue = Image.createImage("/blue.png");
				imgBlack = Image.createImage("/black.png");
				imgJisu = Image.createImage("/jisu.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*��ͼƬ��Ϊnull*/
	public void clear(){
		if(imgOwn!=null || imgYellow!=null || imgRed!=null || imgBoss!=null 
			 || imgLife!=null || imgOwn2!=null || imgBlue!=null
			 || imgBlack!=null || imgJisu!=null || imgBossHide!=null){
			imgOwn=null; imgBoss=null;
			imgLife=null; imgRed=null;
			imgYellow=null;imgBossHide=null;
			imgOwn2=null; imgBlue=null;
			imgBlack=null;imgJisu=null;
		}
	}
	
	/*�������*/
	public void clearObject(){
		npcs.removeAllElements();
	}
}
