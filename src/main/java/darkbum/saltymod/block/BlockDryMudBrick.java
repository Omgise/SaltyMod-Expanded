package darkbum.saltymod.block;

import darkbum.saltymod.util.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

import static darkbum.saltymod.util.BlockUtils.*;

/**
 * Block class for the dry mud brick block.
 * The dry mud brick is a regular block that is created from wet mud brick.
 *
 * @author DarkBum
 * @since 1.9.f
 */
public class BlockDryMudBrick extends Block {

    /**
     * Constructs a new block instance with a given name and a creative tab.
     * <p>
     * Also assigns a material and other base properties through {@link BlockUtils}.
     *
     * @param name The internal name of the block.
     * @param tab  The creative tab in which the block appears.
     */
    public BlockDryMudBrick(String name, CreativeTabs tab) {
        super(Material.rock);
        setBlockName(name);
        setCreativeTab(tab);
        setBlockTextureName("saltymod:mud_bricks");
        propertiesDryMudBrick(this);
    }
}
