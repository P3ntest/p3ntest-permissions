package me.P3ntest.permissions.permissions;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;

public class P3ntestRank {

    private int rankId, power;
    private long endTimestamp;
    private String prefix;

    public String getDisplayName() {return displayName;}

    private String displayName;
    private ChatColor color;

    public int getRankId() {return rankId;}

    public long getEndTimestamp() {return endTimestamp;}

    public void setEndTimestamp(long end) {this.endTimestamp = end;}

    public int getPower() {return power;}

    public ChatColor getColor() {return color;}

    public P3ntestRank(int rankId, @Nullable long endTimestamp, int power, ChatColor color, String prefix, String displayName) {
        this.rankId = rankId;
        this.endTimestamp = endTimestamp;
        this.power = power;
        this.color = color;
        this.displayName = displayName;
        this.prefix = prefix;
    }

    public String getPrefix() {return prefix;}
}
