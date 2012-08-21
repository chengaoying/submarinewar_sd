package submarine;

import cn.ohyeah.stb.game.SGraphics;


/**
 * ±¬Õ¨Ð§¹ûÀà
 * @author Administrator
 *
 */
public class Exploder implements Common{

	private int mapx;
	private int mapy;
	private int[] frame={0,1,2,3,4,5,6,7,8,9,10,11,12,13};
	private int i;
	
	public Exploder(int mapx, int mapy){
		this.mapx = mapx;
		this.mapy = mapy;
	}
	
	public void drawExplode(SGraphics g, DrawGame drawGame){
		if(i<13){
			i++;
		}
		drawGame.drawBurstEffect(g, mapx, mapy, frame[i]);
	}
}
