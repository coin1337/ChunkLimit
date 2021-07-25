package me.christallinqq.chunklimit;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChunkLimit extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info(ChatColor.RED + "Loading config");
        saveDefaultConfig();

        getLogger().info(ChatColor.RED + "Registering listener");
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info(ChatColor.RED + "Done! :D");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Chunk chunk = block.getChunk();
        int c = getConfig().getInt("chests-per-chunk");
        int s = getConfig().getInt("shulkers-per-chunk");

        if (block.getType().toString().contains("CHEST")
                && getAmountOf(chunk, "CHEST") >= c) {
            event.setBuild(false);
            player.sendMessage(format(getConfig().getString("chest-message").replace("%chests_per_chunk%", String.valueOf(c))));
        } else if (block.getType().toString().contains("SHULKER_BOX")
                && getAmountOf(chunk, "SHULKER_BOX") >= s) {
            event.setBuild(false);
            player.sendMessage(format(getConfig().getString("shulker-message").replace("%shulkers_per_chunk%", String.valueOf(s))));
        }
    }

    public static int getAmountOf(final Chunk chunk, final String material) {
        int amount = 0;
        int bx = chunk.getX()<<4;
        int bz = chunk.getZ()<<4;

        World world = chunk.getWorld();

        for(int xx = bx; xx < bx+16; xx++) {
            for(int zz = bz; zz < bz+16; zz++) {
                for(int yy = 0; yy < 128; yy++) {
                    if (world.getBlockAt(xx, yy, zz).getType().toString().contains(material)) {
                        ++amount;
                    }
                }
            }
        }

        return amount;
    }

    private String format(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}

