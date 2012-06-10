package net.mdcreator.tpplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class TPPlusExecutor implements CommandExecutor{

    TPPlus plugin;
    private String title = ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "TP+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    public TPPlusExecutor(TPPlus plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PluginDescriptionFile pdf = plugin.getDescription();
        if(args.length==0){
            sender.sendMessage(new String[] {
                    title + "info",
                    ChatColor.DARK_GRAY + "by " + ChatColor.GRAY + pdf.getAuthors().get(0),
                    ChatColor.DARK_GRAY + "vers " + ChatColor.GRAY + pdf.getVersion(),
                    ChatColor.GRAY + pdf.getDescription()
            });
            return true;
        } else if(args.length==1){
            if(args[0].equals("update")){
                try {
                    sender.sendMessage(title + "Fetching plugin online " + ChatColor.DARK_GRAY + "{------}");
                    URL onlinePlugin = new URL("https://github.com/Gratimax/TPPlus/blob/master/deploy/TPPlus.jar?raw=true");
                    ReadableByteChannel rbc = Channels.newChannel(onlinePlugin.openStream());
                    sender.sendMessage(title + "Fetching local copy " + ChatColor.DARK_GRAY + "{==----}");
                    FileOutputStream fos = new FileOutputStream(plugin.getDataFolder().getParentFile().getPath() + "\\TPPlus.jar");
                    sender.sendMessage(title + "Copying repositories " + ChatColor.DARK_GRAY + "{====--}");
                    fos.getChannel().transferFrom(rbc, 0, 1 << 24);
                    sender.sendMessage(title + "Finished, reloading server " + ChatColor.DARK_GRAY + "{======}");
                    plugin.getLogger().info("Plugin update completed.");
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return true;
            } else return false;
        }
        return false;
    }
}
