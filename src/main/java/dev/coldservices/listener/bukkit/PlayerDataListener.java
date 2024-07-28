package dev.coldservices.listener.bukkit;

import dev.coldservices.CAC;
import dev.coldservices.data.PlayerData;
import dev.coldservices.data.PlayerDataManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDataListener implements Listener {

    @EventHandler
    public void onVelocity(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof Player) {
            Player player = (Player) entity;

            if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                PlayerData data = CAC.get(PlayerDataManager.class).getPlayerData(player.getUniqueId());

                if(data == null) return;

                data.getVelocityTracker().setFellDown(true);
            }
        }
    }
}
