package com.myclient.mixin;

import com.myclient.MyClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    
    @Shadow
    private MinecraftClient client;
    
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKeyPress(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        // Only on key press, not release
        if (action != GLFW.GLFW_PRESS) return;
        
        // Skip if invalid key
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;
        
        System.out.println("[MagicMBK] Key event: " + key + " action: " + action);
        
        // Check if client is ready
        if (MyClient.INSTANCE == null) {
            System.out.println("[MagicMBK] Client not ready!");
            return;
        }
        
        if (MyClient.INSTANCE.moduleManager == null) {
            System.out.println("[MagicMBK] ModuleManager not ready!");
            return;
        }
        
        // Allow GUI keybind even when in GUI
        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            if (client.currentScreen == null) {
                System.out.println("[MagicMBK] RIGHT SHIFT pressed, opening GUI");
                MyClient.INSTANCE.moduleManager.onKeyPress(key);
            }
            return;
        }
        
        // Only process other keys when not in a screen
        if (client.currentScreen == null) {
            MyClient.INSTANCE.moduleManager.onKeyPress(key);
        }
    }
}