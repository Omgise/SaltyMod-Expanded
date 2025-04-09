package darkbum.saltymod.entity.render;

import darkbum.saltymod.configuration.configs.ModConfigurationEntities;
import darkbum.saltymod.entity.EntityHornedSheep;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderHornedSheep extends RenderLiving {

        private static final ResourceLocation hornedSheepTextures = new ResourceLocation("saltymod:textures/entity/horned_sheep/horned_sheep_fur.png");
        private static final ResourceLocation shearedHornedSheepTextures = new ResourceLocation("saltymod:textures/entity/horned_sheep/horned_sheep.png");
        private static final ResourceLocation shearedHornedSheepThinTextures = new ResourceLocation("saltymod:textures/entity/horned_sheep/thin/horned_sheep_thin.png");

    public RenderHornedSheep(ModelBase modelBase1, ModelBase modelBase2, float par3) {
        super(modelBase1, par3);
        this.setRenderPassModel(modelBase2);
    }

    protected int shouldRenderPass(EntityHornedSheep entityHornedSheep, int renderPass, float partialTicks) {
        if (renderPass == 0 && !entityHornedSheep.getSheared()) {
            this.bindTexture(hornedSheepTextures);

            if (entityHornedSheep.hasCustomNameTag() && "jeb_".equals(entityHornedSheep.getCustomNameTag())) {
                boolean flag = true;
                int k = entityHornedSheep.ticksExisted / 25 + entityHornedSheep.getEntityId();
                int l = k % EntityHornedSheep.fleeceColorTable.length;
                int i1 = (k + 1) % EntityHornedSheep.fleeceColorTable.length;
                float f1 = ((float)(entityHornedSheep.ticksExisted % 25) + partialTicks) / 25.0F;
                GL11.glColor3f(EntityHornedSheep.fleeceColorTable[l][0] * (1.0F - f1) + EntityHornedSheep.fleeceColorTable[i1][0] * f1, EntityHornedSheep.fleeceColorTable[l][1] * (1.0F - f1) + EntityHornedSheep.fleeceColorTable[i1][1] * f1, EntityHornedSheep.fleeceColorTable[l][2] * (1.0F - f1) + EntityHornedSheep.fleeceColorTable[i1][2] * f1);
            } else {
                int j = entityHornedSheep.getFleeceColor();
                GL11.glColor3f(EntityHornedSheep.fleeceColorTable[j][0], EntityHornedSheep.fleeceColorTable[j][1], EntityHornedSheep.fleeceColorTable[j][2]);
            }
            return 1;
        } else {
            return -1;
        }
    }

    protected ResourceLocation getEntityTexture(EntityHornedSheep entity) {
        if (ModConfigurationEntities.hornedSheepThinHorns) {
            return shearedHornedSheepThinTextures;
        }
        return shearedHornedSheepTextures;
    }

    protected int shouldRenderPass(EntityLivingBase entityLivingBase, int renderPass, float partialTicks) {
        return this.shouldRenderPass((EntityHornedSheep) entityLivingBase, renderPass, partialTicks);
    }

    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((EntityHornedSheep)entity);
    }
}
