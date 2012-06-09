package net.mdcreator.tpplus.spawn;

import net.mdcreator.tpplus.TPPlus;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;

public class SpawnManager {

    private TPPlus plugin;
    public Location spawnLoc;

    public SpawnManager(TPPlus plugin){
        this.plugin = plugin;
        Server server = plugin.getServer();
        FileConfiguration config = plugin.configYML;
        spawnLoc = new Location(
                server.getWorld(config.getString("spawn.world")),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                (float) config.getDouble("spawn.pitch"),
                (float) config.getDouble("spawn.yaw")
        );
    }
}
