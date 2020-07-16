package ml.govnoed.ShareDamage;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public boolean shareDamageActive = false;
	Map<String, Integer> damageTaken = new HashMap<String, Integer>();
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("sharedamage").setTabCompleter(new Cmdtab());
		shareDamageActive = false;
	}

	@Override
	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (label.equalsIgnoreCase("sharedamage")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Sorry, only player can use this command!");
				return true;
			}
				
			if (args[0].equalsIgnoreCase("start")) {
				shareDamageActive = true;
				if (shareDamageActive == true) {
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "Damage sharing between all players has been enabled!");
				}
				return true;
			}
			
			if (args[0].equalsIgnoreCase("stop")) {
				shareDamageActive = false;
				if (shareDamageActive == false) {
					Bukkit.getServer().broadcastMessage(ChatColor.RED + "Damage sharing between all players has been disabled!");
				}
				
				Set<Map.Entry<String, Integer>> results = damageTaken.entrySet();
				
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Statistics for this run were:");
				for (Map.Entry<String, Integer> player : results) {
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + player.getKey() + ChatColor.WHITE + " took " + ChatColor.RED + player.getValue() + ChatColor.WHITE + " damage");
				}
				
				damageTaken.clear();
				return true;
			}
			if (args[0].equalsIgnoreCase("stats")) {
				
				Set<Map.Entry<String, Integer>> results = damageTaken.entrySet();
				
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Statistics for this run:");
				for (Map.Entry<String, Integer> player : results) {
					Bukkit.getServer().broadcastMessage(ChatColor.GREEN + player.getKey() + ChatColor.WHITE + " took " + ChatColor.RED + player.getValue() + ChatColor.WHITE + " damage");
				}
			}
		}
		
		return false;

	}
	
	@EventHandler
	public void onDamageTaken(EntityDamageEvent event) {
		
		if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
			return;
		}
		
		if (shareDamageActive == false) {
			return;
		}
		
		if(event.getEntity() instanceof Player) {
			
			Player player = (Player) event.getEntity();
			
			
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(player.getName() != p.getName()) p.damage(event.getDamage());
			}
			
			if (damageTaken.containsKey(player.getName())) {
				damageTaken.put(player.getName(), damageTaken.get(player.getName()) + ((int) event.getDamage()));
			} else {
				damageTaken.put(player.getName(), (int) event.getDamage());
			}
		}
		
	}
	
	

}
