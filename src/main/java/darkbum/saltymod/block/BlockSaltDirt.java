package darkbum.saltymod.block;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import darkbum.saltymod.init.ModAchievementList;
import darkbum.saltymod.init.ModBlocks;
import darkbum.saltymod.init.ModItems;

public class BlockSaltDirt extends Block {

    @SideOnly(Side.CLIENT)
    private IIcon TOP;

    @SideOnly(Side.CLIENT)
    private IIcon SIDE;

    private long lastMessageTime = -1;
    private static final int COOLDOWN_TICKS = 20;

    public BlockSaltDirt(String name, CreativeTabs tab) {
        super(Material.ground);
        setTickRandomly(true);
        setStepSound(soundTypeGravel);
        setBlockName(name);
        setCreativeTab(tab);
        setHardness(0.5F);
        setResistance(1.0F);
        setHarvestLevel("shovel", 0);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta > 1) meta = 0;
        return (meta == 1 && side == 1) ? this.TOP : ((meta == 1 && side > 1) ? this.SIDE : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1) {
        this.blockIcon = par1.registerIcon("saltymod:salt_dirt");
        this.TOP = par1.registerIcon("saltymod:salt_lake_dirt_top");
        this.SIDE = par1.registerIcon("saltymod:salt_lake_dirt");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(net.minecraft.util.MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        int meta = world.getBlockMetadata(x, y, z);
        return new ItemStack(this, 1, meta);
    }

    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        if (!world.isRemote && world.getBlockMetadata(x, y, z) == 1) {
            if (entity instanceof net.minecraft.entity.EntityLivingBase && EntityList.getEntityString(entity) != null
                && ((EntityList.getEntityString(entity)
                    .toLowerCase()
                    .contains("slime")
                    && !EntityList.getEntityString(entity)
                        .toLowerCase()
                        .contains("lava"))
                    || EntityList.getEntityString(entity)
                        .toLowerCase()
                        .contains("witch")))
                world.scheduleBlockUpdate(x, y, z, this, 0);
            if (entity instanceof EntityPlayer) ((EntityPlayer) entity).addStat(ModAchievementList.nav_salt_lake, 1);
        }
    }

    public void updateTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            if (world.getBlockMetadata(x, y, z) == 1) {
                int d1 = 0;
                double d0 = 0.0625D;
                AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1) + d0, (z + 1));
                List<Entity> list = world.getEntitiesWithinAABB(Entity.class, axisalignedbb);
                Iterator<Entity> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Entity entity = iterator.next();
                    if (entity instanceof net.minecraft.entity.EntityLivingBase
                        && EntityList.getEntityString(entity) != null
                        && ((EntityList.getEntityString(entity)
                            .toLowerCase()
                            .contains("slime")
                            && !EntityList.getEntityString(entity)
                                .toLowerCase()
                                .contains("lava"))
                            || EntityList.getEntityString(entity)
                                .toLowerCase()
                                .contains("witch"))) {
                        entity.attackEntityFrom(DamageSource.cactus, 1.0F);
                        d1 = 3;
                    }
                    if (d1 > 0) {
                        d1--;
                        for (int x1 = x - 1; x1 < x + 2; x1++) {
                            for (int z1 = z - 1; z1 < z + 2; z1++) {
                                if (world.getBlock(x1, y, z1) == ModBlocks.salt_block
                                    || world.getBlock(x1, y, z1) == ModBlocks.salt_lamp
                                    || world.getBlock(x1, y, z1) == ModBlocks.salt_lake
                                    || world.getBlock(x1, y, z1) == ModBlocks.salt_dirt
                                    || world.getBlock(x1, y, z1) == ModBlocks.salt_brick_stairs
                                    || world.getBlock(x1, y, z1) == ModBlocks.salt_slab
                                    || world.getBlock(x1, y, z1) == ModBlocks.double_salt_slab)
                                    world.scheduleBlockUpdate(x1, y, z1, this, 10);
                            }
                        }
                    }
                }
                if (world.getBlock(x, y + 1, z)
                    .getMaterial() == Material.craftedSnow
                    || world.getBlock(x, y + 1, z)
                        .getMaterial() == Material.ice)
                    world.setBlock(x, y + 1, z, Blocks.water);
            }
            if (world.getBlock(x, y + 1, z)
                .getMaterial() == Material.snow) world.setBlockToAir(x, y + 1, z);
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx,
        float hity, float hitz) {
        if (world.isRemote) return true;

        int meta = world.getBlockMetadata(x, y, z);

        long currentTick = world.getTotalWorldTime();

        if (meta == 0) {
            if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem()
                .getItem() == ModItems.salt_pinch) {
                if (currentTick - lastMessageTime < COOLDOWN_TICKS) {
                    return false;
                }
                player.addChatMessage(new ChatComponentText(I18n.format(getUnlocalizedName() + ".mess")));
                lastMessageTime = currentTick;
            }
            return true;
        }
        return false;
    }

    public MapColor getMapColor(int meta) {
        MapColor color = MapColor.dirtColor;
        if (meta == 1) color = MapColor.quartzColor;
        return color;
    }
}
