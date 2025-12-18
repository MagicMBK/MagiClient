package com.myclient.module;

import com.myclient.gui.ClickGUI;
import com.myclient.module.modules.*;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    
    private List<Module> modules = new ArrayList<>();
    private ClickGUI clickGUI;
    
    public ModuleManager() {
        clickGUI = new ClickGUI();
        
        // COMBAT
        modules.add(new KillAura());
        
        // MOVEMENT
        modules.add(new Flight());
        modules.add(new Speed());
        modules.add(new AutoSprint());
        
        // PLAYER
        modules.add(new NoFall());
        modules.add(new Scaffold());
        modules.add(new AutoRespawn());
        modules.add(new NoHunger());
        
        // RENDER
        modules.add(new Fullbright());
    }
    
    public void onKeyPress(int key) {
        // RIGHT SHIFT = Open GUI
        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            openGUI();
            return;
        }
        
        // Module keybinds
        for (Module module : modules) {
            if (module.getKey() == key) {
                module.toggle();
                System.out.println("[MagicMBK] " + module.getName() + " " + (module.isEnabled() ? "enabled" : "disabled"));
            }
        }
    }
    
    public void openGUI() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc != null) {
            mc.setScreen(clickGUI);
        }
    }
    
    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
    
    public List<Module> getModules() {
        return modules;
    }
    
    public List<Module> getModulesByCategory(Module.Category category) {
        List<Module> result = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }
    
    public ClickGUI getClickGUI() {
        return clickGUI;
    }
}