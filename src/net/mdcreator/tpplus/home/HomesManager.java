package net.mdcreator.tpplus.home;

import net.mdcreator.tpplus.TPPlus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HomesManager {

    public HashMap<String, Home> homes = new HashMap<String, Home>();
    private TPPlus plugin;
    public Set<String> openHomes = new HashSet<String>();

    public HomesManager(TPPlus plugin){
        this.plugin = plugin;
    }

}

