package net.mdcreator.tpplus.tp;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TpManager {

    public Set<TpPair> pairs = new HashSet<TpPair>();

    public TpPair get(Player target, Player request){
        for(TpPair pair: pairs){
            if(pair.equals(new TpPair(target, request)))  return pair;
        }
        return null;
    }
}
