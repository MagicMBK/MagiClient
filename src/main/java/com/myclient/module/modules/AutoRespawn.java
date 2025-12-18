package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class AutoRespawn extends Module {
    
    public AutoRespawn() {
        super("AutoRespawn", "Respawn automatically", GLFW.GLFW_KEY_N, Category.PLAYER);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) return;
        
        if (mc.player.isDead()) {
            mc.player.requestRespawn();
        }
    }
}