package de.devcyntrix.sqleco;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

public class SQLEcoPlugin extends JavaPlugin implements Listener {

    private PlayerAccountStorage storage;
    private SQLEcoImpl sqlEco;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        String url = getConfig().getString("database.url");
        String username = getConfig().getString("database.username");
        String password = getConfig().getString("database.password");

        Supplier<Connection> supplier = () -> {
            try {
                return DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };

        try {
            this.storage = new SQLPlayerAccountStorage(supplier);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.sqlEco = new SQLEcoImpl(this);

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getServicesManager().register(Economy.class, sqlEco, this, ServicePriority.Normal);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Economy load = getServer().getServicesManager().load(Economy.class);
        if(load == null) {
            player.sendMessage("§cCannot find Economy plugin");
            return;
        }

        player.sendMessage("Your balance is %s".formatted(load.format(load.getBalance(player))));
    }

    @Override
    public void onDisable() {

    }

    public PlayerAccountStorage getStorage() {
        return storage;
    }
}
