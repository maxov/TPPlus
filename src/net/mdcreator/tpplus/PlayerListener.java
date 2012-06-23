package net.mdcreator.tpplus;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private TPPlus plugin;
    private String title = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "TP+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    public PlayerListener(TPPlus plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        if(!player.hasPlayedBefore()){
            FileConfiguration config = plugin.configYML;
            final Location loc = new Location(
                    plugin.getServer().getWorld(config.getString("spawn.world")),
                    config.getDouble("spawn.y"),
                    config.getDouble("spawn.x"),
                    config.getDouble("spawn.z"),
                    (float) config.getDouble("spawn.pitch"),
                    (float) config.getDouble("spawn.yaw")
            );
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
                    player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
                    player.teleport(loc);
                    loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                    loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                    loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 90);
                }
            });
            plugin.getServer().broadcastMessage(ChatColor.YELLOW + "Everyone welcome " + player.getName() + " to the server!");
            event.setJoinMessage("");
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedEnterEvent event){
        Player player = event.getPlayer();
        if(!plugin.homesManager.homes.containsKey(player.getName())){
            player.sendMessage(title + "To set your bed as home, say " + ChatColor.DARK_GRAY +"/home set bed" + ChatColor.GRAY + ".");
        }
    }
}
