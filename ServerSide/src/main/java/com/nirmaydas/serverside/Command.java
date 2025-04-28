package com.nirmaydas.serverside;
import java.util.HashMap;
import java.util.Map;

public class Command {
    private String command;
    private int itemId;
    private String memberId;
    private String password;
    private Map<String, String> commandDetails;

    public Command(String command) {
        this.command = command;
        this.itemId = 0;
        this.memberId = null;
        this.password = null;
        this.commandDetails = new HashMap<>();
    }

    public Command(String command, int itemId, String memberId) {
        this.command = command;
        this.itemId = itemId;
        this.memberId = memberId;
        this.password = null;
        this.commandDetails = new HashMap<>();
    }

    public String getCommand() {
        return command;
    }

    public int getItemId() {
        return itemId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getPassword() {
        return password;
    }

    public String getString(String key) {
        return commandDetails.get(key);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCommandDetail(String key, String value) {
        this.commandDetails.put(key, value);
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}