package com.myclient.gui;

import com.myclient.MyClient;
import com.myclient.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClickGUI extends Screen {
    
    private int categoryWidth = 100;
    private int moduleHeight = 18;
    private int categoryHeaderHeight = 22;
    private int padding = 5;
    
    private Map<Module.Category, int[]> categoryPositions = new HashMap<>();
    private Map<Module.Category, Boolean> categoryExpanded = new HashMap<>();
    
    private Module.Category draggingCategory = null;
    private int dragOffsetX, dragOffsetY;
    
    private Module bindingModule = null;
    
    private long openTime = 0;
    
    // Category colors
    private final int[] categoryColors = {
        0xFFE53935, // COMBAT - Red
        0xFF43A047, // MOVEMENT - Green
        0xFF1E88E5, // PLAYER - Blue
        0xFF8E24AA, // RENDER - Purple
        0xFFFFB300  // MISC - Orange
    };
    
    public ClickGUI() {
        super(Text.literal("MagicMBK Client"));
        
        // Initialize category positions
        int startX = 10;
        int startY = 30;
        int gap = 110;
        
        int i = 0;
        for (Module.Category category : Module.Category.values()) {
            categoryPositions.put(category, new int[]{startX + (i * gap), startY});
            categoryExpanded.put(category, true);
            i++;
        }
    }
    
    @Override
    protected void init() {
        super.init();
        openTime = System.currentTimeMillis();
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        // Dark overlay
        context.fill(0, 0, this.width, this.height, 0x88000000);
        
        // Title
        context.drawCenteredTextWithShadow(this.textRenderer, "MagicMBK Client v1.0", this.width / 2, 10, 0xFF9B30FF);
        
        // Handle dragging
        if (draggingCategory != null) {
            int[] pos = categoryPositions.get(draggingCategory);
            pos[0] = mouseX - dragOffsetX;
            pos[1] = mouseY - dragOffsetY;
        }
        
        // Render each category
        int colorIndex = 0;
        for (Module.Category category : Module.Category.values()) {
            renderCategory(context, category, mouseX, mouseY, categoryColors[colorIndex % categoryColors.length]);
            colorIndex++;
        }
        
        // Instructions
        context.drawCenteredTextWithShadow(this.textRenderer, "LEFT=Toggle | MIDDLE=Bind | Drag Header to Move | ESC=Close", this.width / 2, this.height - 15, 0xFF888888);
    }
    
    private void renderCategory(DrawContext context, Module.Category category, int mouseX, int mouseY, int color) {
        int[] pos = categoryPositions.get(category);
        int x = pos[0];
        int y = pos[1];
        boolean expanded = categoryExpanded.get(category);
        
        List<Module> modules = MyClient.INSTANCE.moduleManager.getModulesByCategory(category);
        if (modules.isEmpty()) return;
        
        int totalHeight = categoryHeaderHeight;
        if (expanded) {
            totalHeight += modules.size() * moduleHeight;
        }
        
        // Shadow
        context.fill(x + 2, y + 2, x + categoryWidth + 2, y + totalHeight + 2, 0x55000000);
        
        // Background
        context.fill(x, y, x + categoryWidth, y + totalHeight, 0xEE111111);
        
        // Header
        context.fill(x, y, x + categoryWidth, y + categoryHeaderHeight, color);
        
        // Category name
        String categoryName = category.name();
        context.drawCenteredTextWithShadow(this.textRenderer, categoryName, x + categoryWidth / 2, y + 7, 0xFFFFFFFF);
        
        // Expand/collapse arrow
        String arrow = expanded ? "▼" : "▶";
        context.drawTextWithShadow(this.textRenderer, arrow, x + categoryWidth - 12, y + 7, 0xFFFFFFFF);
        
        // Render modules if expanded
        if (expanded) {
            int moduleY = y + categoryHeaderHeight;
            for (Module module : modules) {
                boolean hovered = mouseX >= x && mouseX <= x + categoryWidth && 
                                  mouseY >= moduleY && mouseY <= moduleY + moduleHeight;
                
                // Module background
                int bgColor;
                if (module.isEnabled()) {
                    bgColor = hovered ? 0xFF2E7D32 : 0xFF1B5E20;
                } else {
                    bgColor = hovered ? 0xFF424242 : 0xFF2A2A2A;
                }
                context.fill(x, moduleY, x + categoryWidth, moduleY + moduleHeight, bgColor);
                
                // Module name
                context.drawTextWithShadow(this.textRenderer, module.getName(), x + 4, moduleY + 5, 0xFFFFFFFF);
                
                // Keybind
                String keyText = bindingModule == module ? "..." : getKeyName(module.getKey());
                int keyWidth = this.textRenderer.getWidth(keyText);
                context.drawTextWithShadow(this.textRenderer, keyText, x + categoryWidth - keyWidth - 4, moduleY + 5, 0xFF888888);
                
                // Bottom border
                context.fill(x, moduleY + moduleHeight - 1, x + categoryWidth, moduleY + moduleHeight, 0xFF333333);
                
                moduleY += moduleHeight;
            }
        }
        
        // Border
        context.fill(x - 1, y - 1, x + categoryWidth + 1, y, color);
        context.fill(x - 1, y + totalHeight, x + categoryWidth + 1, y + totalHeight + 1, color);
        context.fill(x - 1, y, x, y + totalHeight, color);
        context.fill(x + categoryWidth, y, x + categoryWidth + 1, y + totalHeight, color);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mX = (int) mouseX;
        int mY = (int) mouseY;
        
        for (Module.Category category : Module.Category.values()) {
            int[] pos = categoryPositions.get(category);
            int x = pos[0];
            int y = pos[1];
            boolean expanded = categoryExpanded.get(category);
            
            List<Module> modules = MyClient.INSTANCE.moduleManager.getModulesByCategory(category);
            if (modules.isEmpty()) continue;
            
            // Header click
            if (mX >= x && mX <= x + categoryWidth && mY >= y && mY <= y + categoryHeaderHeight) {
                if (button == 0) {
                    // Left click on header - start dragging
                    draggingCategory = category;
                    dragOffsetX = mX - x;
                    dragOffsetY = mY - y;
                } else if (button == 1) {
                    // Right click on header - toggle expand
                    categoryExpanded.put(category, !expanded);
                }
                return true;
            }
            
            // Module clicks (only if expanded)
            if (expanded) {
                int moduleY = y + categoryHeaderHeight;
                for (Module module : modules) {
                    if (mX >= x && mX <= x + categoryWidth && mY >= moduleY && mY <= moduleY + moduleHeight) {
                        if (button == 0) {
                            // Left click - toggle module
                            module.toggle();
                        } else if (button == 2) {
                            // Middle click - bind key
                            bindingModule = module;
                        }
                        return true;
                    }
                    moduleY += moduleHeight;
                }
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingCategory = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // Prevent instant close
        if (System.currentTimeMillis() - openTime < 200) {
            return true;
        }
        
        // If binding a key
        if (bindingModule != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                bindingModule.setKey(0);
            } else {
                bindingModule.setKey(keyCode);
            }
            bindingModule = null;
            return true;
        }
        
        // Close on ESC or RIGHT SHIFT
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            this.close();
            return true;
        }
        
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    private String getKeyName(int key) {
        if (key == 0) return "-";
        
        String name = GLFW.glfwGetKeyName(key, 0);
        if (name != null) return name.toUpperCase();
        
        return switch (key) {
            case GLFW.GLFW_KEY_RIGHT_SHIFT -> "RSHIFT";
            case GLFW.GLFW_KEY_LEFT_SHIFT -> "LSHIFT";
            case GLFW.GLFW_KEY_LEFT_CONTROL -> "LCTRL";
            case GLFW.GLFW_KEY_RIGHT_CONTROL -> "RCTRL";
            case GLFW.GLFW_KEY_SPACE -> "SPACE";
            case GLFW.GLFW_KEY_LEFT_ALT -> "LALT";
            case GLFW.GLFW_KEY_RIGHT_ALT -> "RALT";
            default -> "K" + key;
        };
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}