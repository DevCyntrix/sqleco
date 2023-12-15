package de.devcyntrix.sqleco.storage;

import de.devcyntrix.sqleco.api.storage.PlayerAccountStorage;
import de.devcyntrix.sqleco.model.PlayerAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.UUID;
import java.util.function.Supplier;

public class SQLPlayerAccountStorage implements PlayerAccountStorage {

    private final Supplier<Connection> supplier;

    public SQLPlayerAccountStorage(Supplier<Connection> supplier) throws SQLException {
        this.supplier = supplier;

        try (Connection connection = supplier.get()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS economy (id VARCHAR(37) NOT NULL, balance DECIMAL NOT NULL DEFAULT 0, PRIMARY KEY (id));");
        }
    }

    @Override
    public void addBalance(@NotNull UUID id, double balance) {
        try (Connection connection = supplier.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO economy (id, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = balance + ?;");
            preparedStatement.setString(1, id.toString());
            preparedStatement.setDouble(2, balance);
            preparedStatement.setDouble(3, balance);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAccount(@NotNull PlayerAccount account) {
        try (Connection connection = supplier.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO economy (id, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = ?;");
            preparedStatement.setString(1, account.getId().toString());
            preparedStatement.setDouble(2, account.getBalance());
            preparedStatement.setDouble(3, account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable PlayerAccount getAccount(@NotNull UUID id, @Nullable Supplier<PlayerAccount> defaultReturn) {

        try (Connection connection = supplier.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM economy WHERE id = ?;");
            preparedStatement.setString(1, id.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                return defaultReturn == null ? null : defaultReturn.get();
            }
            return new PlayerAccount(id, resultSet.getDouble("balance"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAccount(@NotNull UUID id) {
        try (Connection connection = supplier.get()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM economy WHERE id = ?;");
            preparedStatement.setString(1, id.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
