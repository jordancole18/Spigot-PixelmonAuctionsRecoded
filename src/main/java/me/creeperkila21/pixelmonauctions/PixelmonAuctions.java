package me.creeperkila21.pixelmonauctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import me.creeperkila21.pixelmonauctions.Utils.Utils;
import me.creeperkila21.pixelmonauctions.auction.Auction;
import me.creeperkila21.pixelmonauctions.commands.pixelmonauctionCommand;
import me.creeperkila21.pixelmonauctions.config.FileManager;
import me.creeperkila21.pixelmonauctions.listeners.PlayerEvents;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PixelmonAuctions extends JavaPlugin{

	public static String prefix = null;
	
	FileManager fm = FileManager.getInstance();
	
	public static Auction currentAuction;
	
	public static HashMap<Integer, Auction> allAuctions;
	
	private static Plugin instance;
	
    private static final Logger log = Logger.getLogger("Minecraft");
    public static Economy econ = null;
	
	public static int time = 0;
	public static boolean antiSnipeEnabled;
	public static int antiSnipeTime;
	public static int antiSnipeTimeAdded;
	
	public static List<Player> avoid;
	
	public static List<Integer> broadcastOn = new ArrayList<Integer>();
	
	public void onEnable(){
		
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		fm.setup(this);
		avoid = new ArrayList<Player>();
		allAuctions = new HashMap<Integer, Auction>();
		for(String i : fm.getConfig().getStringList("broadcastOn")){
			if(!(Utils.isInt(i))){
				Bukkit.getLogger().severe("[PixelmonAuctions] Invalid number \'" + i + "\'");
			}else{
				broadcastOn.add(Integer.parseInt(i));
			}
		}
		antiSnipeEnabled = fm.getConfig().getBoolean("antiSnipeOn");
		antiSnipeTime = fm.getConfig().getInt("antiSnipeTimeInSeconds");
		antiSnipeTimeAdded = fm.getConfig().getInt("antiSnipeAddedTimeInSeconds");
		instance = this;
		time = Utils.getTimeInSeconds(fm.getConfig().getString("time"));
		getCommand("pixelauction").setExecutor(new pixelmonauctionCommand());
		Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }	
	
	public void onDisable(){
		
	}
	
	public static Plugin getInstance(){
		return instance;
	}
	
}
