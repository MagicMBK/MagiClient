package com.myclient.module;

import com.myclient.MyClient;
import net.minecraft.client.MinecraftClient;

public abstract class Module {
    
    protected static final MinecraftClient mc = MinecraftClient.getInstance();
    
    private String name;
    private String description;
    private int key;
    private Category category;
    private boolean enabled;
    
    public Module(String name, String description, int key, Category category) {
        this.name = name;
        this.description = description;
        this.key = key;
        this.category = category;
        this.enabled = false;
    }
    
    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }
    
    public void enable() {
        enabled = true;
        MyClient.INSTANCE.eventManager.register(this);
        onEnable();
    }
    
    public void disable() {
        enabled = false;
        MyClient.INSTANCE.eventManager.unregister(this);
        onDisable();
    }
    
    public void onEnable() {}
    public void onDisable() {}
    
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    
    public enum Category {
        COMBAT,
        MOVEMENT,
        PLAYER,
        RENDER,
        MISC
    }
}