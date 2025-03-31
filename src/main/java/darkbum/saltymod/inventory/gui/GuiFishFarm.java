package darkbum.saltymod.inventory.gui;

import darkbum.saltymod.inventory.container.ContainerFishFarm;
import darkbum.saltymod.tileentity.TileEntityFishFarm;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class GuiFishFarm extends GuiContainer {

    private static final ResourceLocation guiTextures = new ResourceLocation("saltymod:textures/gui/container/fish_farm.png");

    public GuiFishFarm(InventoryPlayer player, TileEntityFishFarm tileEntityFishFarm) {
        super(new ContainerFishFarm(player, tileEntityFishFarm));
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString("Fish Farm", 8, 8, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 4, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
    }
}
