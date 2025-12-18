package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.UpdateEvent;
import com.myclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class Scaffold extends Module {
    
    public Scaffold() {
        super("Scaffold", "Auto-bridge", GLFW.GLFW_KEY_B, Category.PLAYER);
    }
    
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.player == null || mc.world == null) return;
        
        // Find block slot in hotbar
        int blockSlot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem) {
                blockSlot = i;
                break;
            }
        }
        
        if (blockSlot == -1) return;
        
        // Get position below player
        BlockPos below = mc.player.getBlockPos().down();
        
        // Check if air
        if (mc.world.getBlockState(below).isAir()) {
            // Switch to block
            int oldSlot = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = blockSlot;
            
            // Place block
            BlockHitResult hitResult = new BlockHitResult(
                new Vec3d(below.getX() + 0.5, below.getY() + 1, below.getZ() + 0.5),
                Direction.UP,
                below,
                false
            );
            
            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hitResult);
            mc.player.swingHand(Hand.MAIN_HAND);
            
            // Switch back
            mc.player.getInventory().selectedSlot = oldSlot;
        }
    }
}