package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class Flight extends Module {
    
    private double speed = 5.0; // Much faster!
    
    public Flight() {
        super("Flight", "Fly like creative mode", GLFW.GLFW_KEY_G, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) return;
        
        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlySpeed((float) (speed / 10.0));
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.getAbilities().flying = false;
            mc.player.getAbilities().setFlySpeed(0.05f);
        }
    }
}