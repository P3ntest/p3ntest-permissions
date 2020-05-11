package me.P3ntest.permissions.permissions;

import org.bukkit.ChatColor;

import javax.annotation.Nullable;

public class P3ntestRank {

    private int rankId;
    private int power;

    public int getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(int assignedId) {
        this.assignedId = assignedId;
    }

    private int assignedId;
    private long endTimestamp;
    private String prefix;

    public String getHumanId() {
        return humanId;
    }

    private String humanId;

    public String getDisplayName() {return displayName;}

    private String displayName;
    private ChatColor color;

    public int getRankId() {return rankId;}

    public long getEndTimestamp() {return endTimestamp;}

    public void setEndTimestamp(long end) {this.endTimestamp = end;}

    public int getPower() {return power;}

    public ChatColor getColor() {return color;}

    public P3ntestRank(int rankId, @Nullable long endTimestamp, int power, ChatColor color, String prefix, String displayName, String humanId) {
        this.rankId = rankId;
        this.endTimestamp = endTimestamp;
        this.power = power;
        this.color = color;
        this.displayName = displayName;
        this.prefix = prefix;
        this.humanId = humanId;
    }

    public String getPrefix() {return prefix;}
}
