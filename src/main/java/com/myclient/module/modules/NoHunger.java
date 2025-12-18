package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

public class NoHunger extends Module {
    
    public NoHunger() {
        super("NoHunger", "Reduce hunger loss", GLFW.GLFW_KEY_M, Category.PLAYER);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null) return;
        
        // Send onGround false to reduce hunger
        if (mc.player.isOnGround() && mc.player.age % 10 == 0) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
        }
    }
}