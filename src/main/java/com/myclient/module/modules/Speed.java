package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Speed extends Module {
    
    private double speedMultiplier = 2.0;
    
    public Speed() {
        super("Speed", "Move faster", GLFW.GLFW_KEY_H, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) return;
        
        // Only boost horizontal movement when on ground
        if (mc.player.isOnGround() && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0)) {
            Vec3d velocity = mc.player.getVelocity();
            mc.player.setVelocity(velocity.x * speedMultiplier, velocity.y, velocity.z * speedMultiplier);
        }
    }
}