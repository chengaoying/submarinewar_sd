package submarine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cn.ohyeah.itvgame.model.GameAttainment;
import cn.ohyeah.itvgame.model.GameRecord;
import cn.ohyeah.stb.game.ServiceWrapper;

public class SaveGameRecord implements Common{
	private SubmarineGameEngine engine;
	public SaveGameRecord(SubmarineGameEngine engine) {
		this.engine = engine;
	}

	/*存游戏成就,用于排名--返回0成功*/
	public int saveGameAttainment(Role own, Role boss, int currLevel){
		ServiceWrapper sw = engine.getServiceWrapper();
		int medalNum = SubmarineGameEngine.medal + SubmarineGameEngine.medal2*4 + SubmarineGameEngine.medal3*16;
		GameAttainment attainment = new GameAttainment();
		attainment.setAttainmentId(SubmarineGameEngine.attainmentId);
		attainment.setScores(own.scores);
		attainment.setPlayDuration(own.eatCount);
		attainment.setRemark(String.valueOf(medalNum));
		sw.saveAttainment(attainment);
			
		return sw.getServiceResult();
	}
	
	/*存游戏记录*/
	public int saveGameRecord(Role own, Role boss, int currLevel){
		byte record[];
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);
		ServiceWrapper sw = engine.getServiceWrapper();
		try {
			dout.writeByte(currLevel);
			dout.writeByte(own.lifeNum);
			dout.writeByte(own.limitLife);
			dout.writeByte(own.eatCount);
			dout.writeByte(own.eatCount2);
			dout.writeByte(own.status);
			dout.writeByte(own.nonceLife);
			dout.writeByte(SubmarineGameEngine.medal);
			dout.writeByte(SubmarineGameEngine.medal2);
			dout.writeByte(SubmarineGameEngine.medal3);
			dout.writeByte(Propety.useHidePropNum);
			dout.writeByte(Propety.useLimitBooldPropNum);
			dout.writeByte(Propety.useMedigelPropNum);
			dout.writeBoolean(SubmarineGameEngine.dartleFlag);
			dout.writeByte(own.id);
			dout.writeByte(SubmarineGameEngine.g_status);
			dout.writeByte(SubmarineGameEngine.difficultLevel);
			dout.writeBoolean(SubmarineGameEngine.isOpenDifficult2);
			dout.writeBoolean(SubmarineGameEngine.isOpenDifficult3);
			dout.writeInt(own.scores2);
			dout.writeBoolean(SubmarineGameEngine.hasPropReward);
			dout.writeBoolean(SubmarineGameEngine.hasPropReward2);
			dout.writeBoolean(SubmarineGameEngine.hasPropReward3);
			if(SubmarineGameEngine.g_status==GAME_SUB_STATUS_PLAYING_BOSS){
				dout.writeInt(boss.nonceLife);
				dout.writeInt(boss.mapx);
				dout.writeInt(boss.mapy);
			}else {
				dout.writeByte(own.eatBlack);
				dout.writeByte(own.eatBlue);
				dout.writeByte(own.eatJisu);
				dout.writeByte(own.eatRed);
				dout.writeByte(own.eatYellow);
				dout.writeInt(own.harm);
			}
			System.out.println("own.id:" + own.id);
			System.out.println("own.lifeNum:" + own.lifeNum);
			System.out.println("own.limitLife:" + own.limitLife);
			System.out.println("own.eatBlack:" + own.eatBlack);
			System.out.println("own.eatBlue:" + own.eatBlue);
			System.out.println("own.eatJisu:" + own.eatJisu);
			System.out.println("own.eatRed:" + own.eatRed);
			System.out.println("own.eatYellow:" + own.eatYellow);
			System.out.println("harm: " + SubmarineGameEngine.harm);
			System.out.println("own.eatCount:" + own.eatCount);
			System.out.println("own.eatCount2:" + own.eatCount2);
			System.out.println("own.status:" + own.status);
			System.out.println("nonceLife: " + own.nonceLife);
			//System.out.println("id:" + own.id);
			System.out.println("g_status: " + SubmarineGameEngine.g_status);
			System.out.println("SubmarineGameEngine.hasPropReward: " + SubmarineGameEngine.hasPropReward);
			System.out.println("SubmarineGameEngine.hasPropReward2: " + SubmarineGameEngine.hasPropReward2);
			System.out.println("SubmarineGameEngine.hasPropReward3: " + SubmarineGameEngine.hasPropReward3);
			
			record = bout.toByteArray();
			GameRecord gameRecord = new GameRecord();
			gameRecord.setData(record);
			gameRecord.setRecordId(SubmarineGameEngine.attainmentId);
			gameRecord.setScores(own.scores);
			gameRecord.setPlayDuration(own.eatCount);
			int medalNum = SubmarineGameEngine.medal + SubmarineGameEngine.medal2*4 + SubmarineGameEngine.medal3*16;
			gameRecord.setRemark(String.valueOf(medalNum));
			//engineService.saveAttainment(attainment);
			sw.saveRecord(gameRecord);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				dout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					bout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return sw.getServiceResult();
	}
	
