package com.myclient.mixin;

import com.myclient.MyClient;
import com.myclient.event.events.PacketEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    
    // Aggiunto "cancellable = true" per poter bloccare i pacchetti
    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if (MyClient.INSTANCE != null && MyClient.INSTANCE.eventManager != null) {
            PacketEvent event = new PacketEvent(packet);
            MyClient.INSTANCE.eventManager.call(event);
            
            // Se il modulo dice di bloccare, fermiamo l'invio
            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }
}