package de.devcyntrix.sqleco;

import de.devcyntrix.sqleco.api.storage.PlayerAccountStorage;
import de.devcyntrix.sqleco.storage.SQLPlayerAccountStorage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

public class SQLEcoPlugin extends JavaPlugin {

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

        getServer().getServicesManager().register(Economy.class, sqlEco, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {

    }

    public PlayerAccountStorage getStorage() {
        return storage;
    }
}
