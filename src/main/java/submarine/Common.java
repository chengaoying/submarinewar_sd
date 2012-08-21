package submarine;


import javax.microedition.lcdui.Graphics;

/**
 * 
 * @author xiaochen
 *
 */
public interface Common {

  public static final int GAME_STATUS_INIT = 0;
  public static final int GAME_STATUS_MAIN_MENU = 1;
  public static final int GAME_STATUS_PLAYING = 2;
  public static final int GAME_STATUS_PALYING_MENU = 3;
  public static final int GAME_STATUS_SELECT_SUBMIRINE = 4;
  public static final int GAME_STATUS_SELECT_LEVEL = 5;
  
  public static final int GAME_SUB_STATUS_PLAYING_NPC = 20;
  public static final int GAME_SUB_STATUS_PLAYING_BOSS = 21;


  public static final int GAME_STATUS_SHOP = 6;
  public static final int GAME_STATUS_INTRO = 7;
  public static final int GAME_STATUS_PURCHASING = 8;
  //public static final int GAME_STATUS_PURCHASING_STATE = 9;
  //public static final int GAME_STATUS_RECHARGE = 10;
  //public static final int GAME_STATUS_RECHARGE_STATE = 11;
  public static final int GAME_STATUS_PASS = 12;
  public static final int GAME_STATUS_RANKING = 13;
  public static final int GAME_STAUS_NO_RECORD = 14;
  public static final int GAME_STATUS_GAMEOVER = 15;
  public static final int GAME_STATUS_DIFFICULTYLEVEL = 16;
  public static final int GAME_STATUS_HELP = 17;
  
  /*公用变量*/
  public static int screenW = SubmarineGameEngine.ScrW;
  public static int screenH = SubmarineGameEngine.ScrH;
  public static int gameMapX = 910; //游戏区域大小
  public static int gameMapY = 615; //游戏区域大小	
  public static Weapon weapon = new Weapon();
  public static Attacks attacks = new Attacks();
  public static byte TopLeft = Graphics.TOP | Graphics.LEFT;
  public static byte TopRight = Graphics.TOP | Graphics.RIGHT;
  
}
