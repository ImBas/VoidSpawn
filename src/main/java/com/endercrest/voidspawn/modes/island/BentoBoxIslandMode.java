package com.endercrest.voidspawn.modes.island;

import com.endercrest.voidspawn.TeleportResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;

public class BentoBoxIslandMode extends BaseIslandMode {
    private final BentoBox bentoBox;

    public BentoBoxIslandMode() {
        this.bentoBox = (BentoBox) Bukkit.getPluginManager().getPlugin("BentoBox");
        if (this.bentoBox == null) {
            throw new IllegalStateException("BentoBoxIslandMode is missing!");
        }
    }

    @Override
    public TeleportResult onActivateIsland(Player player, String worldName) {
        World world = player.getWorld();
        User user = User.getInstance(player);

        // Check if world is an island world (this check is for a bug in before v1.13)
        if (!bentoBox.getIWM().inWorld(world))
            return TeleportResult.MISSING_ISLAND;

        // First checks if spawn can be found in current world. If not, iterate through worlds until we find one.
        Location location = bentoBox.getIslands().getHomeLocation(world, user);
        if (location == null) {
            for (World w: Bukkit.getWorlds()) {
                location = bentoBox.getIslands().getHomeLocation(w, user);
                if (location != null) break;
            }
        }

        if (location != null) {
            player.teleport(location);
            return TeleportResult.SUCCESS;
        }
        return TeleportResult.MISSING_ISLAND;
    }

    @Override
    public boolean isEnabled() {
        return isModeEnabled();
    }

    public static boolean isModeEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("BentoBox");
    }
}
