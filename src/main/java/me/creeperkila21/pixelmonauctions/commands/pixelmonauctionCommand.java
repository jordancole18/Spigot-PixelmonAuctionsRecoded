package me.creeperkila21.pixelmonauctions.commands;

import java.util.List;

import me.creeperkila21.pixelmonauctions.PixelmonAuctions;
import me.creeperkila21.pixelmonauctions.Utils.Utils;
import me.creeperkila21.pixelmonauctions.auction.Auction;
import me.creeperkila21.pixelmonauctions.config.FileManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class pixelmonauctionCommand implements CommandExecutor{

	FileManager fm = FileManager.getInstance();
	
	public List<String> format = fm.getConfig().getStringList("format");
	
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
				
				if(Auction.currentAuction != null){
					sender.sendMessage(fm.getMessage("AuctionGoing"));
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
				
//				List<String> moves = Utils.getMoves(player, slot);
//				String name = Utils.getName(player, slot);
//				int lvl = Utils.getLevel(player, slot);
//				List<String> ivs = Utils.getIvs(player, slot);
//				
//				String item = Utils.getItem(player, slot);
//				String nature = Utils.getNature(player, slot);
//				String ability = Utils.getAbility(player, slot);
//				String size = Utils.getSize(player, slot);
//				boolean shiny = Utils.isShiny(player, slot);
				
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
				a.init();
				if(PixelmonAuctions.avoid.contains(player)){
					PixelmonAuctions.avoid.remove(player);
				}
				Utils.sendMessage(fm.getMessage("AuctionStarted"));
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
				
				if(args.length == 1){
					Auction a = Auction.currentAuction;
					a.addBid(player, a.getCurrentPrice() + a.getIncrement());
				}else if(args.length >= 2){
					Auction a = Auction.currentAuction;
					
					if(Utils.isDouble(args[1]) == false){
						for(String i : format){
							player.sendMessage(fm.formatString(i));
						}
						return true;
					}
					
					double d = Double.parseDouble(args[1]);
					
					a.addBid(player, d);
				}
			}
		}
		
		return true;
	}

}
