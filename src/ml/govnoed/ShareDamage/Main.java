package ml.govnoed.ShareDamage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ml.govnoed.ShareDamage.sql.MySQL;
import ml.govnoed.ShareDamage.sql.SQLGetter;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	public boolean shareDamageActive = false;
	Map<String, Integer> damageTaken = new HashMap<String, Integer>();
	
	public MySQL SQL;
	public SQLGetter data;
	public boolean db = false;
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("sharedamage").setTabCompleter(new Cmdtab());
		shareDamageActive = false;
		
		this.SQL = new MySQL();
		this.data = new SQLGetter(this);
		
		try {
			SQL.connect();
			Bukkit.getServer().broadcast("Database is connected!", Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
		} catch (ClassNotFoundException | SQLException e) {
			Bukkit.getLogger().info("Login info is incorrect or you're not using a database!");
		}
		
		if (SQL.isConnected()) {
			Bukkit.getLogger().info("Database is connected!");
			db = true;
			data.createTable();
		}
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
			
			if (db == true) {
				data.addDamage(player.getUniqueId(), event.getDamage());
			}
			
			
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
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if (db == true) {
			Player player = event.getPlayer();
			data.createPlayer(player);
		}
		
	}
	
	

}
