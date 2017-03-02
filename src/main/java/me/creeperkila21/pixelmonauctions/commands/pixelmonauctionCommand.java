package me.creeperkila21.pixelmonauctions.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.creeperkila21.pixelmonauctions.PixelmonAuctions;
import me.creeperkila21.pixelmonauctions.Utils.Utils;
import me.creeperkila21.pixelmonauctions.auction.Auction;
import me.creeperkila21.pixelmonauctions.config.FileManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

public class pixelmonauctionCommand implements CommandExecutor{

	FileManager fm = FileManager.getInstance();
	
	public List<String> format = fm.getLanguage().getStringList("messages.format");
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("pixelauction")){
			
			if(!(sender instanceof Player)) return true;
			
			Player player = (Player) sender;
			
			// Check if the user has no arguments in his command
			if(args.length == 0){
				for(String i : format){
					player.sendMessage(fm.formatString(i));
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("information")){
				
				if(!(sender.hasPermission("pa.info"))){
					sender.sendMessage(fm.getMessage("NoPermission"));
					return true;
				}
				
				if(Auction.currentAuction == null){
					sender.sendMessage(fm.getMessage("NoAuction"));
					return true;
				}
				
				Auction.currentAuction.sendInfoMessage(player);
				return true;
			}
			
			// The auction subcommand
			if(args[0].equalsIgnoreCase("auction")){
				
				if(!(sender.hasPermission("pa.auction"))){
					sender.sendMessage(fm.getMessage("NoPermission"));
					return true;
				}
				
				 // /pixelauction auction <Slot> <Price> <Icrement>
				
				if(args.length != 4){
					for(String i : format){
						player.sendMessage(fm.formatString(i));
					}
					return true;
				}
				
				if(Utils.isInt(args[1]) == false || Utils.isDouble(args[2]) == false || Utils.isDouble(args[3]) == false){
					if(args.length != 2){
						for(String i : format){
							player.sendMessage(fm.formatString(i));
						}
						return true;
					}
					return true;
				}
				
				int slot = Integer.parseInt(args[1]);
				double price = Double.parseDouble(args[2]);
				double increment = Double.parseDouble(args[3]);
				
				if(slot < 1 || slot > 6){
					player.sendMessage(fm.getMessage("InvalidSlot"));
					return true;
				}
				
				if(price <= 0 || increment <= 0){
					for(String i : format){
						player.sendMessage(fm.formatString(i));
					}
					return true;
				}
				
				if(Utils.hasPokemon(player, slot) == false){
					player.sendMessage(fm.getMessage("NoPokemonInSlot"));
					return true;
				}
				
				int amount = 0;
				
				if(Utils.hasPokemon(player, 1) == true){
					amount++;
				}
				if(Utils.hasPokemon(player, 2) == true){
					amount++;
				}
				if(Utils.hasPokemon(player, 3) == true){
					amount++;
				}
				if(Utils.hasPokemon(player, 4) == true){
					amount++;
				}
				if(Utils.hasPokemon(player, 5) == true){
					amount++;
				}
				if(Utils.hasPokemon(player, 6) == true){
					amount++;
				}
				
				if(amount <= 1){
					player.sendMessage(fm.getMessage("LastPokemon"));
					return true;
				}
				
				EntityPixelmon pkm = Utils.getPlayersPixelmon(player).get(slot-1);
				
				Auction a = new Auction(player, pkm, price, PixelmonAuctions.time, slot, increment);
				
				if(Auction.currentAuction != null){
					if(Auction.currentAuction.getPlayer() == player.getName()){
						player.sendMessage(fm.getMessage("AlreadyAuctioning"));
						return true;
					}
					
					List<Integer> allInts = new ArrayList<Integer>();
					allInts.addAll(PixelmonAuctions.allAuctions.keySet());
					
					if(allInts.isEmpty()){
						PixelmonAuctions.allAuctions.put(1, a);
					}else{
						
						int highestNum = 0;
						
						for(int i : allInts){
							if(i >= highestNum){
								highestNum = i;
							}
						}
						
						PixelmonAuctions.allAuctions.put(highestNum + 1, a);
						
					}
					
					PlayerStorage ps = null;
					
					try {
						ps = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(player.getUniqueId());
					} catch (PlayerNotLoadedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ps.changePokemonAndAssignID(slot-1, null);
					ps.sendUpdatedList();
					
					player.sendMessage(FileManager.getInstance().getMessage("AuctionPutInQueue").replace("%num%", PixelmonAuctions.allAuctions.size() + 1 + ""));
					return true;
				}
				
				PlayerStorage ps = null;
				
				try {
					ps = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(player.getUniqueId());
				} catch (PlayerNotLoadedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ps.changePokemonAndAssignID(slot-1, null);
				ps.sendUpdatedList();
				
				a.init();
				if(PixelmonAuctions.avoid.contains(player)){
					PixelmonAuctions.avoid.remove(player);
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("bid")){
				
				if(!(sender.hasPermission("pa.bid"))){
					sender.sendMessage(fm.getMessage("NoPermission"));
					return true;
				}
				
				if(Auction.currentAuction == null){
					sender.sendMessage(fm.getMessage("NoAuction"));
					return true;
				}
				
				if(Auction.currentAuction.getPlayer() == player.getName()){
					sender.sendMessage(fm.getMessage("OwnBid"));
					return true;
				}
				
				if(Auction.currentAuction.getLatestBidder() != null){
					if(Auction.currentAuction.getLatestBidder() == player){
						player.sendMessage(fm.getMessage("LatestBidder"));
						return true;
					}
				}
				
				Auction a = Auction.currentAuction;
				
				if(PixelmonAuctions.econ.getBalance(player.getName()) < a.getCurrentPrice() + a.getIncrement()){
					player.sendMessage(fm.getMessage("HigherBid").replace("%bid%", a.getCurrentPrice() + a.getIncrement() + ""));
					return true;
				}
				
				if(args.length == 1){
					a.addBid(player, a.getCurrentPrice() + a.getIncrement());
				}else if(args.length >= 2){
					
					if(Utils.isDouble(args[1]) == false){
						for(String i : format){
							player.sendMessage(fm.formatString(i));
						}
						return true;
					}
					
					double d = Double.parseDouble(args[1]);
					
					if(a.getCurrentPrice() + a.getIncrement() > d){
						player.sendMessage(fm.getMessage(""));
						return true;
					}
					
					a.addBid(player, d);
				}
			}
		}
		
		return true;
	}

}