	/*读档--返回0成功*/
	public int loadGameRecord(){
		ServiceWrapper sw = engine.getServiceWrapper();
		GameRecord gameRecord = sw.readRecord(SubmarineGameEngine.attainmentId);
		if(!sw.isServiceSuccessful() || gameRecord==null){
			return -1;
		}
		ByteArrayInputStream bin = new ByteArrayInputStream(gameRecord.getData());
		DataInputStream din = new DataInputStream(bin);
		try {
			SubmarineGameEngine.currLevel = din.readByte();
			SubmarineGameEngine.lifeNum = din.readByte();
			SubmarineGameEngine.limitLife = din.readByte();
			SubmarineGameEngine.eatCount = din.readByte();
			SubmarineGameEngine.eatCount2 = din.readByte();
			SubmarineGameEngine.submarineStatus = din.readByte();
			SubmarineGameEngine.nonceLife = din.readByte();
			SubmarineGameEngine.medal = din.readByte();
			SubmarineGameEngine.medal2 = din.readByte();
			SubmarineGameEngine.medal3 = din.readByte();
			Propety.useHidePropNum = din.readByte();
			Propety.useLimitBooldPropNum = din.readByte();
			Propety.useMedigelPropNum = din.readByte();
			SubmarineGameEngine.dartleFlag = din.readBoolean();
			SubmarineGameEngine.submarineId = din.readByte();
			SubmarineGameEngine.gameStatus = din.readByte();
			SubmarineGameEngine.difficultLevel = din.readByte();
			SubmarineGameEngine.isOpenDifficult2 = din.readBoolean();
			SubmarineGameEngine.isOpenDifficult3 = din.readBoolean();
			SubmarineGameEngine.scores2 = din.readInt();
			SubmarineGameEngine.hasPropReward = din.readBoolean();
			SubmarineGameEngine.hasPropReward2 = din.readBoolean();
			SubmarineGameEngine.hasPropReward3 = din.readBoolean();
			if(SubmarineGameEngine.gameStatus==GAME_SUB_STATUS_PLAYING_BOSS){
				SubmarineGameEngine.bossLife = din.readInt();
				SubmarineGameEngine.bossMapx = din.readInt();
				SubmarineGameEngine.bossMapy = din.readInt();
			}else{
				SubmarineGameEngine.eatBlack= din.readByte();
				SubmarineGameEngine.eatBlue = din.readByte();
				SubmarineGameEngine.eatJisu = din.readByte();
				SubmarineGameEngine.eatRed = din.readByte();
				SubmarineGameEngine.eatYellow = din.readByte();
				SubmarineGameEngine.harm = din.readInt();
			}
			SubmarineGameEngine.scores = gameRecord.getScores();
			SubmarineGameEngine.eatCount = gameRecord.getPlayDuration();
			
			System.out.println("currLevel: " + SubmarineGameEngine.currLevel);
			System.out.println("lifeNum: " + SubmarineGameEngine.lifeNum);
			System.out.println("limitLife: " + SubmarineGameEngine.limitLife);
			System.out.println("eatBlack: " + SubmarineGameEngine.eatBlack);
			System.out.println("eatBlue: " + SubmarineGameEngine.eatBlue);
			System.out.println("eatJisu: " + SubmarineGameEngine.eatJisu);
			System.out.println("eatRed: " + SubmarineGameEngine.eatRed);
			System.out.println("eatYellow: " + SubmarineGameEngine.eatYellow);
			System.out.println("harm: " + SubmarineGameEngine.harm);
			System.out.println("eatCount: " + SubmarineGameEngine.eatCount);
			System.out.println("eatCount2: " + SubmarineGameEngine.eatCount2);
			System.out.println("submarineStatus: " + SubmarineGameEngine.submarineStatus);
			System.out.println("nonceLife: " + SubmarineGameEngine.nonceLife);
			System.out.println("recordFlag: " + SubmarineGameEngine.recordFlag);
			//System.out.println("id:" + SubmarineGameEngine.id);
			System.out.println("gameStatus: " + SubmarineGameEngine.gameStatus);
			System.out.println("SubmarineGameEngine.hasPropReward: " + SubmarineGameEngine.hasPropReward);
			System.out.println("SubmarineGameEngine.hasPropReward2: " + SubmarineGameEngine.hasPropReward2);
			System.out.println("SubmarineGameEngine.hasPropReward3: " + SubmarineGameEngine.hasPropReward3);
		} catch (Exception e) {
			System.out.println("没有游戏记录,开始新的游戏!");
			return -1;
			//e.printStackTrace();
		}finally{
			try {
				din.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					bin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return sw.getServiceResult();
	}
	
}
