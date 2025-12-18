package com.myclient.mixin;

import com.myclient.MyClient;
import com.myclient.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    
    @Shadow @Final private MinecraftClient client;
    
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (MyClient.INSTANCE == null) return;
        if (MyClient.INSTANCE.moduleManager == null) return;
        if (client.getDebugHud().shouldShowDebugHud()) return;
        
        TextRenderer textRenderer = client.textRenderer;
        
        // Draw client name
        context.drawTextWithShadow(textRenderer, MyClient.NAME + " v" + MyClient.VERSION, 5, 5, 0xFF9B30FF);
        
        // Draw enabled modules
        List<Module> enabledModules = MyClient.INSTANCE.moduleManager.getModules().stream()
                .filter(Module::isEnabled)
                .collect(Collectors.toList());
        
        int y = 20;
        for (Module module : enabledModules) {
            context.drawTextWithShadow(textRenderer, module.getName(), 5, y, 0xFF00FF00);
            y += 12;
        }
    }
}