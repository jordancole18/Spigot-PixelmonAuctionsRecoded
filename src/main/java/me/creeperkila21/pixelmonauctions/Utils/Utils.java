package me.creeperkila21.pixelmonauctions.Utils;

import java.util.ArrayList;
import java.util.List;

import me.creeperkila21.pixelmonauctions.PixelmonAuctions;
import me.creeperkila21.pixelmonauctions.config.FileManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

public class Utils {

	public static int getTimeInSeconds(String msg){
		
		int time = 0;
		
		if(msg.contains("s")){
			time = Integer.parseInt(msg.replace("s", ""));
		}else if(msg.contains("m")){
			time = Integer.parseInt(msg.replace("m", ""));
		}
		return time;
	}
	
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
	
    public static String replacePokemon(String string, EntityPixelmon ep){
    	
    	String replaced = string + "";
    	
    	return replaced;
    }
    
	@SuppressWarnings("unchecked")
	public static WorldServer getWorldServer(Player player){
		
		WorldServer theWorld = null;
		
		for(WorldServer ws : MinecraftServer.getServer().worldServers){
			
			List<EntityPlayerMP> entities = (List<EntityPlayerMP>) ws.playerEntities;
			
			for(EntityPlayerMP p : entities){
				if(player.getUniqueId() == p.getUniqueID()){
					theWorld = ws;
				}
			}
			
		}
		
		return theWorld;
	}
    
	public static boolean hasPokemon(Player player, int slot){
		
		EntityPixelmon pkm = Utils.getPlayersPixelmon(player).get(slot-1);
		
		if(pkm == null){
			return false;
		}
		
		return true;
	}
	
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
	
	@SuppressWarnings("deprecation")
	public static void sendMessage(String msg){
		for(Player player : Bukkit.getOnlinePlayers()){
			if(PixelmonAuctions.avoid.contains(player)) continue;
			player.sendMessage(msg);
		}
	}
	
	public static void givePlayerPokemon(Player sender, Player target, int slot){
		
		PlayerStorage ss = null;
		try {
			ss = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(sender.getUniqueId());
		} catch (PlayerNotLoadedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		PlayerStorage ts = null;
		try {
			ts = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(target.getUniqueId());
		} catch (PlayerNotLoadedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int k = slot-1;
		
		NBTTagCompound nbt = ss.getList()[k];
		ss.recallAllPokemon();
		ss.changePokemonAndAssignID(k, null);
		
		EntityPixelmon pkm = (EntityPixelmon)PixelmonEntityList.createEntityFromNBT(nbt, Utils.getWorldServer(sender));
		
		ts.addToParty(pkm);
		
		FileManager fm = FileManager.getInstance();
		
		target.sendMessage(fm.getMessage("ReceivedPokemon").replace("%player%", sender.getName()).replace("%pkm%", pkm.getName()));
		sender.sendMessage(fm.getMessage("SentPokemon").replace("%player%", target.getName()).replace("%pkm%", pkm.getName()));
		
	}
	
	public static List<EntityPixelmon> getPlayersPixelmon(Player player){
		WorldServer server = Utils.getWorldServer(player);

		List<EntityPixelmon> pkm = new ArrayList<EntityPixelmon>();
		
		try {
			PlayerStorage storage = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(player.getUniqueId());

			for (int i = 0; i < 6; i++) {
				if(storage.partyPokemon[i] != null){
					EntityPixelmon pixelmon = storage.sendOutFromPosition(i, server);
					pkm.add(pixelmon);
					storage.recallAllPokemon();
				}else{
					pkm.add(null);

				}
			}
		} catch (PlayerNotLoadedException e) {
			e.printStackTrace();
		}
		return pkm;
	}
	
	public static List<String> getMoves(Player player, EntityPixelmon pkm){
		
		if(pkm == null) return null;
		
		List<String> moves = new ArrayList<String>();
		
		for(Attack a : pkm.getMoveset().attacks){
			if(a == null){
				moves.add("None");
			}else{
				moves.add(a.baseAttack.getUnLocalizedName());
			}
		}
		
		return moves;
	}
	
	
	public static String getItem(Player player, EntityPixelmon pkm){
		if(pkm.getItemHeld() != null){
			String item = pkm.getItemHeld().getLocalizedName();
			return item;
		}else{
			return "None";
		}
	}
	
	public static List<String> getEvs(Player player, EntityPixelmon pkm){
		
		int hp = pkm.stats.EVs.HP;
		int attack = pkm.stats.EVs.Attack;
		int defence = pkm.stats.EVs.Defence;
		int specialatk = pkm.stats.EVs.SpecialAttack;
		int specialdefence = pkm.stats.EVs.SpecialDefence;
		int speed = pkm.stats.EVs.Speed;
		
		List<String> evs = new ArrayList<String>();
		evs.add(hp + "");
		evs.add(attack + "");
		evs.add(defence + "");
		evs.add(specialatk + "");
		evs.add(specialdefence + "");
		evs.add(speed + "");
		
		return evs;
	}
	
	public static List<String> getIvs(Player player, EntityPixelmon pkm){
		
		int hp = pkm.stats.IVs.HP;
		int attack = pkm.stats.IVs.Attack;
		int defence = pkm.stats.IVs.Defence;
		int specialatk = pkm.stats.IVs.SpAtt;
		int specialdefence = pkm.stats.IVs.SpDef;
		int speed = pkm.stats.IVs.Speed;
		
		List<String> evs = new ArrayList<String>();
		evs.add(hp + "");
		evs.add(attack + "");
		evs.add(defence + "");
		evs.add(specialatk + "");
		evs.add(specialdefence + "");
		evs.add(speed + "");
		return evs;
	}
	
}
