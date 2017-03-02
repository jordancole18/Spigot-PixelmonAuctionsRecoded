package me.creeperkila21.pixelmonauctions.auction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.creeperkila21.pixelmonauctions.Utils.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.bukkit.entity.Player;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.database.DatabaseMoves;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;

public class Pokemon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int lvl;
	private List<String> moves = new ArrayList<String>();
	private List<String> evs = new ArrayList<String>();
	private List<String> ivs = new ArrayList<String>();
	private String ability;
	private int item;
	private boolean isShiny;
	private String nature;
	private EnumPokeballs ball;
	private String size;
	private String nickname;
	private int xp;
	private float maxHealth;
	
	public Pokemon(String name, int lvl, List<String> moves, List<String> ivs, List<String> evs, String ability, int item, boolean isShiny, String nature, EnumPokeballs ball, String size, String nickname, int xp, float maxHealth){
		this.name = name;
		this.lvl = lvl;
		this.moves = moves;
		this.ability = ability;
		this.item = item;
		this.isShiny = isShiny;
		this.nature = nature;
		this.evs = evs;
		this.ivs = ivs;
		this.ball = ball;
		this.size = size;
		this.nickname = nickname;
		this.xp = xp;
		this.maxHealth = maxHealth;
	}

	public Pokemon(EntityPixelmon pokemon){
		this.name = pokemon.getName();
		this.lvl = pokemon.getLvl().getLevel();
		List<String> moves = new ArrayList<String>();
		for(Attack i : pokemon.getMoveset().attacks){
			if(i == null){
				moves.add("None");
				continue;
			}
			moves.add(i.baseAttack.getUnLocalizedName());
		}
		
		this.moves = moves;
		
		// TODO HP, ATK, DEFENCE, SPATK, SPDEF, SPEED
		
		
		
		List<String> ivs = new ArrayList<String>();
		
		ivs.add(pokemon.stats.IVs.HP + "");
		ivs.add(pokemon.stats.IVs.Attack + "");
		ivs.add(pokemon.stats.IVs.Defence + "");
		ivs.add(pokemon.stats.IVs.SpAtt + "");
		ivs.add(pokemon.stats.IVs.SpDef + "");
		ivs.add(pokemon.stats.IVs.Speed + "");
		this.ivs = ivs;
		
		List<String> evs = new ArrayList<String>();
		
		
		evs.add(pokemon.stats.EVs.HP + "");
		evs.add(pokemon.stats.EVs.Attack + "");
		evs.add(pokemon.stats.EVs.Defence + "");
		evs.add(pokemon.stats.EVs.SpecialAttack + "");
		evs.add(pokemon.stats.EVs.SpecialDefence + "");
		evs.add(pokemon.stats.EVs.Speed + "");
		
		this.evs = evs;
		this.ability = pokemon.getAbility().getName();
		int id = -1;
		if(pokemon.getHeldItem() != null){
			id = Item.getIdFromItem(pokemon.getHeldItem().getItem());
		}
		this.item = id;
		this.isShiny = pokemon.getIsShiny();
		this.nature = pokemon.getNature().name();
		this.ball = pokemon.caughtBall;
		this.size = pokemon.getGrowth().name();
		this.nickname = pokemon.getNickname();
		this.xp = pokemon.getLvl().getExp();
		this.maxHealth = pokemon.getMaxHealth();
	}
	
	public String getFormattedMoves(){
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
		return m;
	}
	
	public float getMaxHealth(){
		return maxHealth;
	}
	
	public String getFormattedEVs(){
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
		return ev;
	}
	
	public String getFormattedIVs(){
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
		}
		return iv;
	}
	
	public String getName(){
		return name;
	}
	
	public int getLevel(){
		return lvl;
	}
	
	public EnumPokeballs getPokeball(){
		return ball;
	}
	
	public String getPokeballName(){
		return ball.name();
	}
	
	public List<String> getMoves(){
		return moves;
	}
	
	public String getAbility(){
		return ability;
	}
	
	public int getItem(){
		return item;
	}
	
	public boolean isShiny(){
		return isShiny;
	}
	
	public String getNature(){
		return nature;
	}
	
	public List<String> getEVs(){
		return evs;
	}
	
	public List<String> getIVs(){
		return ivs;
	}
	
	public String toString(){
		String m = "";
		for(String i : this.getMoves()){
			if(m == ""){
				m = i + ":";
			}else{
				m = m + "" + i + ":";
			}
		}
		String ivs = "";
		for(String i : this.ivs){
			if(ivs == ""){
				ivs = i + ":";
			}else{
				ivs = ivs + "" + i + ":";
			}
		}
		String evs = "";
		for(String i : this.evs){
			if(evs == ""){
				evs = i + ":";
			}else{
				evs = evs + "" + i + ":";
			}
		}
		return name + "," + lvl + "," + m + "," + evs + "," + ivs + "," + ability + "," + item + "," + isShiny + "," + nature + "," + ball.name() + "," + size + "," + nickname + "," + xp + "," + maxHealth;
	}
	
	public String getNickname(){
		return nickname;
	}
	
	public String getItemName(Player player){
		EntityPixelmon pkm = getEntityPixelmon();
		if(pkm.getItemHeld() != null){
			String item = pkm.getItemHeld().getLocalizedName();
			return item;
		}else{
			return "None";
		}
	}
	
	public static Pokemon fromString(String n){
		String[] pkmData = n.split(",");
		
		String name = "";
		int lvl;
		String[] m;
		String[] ivs;
		String[] evs;
		String ability = "";
		int item;
		boolean isShiny;
		String nature;	
		EnumPokeballs ball;
		String size;
		String nickname;
		int xp;
		float maxHealth;
		
		name = pkmData[0];
		lvl = Integer.parseInt(pkmData[1]);
		m = pkmData[2].replace("[", "").replace("]", "").split(":");
		evs = pkmData[3].split(":");
		ivs = pkmData[4].split(":");
		ability = pkmData[5];
		item = Integer.parseInt(pkmData[6]);
		isShiny = Boolean.parseBoolean(pkmData[7]);
		nature = pkmData[8];
		ball = EnumPokeballs.valueOf(pkmData[9]);
		size = pkmData[10];
		nickname = pkmData[11];
		xp = Integer.parseInt(pkmData[12]);
		maxHealth = Float.parseFloat(pkmData[13]);
		
		
		List<String> moves = Arrays.asList(m);
		List<String> i = Arrays.asList(ivs);
		List<String> e = Arrays.asList(evs);
		
		Pokemon p = new Pokemon(name, lvl, moves, i, e, ability, item, isShiny, nature, ball, size, nickname, xp, maxHealth);
		
		return p;
	}
	
	@SuppressWarnings("static-access")
	public EntityPixelmon getEntityPixelmon(){
		EntityPixelmon p = (EntityPixelmon)PixelmonEntityList.createEntityByName(this.name, Utils.getWorldServer());
		
		p.setNature(EnumNature.valueOf(nature));
		p.setIsShiny(isShiny);
		p.getLvl().setLevel(lvl);
		p.setAbility(ability);
		p.setGrowth(EnumGrowth.valueOf(size));
		p.getLvl().setExp(xp);
		p.setNickname(nickname);
		p.setHealth(maxHealth);
		if(item > -1){
			ItemStack i = new ItemStack(Item.getItemById(item));
			p.setHeldItem(i);
		}
		p.caughtBall = ball;
		
		p.stats.IVs.HP = Integer.parseInt(ivs.get(0));
		p.stats.IVs.Attack = Integer.parseInt(ivs.get(1));
		p.stats.IVs.Defence = Integer.parseInt(ivs.get(2));
		p.stats.IVs.SpAtt = Integer.parseInt(ivs.get(3));
		p.stats.IVs.SpDef = Integer.parseInt(ivs.get(4));
		p.stats.IVs.Speed = Integer.parseInt(ivs.get(5));
		p.stats.EVs.HP = Integer.parseInt(evs.get(0));
		p.stats.EVs.Attack = Integer.parseInt(evs.get(1));
		p.stats.EVs.Defence = Integer.parseInt(evs.get(2));
		p.stats.EVs.SpecialAttack = Integer.parseInt(evs.get(3));
		p.stats.EVs.SpecialDefence = Integer.parseInt(evs.get(4));
		p.stats.EVs.Speed = Integer.parseInt(evs.get(5));
		
		for(String a : moves){
			
			DatabaseMoves dm = new DatabaseMoves();
			Attack atk = dm.getAttack(a);
			
			p.getMoveset().add(atk);
		}
		return p;
	}
}
