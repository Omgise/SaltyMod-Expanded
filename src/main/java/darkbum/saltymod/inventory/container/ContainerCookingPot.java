package darkbum.saltymod.inventory.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import darkbum.saltymod.inventory.slot.SlotCookingPotBowl;
import darkbum.saltymod.inventory.slot.SlotCookingPotOutputLocked;
import darkbum.saltymod.inventory.slot.SlotCookingPotPinch;
import darkbum.saltymod.inventory.slot.SlotMachineIngred;
import darkbum.saltymod.util.MachineUtilRegistry;
import darkbum.saltymod.tileentity.TileEntityCookingPot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerCookingPot extends Container {

    private final TileEntityCookingPot tileEntityCookingPot;

    private static final int SLOT_INGRED_1 = 0;
    private static final int SLOT_INGRED_2 = 1;
    private static final int SLOT_INGRED_3 = 2;
    private static final int SLOT_INGRED_4 = 3;
    private static final int SLOT_INGRED_5 = 4;
    private static final int SLOT_INGRED_6 = 5;
    public static final int SLOT_OUTPUT = 6;
    private static final int SLOT_PINCH = 7;
    private static final int SLOT_BOWL = 8;
    private static final int SLOT_COUNT_MACHINE = 9;

    private static final int SLOT_PLAYER_INV_START = SLOT_COUNT_MACHINE;
    private static final int SLOT_HOTBAR_START = SLOT_PLAYER_INV_START + 27;
    private static final int SLOT_TOTAL = SLOT_HOTBAR_START + 9;

    public ContainerCookingPot(InventoryPlayer playerInventory, TileEntityCookingPot TileEntityCookingPot) {
        tileEntityCookingPot = TileEntityCookingPot;

        addSlotToContainer(new SlotMachineIngred(playerInventory.player, TileEntityCookingPot, SLOT_INGRED_1, 30, 17));
        addSlotToContainer(new SlotMachineIngred(playerInventory.player, TileEntityCookingPot, SLOT_INGRED_2, 48, 17));
        addSlotToContainer(new SlotMachineIngred(playerInventory.player, TileEntityCookingPot, SLOT_INGRED_3, 66, 17));
        addSlotToContainer(new SlotMachineIngred(playerInventory.player, TileEntityCookingPot, SLOT_INGRED_4, 30, 35));
        addSlotToContainer(new SlotMachineIngred(playerInventory.player, TileEntityCookingPot, SLOT_INGRED_5, 48, 35));
        addSlotToContainer(new SlotMachineIngred(playerInventory.player, TileEntityCookingPot, SLOT_INGRED_6, 66, 35));
        addSlotToContainer(new SlotCookingPotOutputLocked(playerInventory.player, TileEntityCookingPot, SLOT_OUTPUT, 124, 26, TileEntityCookingPot, SLOT_BOWL));
        addSlotToContainer(new SlotCookingPotPinch(TileEntityCookingPot, SLOT_PINCH, 8, 17));
        addSlotToContainer(new SlotCookingPotBowl(TileEntityCookingPot, SLOT_BOWL, 124, 55));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int index = col + row * 9 + 9;
                int x = 8 + col * 18;
                int y = 84 + row * 18;
                addSlotToContainer(new Slot(playerInventory, index, x, y));
            }
        }

        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            addSlotToContainer(new Slot(playerInventory, col, x, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < inventorySlots.size(); ++i) {
            Slot slot = inventorySlots.get(i);
            if (slot.getHasStack()) {
                ItemStack stack = slot.getStack();
                if (!ItemStack.areItemStacksEqual(stack, inventoryItemStacks.get(i))) {
                    inventoryItemStacks.set(i, stack.copy());
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int value) {
        if (id == 0) {
            tileEntityCookingPot.cookingTime = value;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntityCookingPot.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            itemStack = stackInSlot.copy();

            if (slotIndex < SLOT_PLAYER_INV_START) {
                if (!mergeItemStack(stackInSlot, SLOT_PLAYER_INV_START, SLOT_TOTAL, true)) {
                    return null;
                }
            } else {
                boolean isPinchItem = MachineUtilRegistry.isValidPinch(stackInSlot);
                boolean isBowlItem = MachineUtilRegistry.isValidBowl(stackInSlot);
                boolean isSpadeItem = MachineUtilRegistry.isValidSpade(stackInSlot);
                boolean merged = false;

                if (isPinchItem) {
                    merged = mergeItemStack(stackInSlot, SLOT_PINCH, SLOT_PINCH + 1, false);
                } else if (isBowlItem) {
                    merged = mergeItemStack(stackInSlot, SLOT_BOWL, SLOT_BOWL + 1, false);
                } else if (!isSpadeItem) {
                    merged = mergeItemStack(stackInSlot, SLOT_INGRED_1, SLOT_INGRED_6 + 1, false);
                }

                if (!merged) {
                    if (slotIndex < SLOT_HOTBAR_START) {
                        merged = mergeItemStack(stackInSlot, SLOT_HOTBAR_START, SLOT_TOTAL, false);
                    } else if (slotIndex < SLOT_TOTAL) {
                        merged = mergeItemStack(stackInSlot, SLOT_PLAYER_INV_START, SLOT_HOTBAR_START, false);
                    }

                    if (!merged) {
                        return null;
                    }
                }
            }

            if (stackInSlot.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (stackInSlot.stackSize == itemStack.stackSize) {
                return null;
            }

            if (slotIndex == SLOT_OUTPUT && slot instanceof SlotCookingPotOutputLocked) {
                int amountTaken = itemStack.stackSize;
                ((SlotCookingPotOutputLocked) slot).onConsumeKeys(amountTaken);

                slot.onPickupFromSlot(player, stackInSlot);

                return null;
            }

            slot.onPickupFromSlot(player, stackInSlot);
        }

        return itemStack;
    }
}
