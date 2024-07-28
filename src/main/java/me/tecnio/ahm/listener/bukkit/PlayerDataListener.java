package me.tecnio.ahm.listener.bukkit;

import me.tecnio.ahm.AHM;
import me.tecnio.ahm.data.PlayerData;
import net.minecraft.server.v1_8_R3.EntityPlayer;
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
                PlayerData data = AHM.INSTANCE.getDataManager().getPlayerData(player.getUniqueId());

                if(data == null) return;

                data.getVelocityTracker().setFellDown(true);
            }
        }
    }
}
