package darkbum.saltymod.block;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import darkbum.saltymod.common.config.ModConfigurationBlocks;
import darkbum.saltymod.init.ModBlocks;
import darkbum.saltymod.init.ModItems;

public class BlockSaltworts extends BlockBush implements IGrowable {

    @SideOnly(Side.CLIENT)
    private IIcon STAGE_1;

    @SideOnly(Side.CLIENT)
    private IIcon STAGE_2;

    @SideOnly(Side.CLIENT)
    private IIcon STAGE_3;

    @SideOnly(Side.CLIENT)
    private IIcon STAGE_4;

    @SideOnly(Side.CLIENT)
    private IIcon DEAD;

    public BlockSaltworts(String name, CreativeTabs tab) {
        setBlockName(name);
        setStepSound(soundTypeGrass);
        setCreativeTab(tab);
        setTickRandomly(true);
        setBlockTextureName("saltymod:saltwort");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (meta < 0 || meta > 5) meta = 0;
        return (meta == 1) ? this.STAGE_1
            : ((meta == 2) ? this.STAGE_2
                : ((meta == 3) ? this.STAGE_3
                    : ((meta == 4) ? this.STAGE_4 : ((meta == 5) ? this.DEAD : this.blockIcon))));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1) {
        this.blockIcon = par1.registerIcon(getTextureName() + "_0");
        this.STAGE_1 = par1.registerIcon(getTextureName() + "_1");
        this.STAGE_2 = par1.registerIcon(getTextureName() + "_2");
        this.STAGE_3 = par1.registerIcon(getTextureName() + "_3");
        this.STAGE_4 = par1.registerIcon(getTextureName() + "_4");
        this.DEAD = par1.registerIcon(getTextureName() + "_5");
    }

    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        float g = 0.375F;
        float v = 0.625F;
        if (meta == 1) {
            g = 0.375F;
            v = 0.5F;
        }
        if (meta == 2) {
            g = 0.3125F;
            v = 0.375F;
        }
        if (meta == 3) {
            g = 0.25F;
            v = 0.25F;
        }
        if (meta == 4) {
            g = 0.1875F;
            v = 0.125F;
        }
        if (meta == 5) {
            g = 0.3125F;
            v = 0.5625F;
        }
        setBlockBounds(0.0F + g, 0.0F, 0.0F + g, 1.0F - g, 1.0F - v, 1.0F - g);
    }

    public boolean canBlockStay(World world, int x, int y, int z) {
        Block B = world.getBlock(x, y - 1, z);
        return ((B.getMaterial() == Material.grass || (B.getMaterial() == Material.ground && B != ModBlocks.salt_dirt)
            || B.getMaterial() == Material.sand
            || B.getMaterial() == Material.clay
            || (B == ModBlocks.salt_dirt && world.getBlockMetadata(x, y - 1, z) == 0))
            && World.doesBlockHaveSolidTopSurface((IBlockAccess) world, x, y - 1, z));
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune) {
        ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
        Item seed = ModItems.saltwort;
        Random rand = new Random();
        if (meta <= 1) drop.add(new ItemStack(seed));
        if (meta == 2) {
            int i = rand.nextInt(2) + 1;
            drop.add(new ItemStack(seed, i));
        }
        if (meta == 3) {
            int i = rand.nextInt(3) + 1;
            drop.add(new ItemStack(seed, i));
        }
        if (meta == 4) {
            int i = rand.nextInt(4) + 2;
            drop.add(new ItemStack(seed, i));
        }
        return drop;
    }

    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (!world.isRemote) {
            Block B = world.getBlock(x, y - 1, z);
            int M = world.getBlockMetadata(x, y - 1, z);
            if ((B == ModBlocks.salt_dirt && M == 0) || (B == ModBlocks.salt_dirt_lite && (M == 1 || M == 2))) {
                if (rand.nextInt(ModConfigurationBlocks.saltwortGrowthSpeed) == 0)
                    if (world.getBlockMetadata(x, y, z) == 0) {
                        world.setBlock(x, y, z, (Block) this, 1, 3);
                    } else if (world.getBlockMetadata(x, y, z) == 1 && world.getFullBlockLightValue(x, y, z) >= 12) {
                        world.setBlock(x, y, z, (Block) this, 2, 3);
                        if (B == ModBlocks.salt_dirt) {
                            world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite, 2, 3);
                        } else if (B == ModBlocks.salt_dirt_lite && M == 2) {
                            world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite, 1, 3);
                        } else if (B == ModBlocks.salt_dirt_lite && M == 1) {
                            world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite);
                        }
                    } else if ((world.getBlockMetadata(x, y, z) >= 2 || world.getBlockMetadata(x, y, z) <= 4)
                        && world.getFullBlockLightValue(x, y, z) >= 12) {
                            if (world.getBlockMetadata(x, y, z) == 2 || world.getBlockMetadata(x, y, z) == 3) {
                                if (world.getBlockMetadata(x, y, z) == 2) {
                                    world.setBlock(x, y, z, (Block) this, 3, 3);
                                } else if (world.getBlockMetadata(x, y, z) == 3) {
                                    world.setBlock(x, y, z, (Block) this, 4, 3);
                                }
                                if (B == ModBlocks.salt_dirt) {
                                    world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite, 2, 3);
                                } else if (B == ModBlocks.salt_dirt_lite && M == 2) {
                                    world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite, 1, 3);
                                } else if (B == ModBlocks.salt_dirt_lite && M == 1) {
                                    world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite);
                                }
                            }
                            int S = 0;
                            for (int x1 = x - 2; x1 <= x + 2; x1++) {
                                for (int z1 = z - 2; z1 <= z + 2; z1++) {
                                    if (world.getBlock(x1, y, z1) == ModBlocks.saltworts) S++;
                                }
                            }
                            if (S < 7) for (int x2 = x - 1; x2 <= x + 1; x2++) {
                                for (int z2 = z - 1; z2 <= z + 1; z2++) {
                                    Block B2 = world.getBlock(x2, y - 1, z2);
                                    int M2 = world.getBlockMetadata(x2, y - 1, z2);
                                    if (rand.nextInt(8) == 0 && world.isAirBlock(x2, y, z2)
                                        && ((B2 == ModBlocks.salt_dirt_lite && (M2 == 1 || M2 == 2))
                                            || (B2 == ModBlocks.salt_dirt && M2 == 0))
                                        && ((B == ModBlocks.salt_dirt_lite && (M == 1 || M == 2))
                                            || (B == ModBlocks.salt_dirt && M == 0))) {
                                        world.setBlock(x2, y, z2, ModBlocks.saltworts);
                                        if (B2 == ModBlocks.salt_dirt) {
                                            world.setBlock(x2, y - 1, z2, ModBlocks.salt_dirt_lite, 2, 3);
                                        } else if (B2 == ModBlocks.salt_dirt_lite && M2 == 2) {
                                            world.setBlock(x2, y - 1, z2, ModBlocks.salt_dirt_lite, 1, 3);
                                        } else if (B2 == ModBlocks.salt_dirt_lite && M2 == 1) {
                                            world.setBlock(x2, y - 1, z2, ModBlocks.salt_dirt_lite);
                                        }
                                        if (world.getBlockMetadata(x, y, z) == 4) if (B == ModBlocks.salt_dirt) {
                                            world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite, 2, 3);
                                        } else if (B == ModBlocks.salt_dirt_lite && M == 2) {
                                            world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite, 1, 3);
                                        } else if (B == ModBlocks.salt_dirt_lite && M == 1) {
                                            world.setBlock(x, y - 1, z, ModBlocks.salt_dirt_lite);
                                        }
                                    }
                                }
                            }
                        }
            } else if (rand.nextInt(ModConfigurationBlocks.saltwortGrowthSpeed + 1) == 0) {
                if (world.getBlockMetadata(x, y, z) == 0) {
                    world.setBlock(x, y, z, (Block) this, 1, 3);
                } else if (world.getBlockMetadata(x, y, z) == 1) {
                    world.setBlock(x, y, z, (Block) this, 5, 3);
                }
            }
        }
    }

    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx,
        float hity, float hitz) {
        ItemStack current = player.getCurrentEquippedItem();
        if (!world.isRemote && world.getBlockMetadata(x, y, z) == 4
            && player.getCurrentEquippedItem() != null
            && player.getCurrentEquippedItem()
                .getItem() == Items.shears) {
            world.setBlock(x, y, z, ModBlocks.saltworts, 2, 3);
            Random rand = new Random();
            int i = rand.nextInt(3) + 1;
            int fort = EnchantmentHelper
                .getEnchantmentLevel(Enchantment.fortune.effectId, player.getCurrentEquippedItem());
            if (fort > 0) i = rand.nextInt(5 - fort) + fort;
            ItemStack item = new ItemStack(ModItems.saltwort, i, 0);
            EntityItem entity_item = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, item);
            entity_item.delayBeforeCanPickup = 10;
            world.spawnEntityInWorld((Entity) entity_item);
            world.playSoundEffect(x + 0.5D, y + 1.0D, z + 0.5D, "mob.sheep.shear", 1.0F, 1.2F);
            ItemStack shear = player.getCurrentEquippedItem();
            if (!player.capabilities.isCreativeMode) {
                shear.damageItem(1, (EntityLivingBase) player);
                if (shear.getItemDamage() > shear.getMaxDamage()) shear.stackSize--;
            }
        }
        return false;
    }

    public static boolean fertilize(World world, int x, int y, int z) {
        boolean chek = false;
        for (int x3 = x - 1; x3 <= x + 1; x3++) {
            for (int z3 = z - 1; z3 <= z + 1; z3++) {
                Block B3 = world.getBlock(x3, y - 1, z3);
                int M3 = world.getBlockMetadata(x3, y - 1, z3);
                boolean P = false;
                if (world.getBlock(x3, y, z3) == ModBlocks.saltworts && world.getBlockMetadata(x3, y, z3) < 4)
                    if ((B3 == ModBlocks.salt_dirt && M3 == 0)
                        || (B3 == ModBlocks.salt_dirt_lite && (M3 == 1 || M3 == 2))) {
                            if (world.getBlockMetadata(x3, y, z3) == 0) {
                                world.setBlock(x3, y, z3, ModBlocks.saltworts, 1, 3);
                                chek = true;
                                P = true;
                            } else {
                                if (world.getBlockMetadata(x3, y, z3) == 1) {
                                    world.setBlock(x3, y, z3, ModBlocks.saltworts, 2, 3);
                                    chek = true;
                                    P = true;
                                } else if (world.getBlockMetadata(x3, y, z3) == 2) {
                                    world.setBlock(x3, y, z3, ModBlocks.saltworts, 3, 3);
                                    chek = true;
                                    P = true;
                                } else if (world.getBlockMetadata(x3, y, z3) == 3) {
                                    world.setBlock(x3, y, z3, ModBlocks.saltworts, 4, 3);
                                    chek = true;
                                    P = true;
                                }
                                if (world.getBlockMetadata(x3, y, z3) < 5) if (B3 == ModBlocks.salt_dirt) {
                                    world.setBlock(x3, y - 1, z3, ModBlocks.salt_dirt_lite, 2, 3);
                                } else if (B3 == ModBlocks.salt_dirt_lite && M3 == 2) {
                                    world.setBlock(x3, y - 1, z3, ModBlocks.salt_dirt_lite, 1, 3);
                                } else if (B3 == ModBlocks.salt_dirt_lite && M3 == 1) {
                                    world.setBlock(x3, y - 1, z3, ModBlocks.salt_dirt_lite);
                                }
                            }
                        } else if (world.getBlockMetadata(x3, y, z3) == 0) {
                            world.setBlock(x3, y, z3, ModBlocks.saltworts, 1, 3);
                            chek = true;
                            P = true;
                        }
                if (P) {
                    P = false;
                    if (!world.isRemote) world.playAuxSFX(2005, x3, y, z3, 0);
                }
            }
        }
        if (chek) {
            chek = false;
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        return new ItemStack(ModItems.saltwort);
    }

    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 30;
    }

    protected boolean canSilkHarvest() {
        return false;
    }

    public boolean func_149851_a(World world, int x, int y, int z, boolean isClient) {
        return fertilize(world, x, y, z);
    }

    public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
        return false;
    }

    public void func_149853_b(World world, Random rand, int x, int y, int z) {}
}
