package com.myclient.module.modules;

import com.myclient.event.EventTarget;
import com.myclient.event.events.PacketEvent;
import com.myclient.mixin.PacketAccessor;
import com.myclient.module.Module;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.lwjgl.glfw.GLFW;

public class NoHunger extends Module {
    
    public NoHunger() {
        super("NoHunger", "Reduces hunger", GLFW.GLFW_KEY_M, Category.PLAYER);
    }
    
    @EventTarget
    public void onPacketSend(PacketEvent event) {
        if (mc.player == null) return;

        // 1. Blocca l'invio del pacchetto "Inizio a correre"
        if (event.getPacket() instanceof ClientCommandC2SPacket) {
            ClientCommandC2SPacket packet = (ClientCommandC2SPacket) event.getPacket();
            if (packet.getMode() == ClientCommandC2SPacket.Mode.START_SPRINTING) {
                event.setCancelled(true);
            }
        }

        // 2. Modifica i pacchetti di movimento per sembrare che "galleggi"
        // (Funziona solo se NON stiamo cadendo, per non rompere NoFall)
        if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket) event.getPacket();
            
            // Se siamo a terra e non stiamo cadendo, diciamo al server che siamo sollevati
            // Questo riduce drasticamente il consumo di cibo
            if (mc.player.isOnGround() && mc.player.fallDistance <= 0.0) {
                ((PacketAccessor) packet).setOnGround(false);
            }
        }
    }
}