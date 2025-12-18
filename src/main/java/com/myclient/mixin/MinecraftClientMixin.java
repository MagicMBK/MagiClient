package com.myclient.mixin;

import com.myclient.MyClient;
import com.myclient.event.events.UpdateEvent;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (MyClient.INSTANCE != null && MyClient.INSTANCE.eventManager != null) {
            MyClient.INSTANCE.eventManager.call(new UpdateEvent());
        }
    }
}