package ua.govnojon.jesusmode.listeners;

import org.bukkit.Location;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.PistonBaseMaterial;
import ua.govnojon.jesusmode.JesusModePlugin;
import ua.govnojon.jesusmode.jesusmode.JesusMode;

public class Listeners implements Listener {

    private JesusModePlugin jesusModePlugin = JesusModePlugin.getInstance();

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        JesusMode jesusMode = jesusModePlugin.getJesusMode().get(player);
        if (jesusMode != null) {
            jesusMode.removeBlockAll();
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (this.jesusModePlugin.getFakeBlocks().contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (this.jesusModePlugin.getFakeBlocks().contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonExtendEvent(BlockPistonExtendEvent event) {
        if (this.jesusModePlugin.getFakeBlocks().contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonRetractEvent(BlockPistonRetractEvent event) {
        if (this.jesusModePlugin.getFakeBlocks().contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        event.blockList().removeAll(this.jesusModePlugin.getFakeBlocks());
    }

    @EventHandler
    public void onEntityExplodeEvent(BlockBurnEvent event) {
        if (this.jesusModePlugin.getFakeBlocks().contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFormEvent(BlockFormEvent event) {
        if (this.jesusModePlugin.getFakeBlocks().contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Location t = event.getTo();
        Location f = event.getFrom();

        if (event.getPlayer().isFlying())
            return;

        //move block
        if (t.getBlockX() != f.getBlockX() || t.getBlockY() != f.getBlockY() || t.getBlockZ() != f.getBlockZ()) {
            JesusMode jesusMode = jesusModePlugin.getJesusMode().get(event.getPlayer());
            if (jesusMode != null) {
                jesusMode.removeBlocks();
                jesusMode.newPosition(event.getTo());
            }
        }
    }


}
