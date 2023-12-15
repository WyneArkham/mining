package io.github.ardryck.mining.listeners;

import io.github.ardryck.mining.utils.DateUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player minePlayer = event.getPlayer();
        World mineWorld = minePlayer.getWorld();

        if (event.getFrom().getName().equalsIgnoreCase("mina") || mineWorld.getName().equalsIgnoreCase("mina_nether")) {
            if (minePlayer.hasPotionEffect(PotionEffectType.FAST_DIGGING))
                minePlayer.removePotionEffect(PotionEffectType.FAST_DIGGING);
        }

        if (mineWorld.getName().equalsIgnoreCase("mina") || mineWorld.getName().equalsIgnoreCase("mina_nether")) {
            minePlayer.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();

            if (player.getWorld().getName().equalsIgnoreCase("mina") || player.getWorld().getName().equalsIgnoreCase("mina_nether")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player minePlayer = (Player)event.getEntity();

            if (minePlayer.getWorld().getName().equalsIgnoreCase("mina")) {
                if (DateUtil.isNight()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
