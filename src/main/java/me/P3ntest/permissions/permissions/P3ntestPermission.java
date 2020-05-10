package me.P3ntest.permissions.permissions;

import javax.annotation.Nullable;

public class P3ntestPermission {

    private String permission;
    private long entTimestamp;

    public String getPermission() {return permission;}

    public long getEndTimestamp() {return entTimestamp;}


    public P3ntestPermission(String permission, @Nullable long endTimestamp) {
        this.permission = permission;
        this.entTimestamp = endTimestamp;
    }

}
