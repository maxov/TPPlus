package net.mdcreator.tpplus.tp;

import org.bukkit.entity.Player;

public class TpPair {

    public Player request;
    public Player target;

    public TpPair (Player req, Player tar){
        request = req;
        target = tar;
    }

    public boolean equals(Object obj){
        TpPair other = (TpPair) obj;
        return other!=null && request.equals(other.request) && target.equals(other.target);
    }

}
