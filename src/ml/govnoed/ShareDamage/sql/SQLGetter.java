package ml.govnoed.ShareDamage.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import ml.govnoed.ShareDamage.Main;

public class SQLGetter {
	
	private Main plugin;
	public SQLGetter(Main plugin) {
		this.plugin = plugin;
		
	}
	
	public void createTable() {
		PreparedStatement ps;
		try {
			ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ShareDamage"
					+ "(NAME VARCHAR(100),UUID VARCHAR(100),DAMAGE DOUBLE,PRIMARY KEY (NAME) )");

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createPlayer(Player player) {
		try {
			
			UUID uuid = player.getUniqueId();
			
			if (!exists(uuid)) {
				PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT INTO `ShareDamage`(`NAME`, `UUID`, `DAMAGE`) VALUES (?,?,?)");
				ps2.setString(1, player.getName());
				ps2.setString(2, uuid.toString());
				ps2.setDouble(3, 0.0);
				ps2.executeUpdate();
				
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean exists(UUID uuid) {
		try {
			System.out.print("existst?");
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM `ShareDamage` WHERE UUID='" + uuid.toString() + "'");

			
			ResultSet results = ps.executeQuery();
			if (results.next()) {
				return true;
			}
			return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void addDamage(UUID uuid, double damage) {
		try {
			System.out.print("adding damage");
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE `ShareDamage` SET `DAMAGE`=? WHERE UUID='" + uuid.toString() + "'");
			ps.setDouble(1, (getDamage(uuid) + damage));

			ps.executeUpdate();
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public double getDamage(UUID uuid) {
		try {
			System.out.print("getting damage");
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT DAMAGE FROM `ShareDamage` WHERE UUID='" + uuid.toString() + "'");
			System.out.print("setting string");

			System.out.print("executing query");
			ResultSet rs = ps.executeQuery();
			double damage = 0.0;
			
			
			if (rs.next()) {
				System.out.print("getting damage");
				damage = rs.getDouble("DAMAGE");
				System.out.print("executing query" + damage);
				return damage;
			}
		
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

}
