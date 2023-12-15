package de.devcyntrix.sqleco;

import de.devcyntrix.sqleco.model.PlayerAccount;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SQLEcoImpl implements Economy {

    private static final NumberFormat FORMAT = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.GERMAN));
    private final SQLEcoPlugin plugin;

    public SQLEcoImpl(SQLEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return plugin.getName();
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double v) {
        return "%s %s".formatted(FORMAT.format(v), v == 1 ? currencyNameSingular() : currencyNamePlural());
    }

    @Override
    public String currencyNamePlural() {
        return "Coins";
    }

    @Override
    public String currencyNameSingular() {
        return "Coin";
    }

    @Override
    public boolean hasAccount(String s) {
        return hasAccount(Bukkit.getOfflinePlayer(s));
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String name) {
        if (name == null)
            return 0;
        return getBalance(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public double getBalance(@Nullable OfflinePlayer offlinePlayer) {
        if (offlinePlayer == null)
            return 0;
        PlayerAccount account = plugin.getStorage().getAccount(offlinePlayer.getUniqueId(), () -> new PlayerAccount(offlinePlayer.getUniqueId(), 0));
        return account.getBalance();
    }

    @Override
    public double getBalance(String name, String world) {
        if (name == null)
            return 0;
        return getBalance(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String world) {
        return getBalance(offlinePlayer);
    }

    @Override
    public boolean has(String name, double amount) {
        if (name == null)
            return false;
        return has(Bukkit.getOfflinePlayer(name), amount);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        if (offlinePlayer == null)
            return false;
        return getBalance(offlinePlayer) >= amount;
    }

    @Override
    public boolean has(String name, String world, double amount) {
        return has(name, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String world, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(name), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player, -amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return withdrawPlayer(name, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(name), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        try {
            plugin.getStorage().addBalance(player.getUniqueId(), amount);
        } catch (Exception e) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
        double balance = getBalance(player);
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return depositPlayer(name, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String name) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String name, String world) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        return true;
    }

}
