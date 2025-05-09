package darkbum.saltymod.common.proxy;

import darkbum.saltymod.block.render.*;
import darkbum.saltymod.common.config.ModConfigurationBlocks;
import darkbum.saltymod.common.config.ModConfigurationWorldGeneration;
import net.minecraft.client.renderer.entity.RenderSnowball;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import darkbum.saltymod.entity.EntityHornedSheep;
import darkbum.saltymod.entity.EntityRainmaker;
import darkbum.saltymod.entity.EntityRainmakerDust;
import darkbum.saltymod.entity.model.ModelHornedSheep1;
import darkbum.saltymod.entity.model.ModelHornedSheep2;
import darkbum.saltymod.entity.render.RenderHornedSheep;
import darkbum.saltymod.entity.render.RenderRainmakerDust;
import darkbum.saltymod.init.ModItems;
import net.minecraft.util.IIcon;

/**
 * Client-side proxy class responsible for client-only initializations like custom renderers for blocks and entities.
 * This class extends behavior defined in {@link CommonProxy}.
 *
 * @author DarkBum
 * @since 1.9.f
 */
public class ClientProxy extends CommonProxy {

    /** Custom icon for milk. */
    @SideOnly(Side.CLIENT)
    public static IIcon MILK;

    /** Custom render IDs for special block renderers. */
    public static int saltGrassRenderType;
    public static int evaporatorRenderType;
    public static int cookingPotRenderType;
    public static int marshReedsRenderType;
    public static int marshGrassRenderType;
    public static int marshReedsNewRenderType;

    /**
     * Helper method that overrides from CommonProxy and makes sure the renderer methods are only called client-side.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void setRenderers() {
        setBlockRenderers();
        setEntityRenderers();
    }

    /**
     * Registers custom block renderers using unique render IDs.
     * Called during client-side initialization.
     */
    public void setBlockRenderers() {
        if (ModConfigurationBlocks.enableSaltDirt) {
            saltGrassRenderType = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new SaltGrassRenderer());
        }

        if (ModConfigurationBlocks.enableEvaporator) {
            evaporatorRenderType = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new EvaporatorRenderer());
        }

        if (ModConfigurationBlocks.enableMachines) {
            cookingPotRenderType = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new CookingPotRenderer());
        }

        if (ModConfigurationWorldGeneration.enableSaltMarsh) {
            marshReedsNewRenderType = RenderingRegistry.getNextAvailableRenderId();
            RenderingRegistry.registerBlockHandler(new MarshReedsRenderer());
        }
    }

    /**
     * Registers custom entity renderers.
     * This method must only be called on the client side.
     */
    public void setEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityRainmaker.class,
            new RenderSnowball(ModItems.rainmaker));
        RenderingRegistry.registerEntityRenderingHandler(EntityRainmakerDust.class,
            new RenderRainmakerDust());
        RenderingRegistry.registerEntityRenderingHandler(EntityHornedSheep.class,
            new RenderHornedSheep(new ModelHornedSheep2(), new ModelHornedSheep1(), 0.7F));
    }
}
