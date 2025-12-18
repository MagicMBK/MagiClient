package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class AutoSprint extends Module {
    
    public AutoSprint() {
        super("AutoSprint", "Always sprint", GLFW.GLFW_KEY_V, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) return;
        
        if (mc.player.input.movementForward > 0 && !mc.player.isSneaking() && !mc.player.horizontalCollision) {
            mc.player.setSprinting(true);
        }
    }
}