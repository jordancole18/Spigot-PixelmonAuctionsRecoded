package me.creeperkila21.pixelmonauctions.auction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.creeperkila21.pixelmonauctions.PixelmonAuctions;
import me.creeperkila21.pixelmonauctions.Utils.Utils;
import me.creeperkila21.pixelmonauctions.config.FileManager;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;

public class Auction {

	private String player;
	private EntityPixelmon ep;
	private double startingPrice;
	private boolean hasEnded = false;
	private Player latestBidder = null;
	private double currentPrice = 0.0;
	private int timeLeft;
	private int slot;
	private double increment;

	public static Auction currentAuction = null;
	
	public Auction(Player player, EntityPixelmon ep, double price, int timeLeft, int slot, double increment){
		this.player = player.getName();
		this.ep = ep;
		this.startingPrice = price;
		this.timeLeft = timeLeft;
		this.slot = slot;
		this.increment = increment;
	}
	
	public void init(){
		if(PixelmonAuctions.allAuctions.isEmpty() == false || PixelmonAuctions.currentAuction != null){
			int lowerNum = 0;
			
			List<Integer> allInts = new ArrayList<Integer>();
			allInts.addAll(PixelmonAuctions.allAuctions.keySet());
			
			lowerNum = Collections.min(allInts) + 1;
			PixelmonAuctions.allAuctions.put(lowerNum, this);
		}else{
			start();
			currentAuction = this;
		}
	}

	public void addBid(Player player, double newPrice){
		setLatestBidder(player);
		setCurrentPrice(newPrice);
		if(PixelmonAuctions.antiSnipeEnabled && timeLeft <= PixelmonAuctions.antiSnipeTime){
			timeLeft+= PixelmonAuctions.antiSnipeTimeAdded;
			FileManager fm = FileManager.getInstance();
			Utils.sendMessage(fm.getMessage("TimeAdded").replace("%timeadded%", PixelmonAuctions.antiSnipeTimeAdded + "").replace("%currenttime%", timeLeft + ""));
		}
	}
	
	public int getSlot(){
		return slot;
	}
	
	public boolean isHasEnded() {
		return hasEnded;
	}

	public void setHasEnded(boolean hasEnded) {
		this.hasEnded = hasEnded;
	}

	public Player getLatestBidder() {
		return latestBidder;
	}

