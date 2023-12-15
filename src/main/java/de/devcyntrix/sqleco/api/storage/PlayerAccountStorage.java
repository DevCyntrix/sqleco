package de.devcyntrix.sqleco.api.storage;

import de.devcyntrix.sqleco.model.PlayerAccount;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Supplier;

public interface PlayerAccountStorage {

    void addBalance(@NotNull UUID id, double balance);

    void updateAccount(@NotNull PlayerAccount account);

    default @Nullable PlayerAccount getAccount(@NotNull UUID id) {
        return getAccount(id, null);
    }

    @Nullable PlayerAccount getAccount(@NotNull UUID id, @Nullable Supplier<PlayerAccount> defaultReturn);

    void deleteAccount(@NotNull UUID id);

}
