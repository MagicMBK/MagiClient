package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import org.lwjgl.glfw.GLFW;

public class Fullbright extends Module {
    
    public Fullbright() {
        super("Fullbright", "See in the dark", GLFW.GLFW_KEY_F, Category.RENDER);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) return;
        mc.options.getGamma().setValue(15.0);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.options.getGamma().setValue(1.0);
        }
    }
}