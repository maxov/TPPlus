package net.mdcreator.tpplus.home;

import net.mdcreator.tpplus.TPPlus;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.dynmap.markers.Marker;

import java.util.Map;

public class HomeExecutor implements CommandExecutor{

    private TPPlus plugin;
    private String title = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "TP+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    public HomeExecutor(TPPlus plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player send;
        if(!(sender instanceof Player)){
            sender.sendMessage(title + ChatColor.RED + "Player context is required!");
            return true;
        }
        send = (Player) sender;
        // /home
        if(args.length==0){
             if(plugin.homesManager.homes.containsKey(send.getName())){
                Home home = plugin.homesManager.homes.get(send.getName());
                Location loc = send.getLocation();
                send.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                send.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                send.getWorld().playEffect(loc, Effect.STEP_SOUND, 51);
                loc = home.pos;
                send.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
                loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
                send.teleport(home.pos);
                loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 51);
                send.sendMessage(title + "Home, sweet home.");
            } else{
                sender.sendMessage(title + ChatColor.RED + "You need a home!");
            }
            return true;
        }else if(args.length==1){

            // /home help
            if(args[0].equals("help")){
                return false;
            } else

            // /home set
            if(args[0].equals("set")){
                Location loc = send.getLocation();
                plugin.homesManager.homes.put(send.getName(), new Home(loc));
                FileConfiguration config = plugin.homesYML;
                String name = send.getName();
                boolean newHome;
                newHome = config.getString(send.getName()) == null;
                config.set(name + ".x", loc.getX());
                config.set(name + ".y", loc.getY());
                config.set(name + ".z", loc.getZ());
                config.set(name + ".yaw", loc.getPitch());
                config.set(name + ".pitch", loc.getYaw());
                config.set(name + ".world", loc.getWorld().getName());
                if(newHome) config.set(name + ".open", false);
                plugin.saveHomes();
                Marker marker = plugin.icons.findMarker(send.getName());
                if(marker==null){
                    String plabel = "";
                    plabel+= (send.getName().toLowerCase().endsWith("s") ? send.getName() + "'" : send.getName() + "'s");
                    plabel+= " home";
                    marker = plugin.icons.createMarker(send.getName(), plabel, true,
                            loc.getWorld().getName(),
                            loc.getX(),
                            loc.getY(),
                            loc.getZ(),
                            plugin.dynmapAPI.getMarkerAPI().getMarkerIcon("house"),
                            true
                    );
                }
                marker.setLocation(loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ());
                loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 51);
                send.sendMessage(title + "Home set.");
            } else

            // /home open
            if(args[0].equals("open")){
                if(plugin.homesManager.homes.containsKey(send.getName())){
                    plugin.homesManager.openHomes.add(send.getName());
                    FileConfiguration config = plugin.homesYML;
                    config.set(send.getName() + ".open", true);
                    plugin.saveHomes();
                    Location home = plugin.homesManager.homes.get(send.getName()).pos;
                    String plabel = "";
                    plabel+= (send.getName().toLowerCase().endsWith("s") ? send.getName() + "'" : send.getName() + "'s");
                    plabel+= " home (open)";
                    plugin.icons.findMarker(send.getName()).setLabel(plabel);
                    home.getWorld().playEffect(home, Effect.ENDER_SIGNAL, 1);
                    home.getWorld().playEffect(home, Effect.MOBSPAWNER_FLAMES, 1);
                    home.getWorld().playEffect(home, Effect.STEP_SOUND, 111);
                    send.sendMessage(title + "Your home is now " + ChatColor.DARK_GREEN + "open" + ChatColor.GRAY + " to guests.");
                } else{
                    send.sendMessage(title + ChatColor.RED + "You need a home!");
                }
            } else

            // /home close
            if(args[0].equals("close")){
                if(plugin.homesManager.homes.containsKey(send.getName())){
                    plugin.homesManager.openHomes.remove(send.getName());
                    FileConfiguration config = plugin.homesYML;
                    config.set(send.getName() + ".open", false);
                    plugin.saveHomes();
                    Location home = plugin.homesManager.homes.get(send.getName()).pos;
                    String plabel = "";
                    plabel+= (send.getName().toLowerCase().endsWith("s") ? send.getName() + "'" : send.getName() + "'s");
                    plabel+= " home";
                    plugin.icons.findMarker(send.getName()).setLabel(plabel);
                    home.getWorld().playEffect(home, Effect.ENDER_SIGNAL, 1);
                    home.getWorld().playEffect(home, Effect.MOBSPAWNER_FLAMES, 1);
                    home.getWorld().playEffect(home, Effect.STEP_SOUND, 40);
                    send.sendMessage(title + "Your home is now " + ChatColor.DARK_RED + "closed" + ChatColor.DARK_GRAY + " to guests.");
                } else{
                    send.sendMessage(title + ChatColor.RED + "You need a home!");
                }
            } else

            // /home list
            if(args[0].equals("list")){
                String fin = "";
                for(Map.Entry<String, Home> home: plugin.homesManager.homes.entrySet()){
                    fin+= (plugin.homesManager.openHomes.contains(home.getKey()) ? ChatColor.DARK_GREEN : ChatColor.DARK_RED) + home.getKey() + ChatColor.GRAY + " ";
                }
                send.sendMessage(title + "Homes");
                send.sendMessage(fin);
            } else

            // /home [player]
            {
                Player target = plugin.getServer().getPlayer(args[0]);
                String name = null;
                if(target==null){
                    for(String player : plugin.homesManager.homes.keySet()){
                        if(player.toLowerCase().startsWith(args[0].toLowerCase())){
                            name = player;
                        }
                    }
                    if(name==null){
                        send.sendMessage(title + ChatColor.RED + "That player does not exist!");
                        return true;
                    }
                } else name = target.getName();
                if(!plugin.homesManager.homes.containsKey(name)){
                    send.sendMessage(title + ChatColor.RED + "That player does not have a home!");
                }else if(!plugin.homesManager.openHomes.contains(name)&&!send.isOp()&&!send.getName().equals(target.getName())){
                    send.sendMessage(title + ChatColor.RED + "That player's home is " + ChatColor.DARK_RED + "closed" + ChatColor.RED + "!");
                } else{
                    Location loc = send.getLocation();
                    Home home = plugin.homesManager.homes.get(name);
                    send.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                    send.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                    send.getWorld().playEffect(loc, Effect.STEP_SOUND, 51);
                    loc = home.pos;
                    loc.getWorld().loadChunk(loc.getWorld().getChunkAt(loc));
                    send.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
                    send.teleport(home.pos);
                    loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                    loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                    loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 51);
                    send.sendMessage(title + "Welcome.");
                }
            }
            return true;
        } else if(args.length==2){
            if(args[0].equals("set")&&args[1].equals("bed")){
                Location loc = send.getBedSpawnLocation();
                if(loc==null){
                    send.sendMessage(title + ChatColor.RED + "You need a bed!");
                    return true;
                }
                plugin.homesManager.homes.put(send.getName(), new Home(loc));
                FileConfiguration config = plugin.homesYML;
                String name = send.getName();
                boolean newHome;
                newHome = config.getString(send.getName()) == null;
                config.set(name + ".x", loc.getX());
                config.set(name + ".y", loc.getY());
                config.set(name + ".z", loc.getZ());
                config.set(name + ".yaw", loc.getPitch());
                config.set(name + ".pitch", loc.getYaw());
                config.set(name + ".world", loc.getWorld().getName());
                if(newHome) config.set(name + ".open", false);
                plugin.saveHomes();
                loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
                loc.getWorld().playEffect(loc, Effect.STEP_SOUND, 51);
                send.sendMessage(title + "Home set to bed.");
                return true;
            }
            return false;
        }
        return false;
    }
}
