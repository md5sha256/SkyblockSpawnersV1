package com.gmail.andrewandy.spawnerplugin.listener;

import com.gmail.andrewandy.spawnerplugin.SpawnerPlugin;
import com.gmail.andrewandy.spawnerplugin.data.DataUtil;
import com.gmail.andrewandy.spawnerplugin.event.SpawnerBreakEvent;

import com.gmail.andrewandy.spawnerplugin.util.Common;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/*
Well aware this is inefficient. Will be fixed.
 */

public class BlockBreakListener implements Listener {

    public BlockBreakListener() {
        SpawnerPlugin.getInstance().getServer().getPluginManager().registerEvents(this, SpawnerPlugin.getInstance());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof CreatureSpawner)) {
            return;
        }
        Spawner spawner = SpawnerPlugin.getSpawnerCache().getFromCache(Spawner.asIdentifier(event.getBlock().getLocation()));
        if (spawner == null) {
            spawner = DataUtil.loadData(event.getBlock().getLocation());
        }
        if (spawner == null) {
            return;
        }
        SpawnerBreakEvent spawnerBreakEvent = new SpawnerBreakEvent(event.getPlayer(), spawner);
        if (spawnerBreakEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }
        DataUtil.clearData(spawner);
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Common.colourise("&e" + Common.capitalise(spawner.getSpawnedType().name().toLowerCase()) + "&e Spawner"));
        itemMeta.setLore(Arrays.asList(
                Common.colourise(""),
                Common.colourise("&b&lInformation:"),
                Common.colourise("  &7-&a Mob: " + Common.capitalise(spawner.getSpawnedType().name().toLowerCase())),
                Common.colourise("  &7-&e Level: " + spawner.getLevel())
        ));
        item.setItemMeta(itemMeta);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("spawner", "true");
        nbtItem.setString("entityType", spawner.getSpawnedType().name());
        nbtItem.setInteger("delay", spawner.getDelay());
        ItemStack finalItem = nbtItem.getItem();
        World world = spawner.getLocation().getWorld();
        event.getBlock().setType(Material.AIR);
        world.dropItemNaturally(spawner.getLocation(), finalItem);
        SpawnerPlugin.getSpawnerCache().purge(spawner);
    }


}
