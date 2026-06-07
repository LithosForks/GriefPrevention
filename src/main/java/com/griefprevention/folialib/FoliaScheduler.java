package com.griefprevention.folialib;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/**
 * copied from network.lithos.minecraft.folialib
 * @author Eli
 * @since April 27, 2026
 */
public final class FoliaScheduler {
    private final Plugin plugin;
    public FoliaScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    private long min1(long ticks) {
        return Math.max(ticks, 1);
    }

    /// run a sync task on the region of the entity
    public void runTask(Entity entity, Runnable task) {
        if (PlatformUtil.isFolia()) {
            if (plugin.getServer().isOwnedByCurrentRegion(entity)) {
                task.run();
            }
            else {
                entity.getScheduler().run(plugin, _ -> task.run(), task);
            }
        }
        else {
            if (plugin.getServer().isPrimaryThread()) {
                task.run();
            }
            else {
                plugin.getServer().getScheduler().runTask(plugin, task);
            }
        }
    }

    /// run a sync task on the global region
    public void runGlobalTask(Runnable task) {
        if (PlatformUtil.isFolia()) {
            plugin.getServer().getGlobalRegionScheduler().execute(plugin, task);
        }
        else {
            if (plugin.getServer().isPrimaryThread()) {
                task.run();
            }
            else {
                plugin.getServer().getScheduler().runTask(plugin, task);
            }
        }
    }

    /// run a sync delayed task on the global region
    public void runGlobalTaskLater(long delayTicks, Runnable task) {
        if (PlatformUtil.isFolia()) {
            plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, _ -> task.run(), min1(delayTicks));
        }
        else {
            plugin.getServer().getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    /// run a sync repeating task on the global region
    public void runGlobalRepeatingTask(long delayTicks, long periodTicks, Runnable task) {
        if (PlatformUtil.isFolia()) {
            plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, _ -> task.run(), min1(delayTicks), periodTicks);
        }
        else {
            plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, delayTicks, periodTicks);
        }
    }

    /// run an asynchronous repeating task
    public void runAsyncRepeatingTask(long delayTicks, long periodTicks, Runnable task) {
        if (PlatformUtil.isFolia()) {
            plugin.getServer().getAsyncScheduler().runAtFixedRate(
                    plugin, _ -> task.run(), min1(delayTicks * 50L), periodTicks * 50L, TimeUnit.MILLISECONDS
            );
        }
        else {
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, task, delayTicks, periodTicks);
        }
    }

    /// run a sync delayed task on the region of the entity
    public void runTaskLater(Entity entity, long delayTicks, Runnable task) {
        if (PlatformUtil.isFolia()) {
            entity.getScheduler().runDelayed(plugin, _ -> task.run(), task, min1(delayTicks));
        }
        else {
            plugin.getServer().getScheduler().runTaskLater(plugin, task, delayTicks);
        }
    }

    /// run an asynchronous task
    public void runAsync(Runnable task) {
        if (PlatformUtil.isFolia()) {
            plugin.getServer().getAsyncScheduler().runNow(plugin, _ -> task.run());
        }
        else {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task);
        }
    }
}
