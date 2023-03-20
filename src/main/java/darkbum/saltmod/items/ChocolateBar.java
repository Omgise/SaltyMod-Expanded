package darkbum.saltmod.items;

import java.util.List;
import java.util.Random;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ChocolateBar extends ItemSaltFood {
    public ChocolateBar(String name, CreativeTabs tab, String textureName) {
        super("chocolateBar", 3, 0.2F, new PotionEffect(Potion.digSpeed.id, 600, 1));
        setUnlocalizedName(name);
        setCreativeTab(tab);
        setTextureName("saltmod:" + textureName);
        setAlwaysEdible();
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean flag) {
        list.add(I18n.format(getUnlocalizedName() + ".tooltip"));
    }

    public void onFoodEaten(ItemStack item, World world, EntityPlayer player) {
        boolean check = false;
        if (player.getFoodStats().getFoodLevel() == 20)
            check = true;
        if (!world.isRemote && check) {
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300));
        }
        if (world.isRemote && player.getFoodStats().getFoodLevel() == 20) {
            Random rand = new Random();
            player.addChatMessage(new ChatComponentText(I18n.format(getUnlocalizedName() + ".mess." + rand.nextInt(4))));
        }
    }
}