	public void setLatestBidder(Player latestBidder) {
		this.latestBidder = latestBidder;
	}

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}

	public void setPlayer(String player) {
		this.player = player;
	}
	
	public String getPlayer() {
		return player;
	}

	public EntityPixelmon getPixelmon() {
		return ep;
	}

	public double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(double price) {
		this.startingPrice = price;
	}
	
	@SuppressWarnings("deprecation")
	public void end() throws PlayerNotLoadedException{
		hasEnded = true;
		if(latestBidder == null){
			Utils.sendMessage(FileManager.getInstance().getMessage("AuctionEndedNoBidder"));
			Player p = Bukkit.getPlayer(player);
			if(p == null){
				@SuppressWarnings("null")
				PlayerStorage bidderStorage = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(p.getUniqueId());
				bidderStorage.addToParty(ep);
			}else{
				FileManager fm = FileManager.getInstance();
				fm.getConfig().set("toGive." + player + "." + ep.getUniqueID().toString(), ep);
			}
		}else{
			EconomyResponse er = PixelmonAuctions.econ.withdrawPlayer(latestBidder.getName(), getCurrentPrice());
			
			if(er.transactionSuccess()){
				EconomyResponse deposit = PixelmonAuctions.econ.depositPlayer(getPlayer(), currentPrice);
				if(deposit.transactionSuccess()){
					PlayerStorage bidderStorage = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(latestBidder.getUniqueId());
					bidderStorage.addToParty(ep);
					FileManager fm = FileManager.getInstance();
					latestBidder.sendMessage(Utils.replacePokemon(fm.getMessage("ReceivedPokemon").replace("%player%", player), ep));
					if(Bukkit.getPlayer(player) != null){
						Bukkit.getPlayer(player).sendMessage(Utils.replacePokemon(fm.getMessage("SentPokemon").replace("%player%", latestBidder.getName()), ep));
					}
				}
			}else{
				FileManager fm = FileManager.getInstance();
				Utils.sendMessage(fm.getMessage("AuctionEnded"));
				latestBidder.sendMessage(fm.getMessage("NotEnoughMoney"));
				if(Bukkit.getPlayer(player) != null){
					Bukkit.getPlayer(player).sendMessage(fm.getMessage("PlayerCouldntAffordAuction").replace("%player%", latestBidder.getName()));
				}
			}
			
		}
		
		currentAuction = null;
		
		if(PixelmonAuctions.allAuctions.isEmpty() == false){
			if(PixelmonAuctions.allAuctions.containsKey(this)){
				PixelmonAuctions.allAuctions.remove(this);
			}
			if(PixelmonAuctions.allAuctions.isEmpty() == false){
				Auction a = PixelmonAuctions.allAuctions.get(0);
				currentAuction = a;
				a.start();
			}
		}
		return;
	}
	
	public void start(){
		new BukkitRunnable(){
			public void run(){
				if(hasEnded == true){
					this.cancel();
				}
				
				timeLeft--;
				
				if(timeLeft == 0){
					try {
						end();
					} catch (PlayerNotLoadedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}.runTaskTimer(PixelmonAuctions.getInstance(), 20, 20);
	}
	
	public void sendInfoMessage(Player player){
		FileManager fm = FileManager.getInstance();
		List<String> list = fm.getLanguage().getStringList("messages.InfoCommand");
		List<String> msg = new ArrayList<String>();
		for(String i : list){
			msg.add(ChatColor.translateAlternateColorCodes('&', i));
		}
		
		List<String> moves = Utils.getMoves(player, ep);
		List<String> ivs = Utils.getIvs(player, ep);
		List<String> evs = Utils.getEvs(player, ep);
		
		String m = "";
		int h = 0;
		for(String g : moves){
			h++;
			if(h == moves.size()){
				m = m + "and " + g;
			}else{
				m = m + g + ", ";
			}
		}
		//HP: Attack: Defence: Sp. Attack: Sp. Defence: Speed:
		String ev = "";
		int evcount = 0;
		
		for(String i : evs){
			evcount++;
			if(ev == ""){
				ev = "HP: " + i;
			}else{
				if(evcount == 2){
					ev = ev + ", Attack: " + i;
				}else if(evcount == 3){
					ev = ev + ", Defence: " + i;
				}else if(evcount == 4){
					ev = ev + ", Sp.Attack: " + i;
				}else if(evcount == 5){
					ev = ev + ", Sp. Defence: " + i;
				}else if(evcount == 6){
					ev = ev + ", Speed: " + i;
				}
			}
		}
		
		String iv = "";
		int ivcount = 0;
		
		for(String i : ivs){
			ivcount++;
			if(iv == ""){
				iv = "HP: " + i;
			}else{
				if(ivcount == 2){
					iv = iv + ", Attack: " + i;
				}else if(ivcount == 3){
					iv = iv + ", Defence: " + i;
				}else if(ivcount == 4){
					iv = iv + ", Sp.Attack: " + i;
				}else if(ivcount == 5){
					iv = iv + ", Sp. Defence: " + i;
				}else if(ivcount == 6){
					iv = iv + ", Speed: " + i;
				}
			}
	
		
		List<String> k = new ArrayList<String>();
		
		for(String i1 : msg){
			String l = i1.replace("%name%", ep.getLocalizedName())
					.replace("%level%", ep.getLvl().getLevel() + "")
					.replace("%shiny%", ep.getIsShiny() + "")
					.replace("%item%", Utils.getItem(player, ep))
					.replace("%nature%", ep.getNature().name())
					.replace("%ability%", ep.getAbility().getLocalizedName())
					.replace("%size%", ep.getGrowth().name())
					.replace("%moves%", m)
					.replace("%evs%", ev)
					.replace("%ivs%", iv)
					.replace("%starting%", startingPrice + "")
					.replace("%increment%", increment + "")
					.replace("%price%", currentPrice + "");
			k.add(l);
		}
		
		for(String i1 : k){
			player.sendMessage(i1);
		}
		
		}
		
	}

	public double getIncrement() {
		return increment;
	}

	public void setIncrement(double increment) {
		this.increment = increment;
	}
	
}
