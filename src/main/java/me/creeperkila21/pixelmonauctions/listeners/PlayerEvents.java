package me.creeperkila21.pixelmonauctions.listeners;

import me.creeperkila21.pixelmonauctions.auction.Pokemon;
import me.creeperkila21.pixelmonauctions.config.FileManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

public class PlayerEvents implements Listener{

	FileManager fm = FileManager.getInstance();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		if(fm.getConfig().contains("toGive." + e.getPlayer().getName())){
			
			for(String i : fm.getConfig().getConfigurationSection("toGive." + e.getPlayer().getName()).getKeys(false)){
			
				EntityPixelmon ep = Pokemon.fromString(fm.getConfig().getString("toGive." + e.getPlayer().getName() + "." + i)).getEntityPixelmon();
				
				PlayerStorage ps = null;
				
				try {
					ps = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(e.getPlayer().getUniqueId());
				} catch (PlayerNotLoadedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				ps.addToParty(ep);
			}
			
		}
	}
	
}
