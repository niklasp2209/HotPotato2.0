package de.bukkitnews.hotpotato.module.game.listener.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void handleEntityDamage(@NotNull EntityDamageEvent event) {
        event.setCancelled(true);
    }
}
