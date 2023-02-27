package ru.liahim.saltmod.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.liahim.saltmod.init.ModBlocks;
import ru.liahim.saltmod.init.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import static ru.liahim.saltmod.init.SaltConfig.*;

public class MudBrickWet extends Block implements IDegradable {

	@SideOnly(Side.CLIENT)
    private IIcon MAIN0;

	@SideOnly(Side.CLIENT)
    private IIcon MAIN1;

    @SideOnly(Side.CLIENT)
    private IIcon MAIN2;

	public MudBrickWet(String name, CreativeTabs tab) {
		super (Material.clay);
		setStepSound(ModSounds.soundTypeWetMudBrick);
		setBlockName(name);
		setCreativeTab(tab);
		setHardness(1.0F);
		setResistance(3.0F);
		setHarvestLevel("shovel", 0);
		if(mudBrickComplex) {
		setTickRandomly(true);
		} else {
		setTickRandomly(false);
		}
	}

	@SideOnly(Side.CLIENT)
		public IIcon getIcon(int side, int meta) {
			return (meta == 1) ? this.MAIN1 : ((meta == 2) ? this.MAIN2 : this.MAIN0);
	}

	@SideOnly(Side.CLIENT)
		public void registerBlockIcons(IIconRegister par1) {
			this.MAIN0 = par1.registerIcon("saltMod:MudBrickWet_0");
			this.MAIN1 = par1.registerIcon("saltMod:MudBrickWet_1");
			this.MAIN2 = par1.registerIcon("saltMod:MudBrickWet_2");
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		tickDegradation(world, x, y, z, rand);
	}

	@Override
	public int getMudBrickWetMeta(int paramInt) {
        return paramInt;
	}

	@Override
	public Block getMudBrickWetFromMeta(int paramInt) {
        return this;
	}
}
