package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class KillAura extends Module {
    
    private double range = 4.0;
    
    public KillAura() {
        super("KillAura", "Automatically attacks entities", GLFW.GLFW_KEY_R, Category.COMBAT);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null || mc.world == null) return;
        
        // Find closest entity
        LivingEntity target = null;
        double closestDistance = range;
        
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof LivingEntity living) {
                if (entity == mc.player) continue;
                if (!living.isAlive()) continue;
                
                // Only attack players and monsters
                if (!(entity instanceof PlayerEntity) && !(entity instanceof Monster)) continue;
                
                double distance = mc.player.distanceTo(entity);
                
                if (distance < closestDistance) {
                    closestDistance = distance;
                    target = living;
                }
            }
        }
        
        // Attack target
        if (target != null && mc.player.getAttackCooldownProgress(0.5f) >= 1.0f) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }
}