package net.mdcreator.tpplus;

import net.mdcreator.tpplus.home.HomeExecutor;
import net.mdcreator.tpplus.home.HomesManager;
import net.mdcreator.tpplus.spawn.SpawnExecutor;
import net.mdcreator.tpplus.spawn.SpawnManager;
import net.mdcreator.tpplus.tp.TPExecutor;
import net.mdcreator.tpplus.tp.TpManager;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;

import java.io.*;
import java.util.Set;

public class TPPlus extends JavaPlugin{

    public File dataFolder;
    public File configFile;
    public FileConfiguration configYML;
    public File homesFile;
    public FileConfiguration homesYML;
    public HomesManager homesManager;
    public SpawnManager spawnManager;
    public TpManager tpManager;

    public CommandExecutor homeExecutor;
    public CommandExecutor spawnExecutor;
    public CommandExecutor tpExecutor;
    public TPPlusExecutor tpPlusExecutor;
    public PlayerListener playerListener;

    public void onEnable(){
        dataFolder = getDataFolder();
        configFile = new File(dataFolder, "config.yml");
        homesFile = new File(dataFolder, "homes.yml");
        homesManager = new HomesManager(this);
        tpManager = new TpManager();
        reloadHomes();

        spawnManager = new SpawnManager(this);
        homeExecutor = new HomeExecutor(this);
        spawnExecutor = new SpawnExecutor(this);
        tpPlusExecutor = new TPPlusExecutor(this);

        tpExecutor = new TPExecutor(this);

        playerListener = new PlayerListener(this);

        getCommand("home").setExecutor(homeExecutor);
        getCommand("spawn").setExecutor(spawnExecutor);
        getCommand("tele").setExecutor(tpExecutor);
        getCommand("tpplus").setExecutor(tpPlusExecutor);

    }

    public void onDisable(){
    }

    public void reloadHomes(){
        if(!dataFolder.exists()) try {
            dataFolder.mkdir();
            homesFile.createNewFile();
            configFile.createNewFile();
            copyFile("/ext/homes.yml", homesFile);
            copyFile("/ext/config.yml", configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        configYML = getConfig();
        homesYML = YamlConfiguration.loadConfiguration(homesFile);
        Set<String> homes = homesYML.getKeys(false);
        for(String player: homes){
            homesManager.homes.put(player, new Home(new Location(
                    getServer().getWorld(homesYML.getString(player + ".world")),
                    homesYML.getDouble(player + ".x"),
                    homesYML.getDouble(player + ".y"),
                    homesYML.getDouble(player + ".z"),
                    (float) homesYML.getDouble(player + ".pitch"),
                    (float) homesYML.getDouble(player + ".yaw")
            )));
            if(homesYML.getBoolean(player + ".open")) homesManager.openHomes.add(player);
        }
    }

    public void saveHomes(){
        try {
            homesYML.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyFile(String from, File to){
        try {
            InputStream in = getClass().getResourceAsStream(from);
            OutputStream out = new FileOutputStream(to);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}