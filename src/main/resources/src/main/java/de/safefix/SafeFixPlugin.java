package de.safefix;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SafeFixPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SafeFix enabled");
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {

        if (!(event.getEntity() instanceof TextDisplay)) return;

        Location loc = event.getLocation();
        Chunk chunk = loc.getChunk();

        if (!chunk.isLoaded()) {
            event.setCancelled(true);

            Bukkit.getScheduler().runTaskLater(this, () -> safeSpawn(loc), 1L);
        }
    }

    private void safeSpawn(Location loc) {

        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load(true);
        }

        Bukkit.getScheduler().runTask(this, () -> {
            TextDisplay d = (TextDisplay)
                    loc.getWorld().spawnEntity(loc, EntityType.TEXT_DISPLAY);

            d.setBillboard(Display.Billboard.CENTER);
            d.setPersistent(false);
        });
    }
}
