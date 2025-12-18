package com.myclient;

import com.myclient.module.ModuleManager;
import com.myclient.event.EventManager;
import net.fabricmc.api.ClientModInitializer;

public class MyClient implements ClientModInitializer {
    
    public static final String NAME = "MagicMBK Client";
    public static final String VERSION = "1.0";
    
    public static MyClient INSTANCE;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    
    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        
        System.out.println("================================");
        System.out.println(NAME + " v" + VERSION + " Loading...");
        System.out.println("================================");
        
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        
        System.out.println("Loaded " + moduleManager.getModules().size() + " modules!");
        System.out.println(NAME + " Loaded Successfully!");
        System.out.println("================================");
    }
}