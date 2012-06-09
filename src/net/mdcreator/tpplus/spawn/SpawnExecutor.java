package net.mdcreator.tpplus.spawn;

import net.mdcreator.tpplus.TPPlus;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SpawnExecutor implements CommandExecutor{

    private TPPlus plugin;
    private String title = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "TP+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
    private SpawnManager spawnManager;

    public SpawnExecutor(TPPlus plugin){
        this.plugin = plugin;
        spawnManager = plugin.spawnManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player send;
        if(!(sender instanceof Player)){
            sender.sendMessage(title + ChatColor.RED + "Player context required!");
            return true;
        }
        send = (Player) sender;
        // /spawn
        if(args.length == 0){
            Location loc = send.getLocation();
            send.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
            send.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
            send.getWorld().playEffect(loc, Effect.STEP_SOUND, 8);
            loc = spawnManager.spawnLoc;
            send.teleport(loc);
            send.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
            send.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
            send.getWorld().playEffect(loc, Effect.STEP_SOUND, 8);
            send.sendMessage(title + "Welcome to spawn.");
            return true;
        } else if(args.length == 1){

            // /spawn help
            if(args[0].equals("help")){
                return false;
            } else

            // /spawn set
            if(args[0].equals("set")){
                if(!send.isOp()){
                    send.sendMessage(title + ChatColor.RED + "You can't set the spawn!");
                } else{
                    Location loc = send.getLocation();
                    spawnManager.spawnLoc = loc;
                    loc.getWorld().setSpawnLocation((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
                    FileConfiguration config = plugin.configYML;
                    config.set("spawn.x", loc.getX());
                    config.set("spawn.y", loc.getY());
                    config.set("spawn.z", loc.getZ());
                    config.set("spawn.pitch", loc.getPitch());
                    config.set("spawn.yaw", loc.getYaw());
                    config.set("spawn.world", loc.getWorld().getName());
                    plugin.saveConfig();
                    send.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                    send.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                    send.getWorld().playEffect(loc, Effect.STEP_SOUND, 8);
                    send.sendMessage(title + "Spawn set.");
                }
                return true;
            } else{
                return false;
            }
        }
        return false;
    }
}
