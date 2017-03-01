package me.creeperkila21.pixelmonauctions.config;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.creeperkila21.pixelmonauctions.PixelmonAuctions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {
	
	static FileManager instance = new FileManager();
        //Make a new file
	FileConfiguration config;
	File cfile;
	FileConfiguration language;
	File lfile;
	
	public static FileManager getInstance() {
		return instance;
	}
	
        //Setup the files
	public void setup(Plugin p) {
                //The name of the file
		cfile = new File(p.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(cfile);
		
		lfile = new File(p.getDataFolder(), "language.yml");
		language = YamlConfiguration.loadConfiguration(lfile);
		
		if(!(lfile.exists())){
			language.set("messages.prefix", "&6[&cPixelmon&ffAuctions&6]");
			language.set("messages.ReceivedPokemon", "%prefix% &6You have received a &e%pokemon% &6from &e%player%");
			language.set("messages.SentPokemon", "%prefix% &6You have sent your &e%pkm% &6to &e%player%");
			language.set("messages.TimeAdded", "%prefix% &e%timeadded% &6has been added to the auction! There is now &e%currenttime% &6left!");
			language.set("messages.AuctionEnded", "%prefix% &6The auction has been ended!");
			language.set("messages.AuctionEndedNoBidder", "%prefix% &6No one has bid! The auction will now end!");
			language.set("messages.AuctionWinner", "%prefix% &e%winner% &6has just won the auction and has received a %pokemon%!");
			language.set("messages.NotEnoughMoney", "%prefix% &cYou don't have enough money to do this!");
			language.set("messages.PlayerCouldntAffordAuction", "%prefix% &c%player% didn't have enough money to accept the auction!!");
			
			List<String> format = new ArrayList<String>();
			format.add("%prefix% &7/pixelauction auction <Slot> <Price> <Increment>");
			format.add("%prefix% &7/pixelauction bid [Amount]");
			format.add("%prefix% &7/pixelauction info");
			language.set("messages.AuctionCommand.format", format);
			
			try {
				language.save(lfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(!(cfile.exists())){
			config.set("time", "1m");
			List<Integer> broadcastOn = new ArrayList<Integer>();
			broadcastOn.add(45);
			broadcastOn.add(30);
			broadcastOn.add(10);
			broadcastOn.add(5);
			broadcastOn.add(4);
			broadcastOn.add(3);
			broadcastOn.add(2);
			broadcastOn.add(1);
			config.set("broadcastOn", broadcastOn);
			config.set("antiSnipeOn", false);
			config.set("antiSnipeTimeInSeconds", 5);
			config.set("antiSnipeAddedTimeInSeconds", 15);
			config.options().header("Author: creeperkila21\nAll configuration data will be stored here!");
			try{
				config.save(cfile);
			} catch(IOException e){
				Bukkit.getConsoleSender().sendMessage("§4ERROR: §7Couldn't create config.yml!");
			}
		}
		
	}
	
	public String formatString(String i){
		String prefix = ChatColor.translateAlternateColorCodes('&', language.getString("messages.prefix"));
		String m = ChatColor.translateAlternateColorCodes('&', i);
		return m.replace("%prefix%", prefix);
	}
	
	public String getPrefix(){
		if(PixelmonAuctions.prefix == null){
			PixelmonAuctions.prefix = ChatColor.translateAlternateColorCodes('&', language.getString("messages.prefix"));
		}
		return PixelmonAuctions.prefix;
	}
	
	public String getMessage(String msg){
		return ChatColor.translateAlternateColorCodes('&', msg.replace("%prefix%", getPrefix()));
	}
	
        //Use this to do like fm.getData().getStringList();
	public FileConfiguration getConfig() {
		return config;
	}
	
	public FileConfiguration getLanguage() {
		return language;
	}
        //Save the file
	public void saveConfig() throws IOException {
		config.save(cfile);
	}
	public void saveLanguage() throws IOException{
		language.save(lfile);
	}
	
        //Reload the file
	public void reloadConfig() {
		config = YamlConfiguration.loadConfiguration(cfile);
	}

	public void reloadLanguage() {
		language = YamlConfiguration.loadConfiguration(lfile);
	}
	
}