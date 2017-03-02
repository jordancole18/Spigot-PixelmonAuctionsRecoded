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
			language.set("messages.prefix", "&6[&cPixelmon&fAuctions&6]");
			language.set("messages.NoPermission", "%prefix% &cYou do not have permission to do this!");
			language.set("messages.ReceivedPokemon", "%prefix% &6You have received a &e%pokemon% &6from &e%player%");
			language.set("messages.SentPokemon", "%prefix% &6You have sent your &e%pokemon% &6to &e%player%");
			language.set("messages.TimeAdded", "%prefix% &e%timeadded% &6has been added to the auction! There is now &e%currenttime% &6left!");
			language.set("messages.AuctionEnded", "%prefix% &6The auction has been ended!");
			language.set("messages.AuctionEndedNoBidder", "%prefix% &6No one has bid! The auction will now end!");
			language.set("messages.AuctionWinner", "%prefix% &e%winner% &6has just won the auction and has received a %pokemon%!");
			language.set("messages.NotEnoughMoney", "%prefix% &cYou don't have enough money to do this!");
			language.set("messages.PlayerCouldntAffordAuction", "%prefix% &c%player% didn't have enough money to accept the auction!!");
			language.set("messages.NoPokemonInSlot", "%prefix% &6You dont have a pokemon in that slot!");
			List<String> format = new ArrayList<String>();
			format.add("%prefix% &7/pixelauction auction <Slot> <Price> <Increment>");
			format.add("%prefix% &7/pixelauction bid [Amount]");
			format.add("%prefix% &7/pixelauction info");
			format.add("%prefix% &7/pixelmonauction mute");
			format.add("%prefix% &7/pixelmonauction unmute");
			language.set("messages.format", format);
			language.set("messages.AuctionStarted", "%prefix% &6%player% is auctioning a %shiny%&aLevel: %lvl% %pokemon% &6pokemon with the moves of &a%moves%&6! Bid Starting at &a$%starting% with an increment of &a$%increment%");
			language.set("messages.LastPokemon", "%prefix% &cYou can not trade your last pokemon!");
			language.set("messages.NoAuction", "%prefix% &6There is currently no auction going on!");
			language.set("messages.OwnBid", "%prefix% &cYou can not bid on your own auction!");
			language.set("messages.InvalidSlot", "%prefix% &cThat is an invalid slot! (1-6)");
			language.set("messages.AuctionEnding", "%prefix% &6The auction for a level %lvl% %pokemon% is ending in %time%");
			language.set("messages.PlayerBids", "%prefix% &e%player% &6has just raised the bid to &e$%currentbid%!");
			language.set("messages.AuctionEnded", "%prefix% &e%player% &6has just bought a &e%pokemon% &6for &e$%price%");
			List<String> info = new ArrayList<String>();
			info.add("&eStarting Bid: &6$%starting%");
			info.add("&ePrice Increment: &6$%increment%");
			info.add("&eCurrent Price: &6$%price%");
			info.add("&ePokemon Name: &6%name%");
			info.add("&eLevel: &6%level%");
			info.add("&eShiny: &6%shiny%");
			info.add("&eItem: &6%item%");
			info.add("&eNature: &6%nature%");
			info.add("&eAbility: &6%ability%");
			info.add("&eSize: &6%size%");
			info.add("&eMoves: &6%moves%");
			info.add("&eEVs: &6%evs%");
			info.add("&eIVs: &6%ivs%");
			language.set("messages.InfoCommand", info);
			language.set("messages.LatestBidder", "%prefix% &6You are already the latest bidder for this auction!");
			language.set("messages.HigherBid", "&6You need to raise your bid to atleast &e%bid%!");
			language.set("messages.AlreadyAuctioning", "&6You already have a pokemon up for auction!");
			language.set("messages.AuctionPutInQueue", "&6You auction is now in the queue! &e#%num% in queue");
			language.set("messages.AutionsMuted", "&6All auctions are now muted!");
			language.set("messages.AuctionsUnmuted", "&6All auctions have been unmuted!");
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
		String m = ChatColor.translateAlternateColorCodes('&', i);
		return m.replace("%prefix%", getPrefix());
	}
	
	public String getPrefix(){
		if(PixelmonAuctions.prefix == null){
			PixelmonAuctions.prefix = ChatColor.translateAlternateColorCodes('&', language.getString("messages.prefix"));
		}
		return PixelmonAuctions.prefix;
	}
	
	public String getMessage(String msg){
		return ChatColor.translateAlternateColorCodes('&', language.getString("messages." + msg).replace("%prefix%", getPrefix()));
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