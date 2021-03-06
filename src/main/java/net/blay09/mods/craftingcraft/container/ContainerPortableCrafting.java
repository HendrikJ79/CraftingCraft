package net.blay09.mods.craftingcraft.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPortableCrafting extends Container {

    private final EntityPlayer entityPlayer;
    private final InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    private final IInventory craftResult = new InventoryCraftResult();

    public ContainerPortableCrafting(EntityPlayer entityPlayer) {
        this.entityPlayer = entityPlayer;
        addSlotToContainer(new SlotCrafting(entityPlayer, craftMatrix, craftResult, 0, 124, 35));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new Slot(craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(entityPlayer.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(entityPlayer.inventory, i, 8 + i * 18, 142));
        }
        onCraftMatrixChanged(craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, entityPlayer.worldObj));
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = craftMatrix.removeStackFromSlot(i);
            if (itemStack != null) {
                if (!entityPlayer.inventory.addItemStackToInventory(itemStack) && !entityPlayer.worldObj.isRemote) {
                    entityPlayer.dropItem(itemStack, false);
                }
            }
        }
        entityPlayer.inventoryContainer.detectAndSendChanges();
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int i) {
        ItemStack itemStack = null;
        Slot slot = this.inventorySlots.get(i);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            //noinspection ConstantConditions
            itemStack = slotStack.copy();

            if (i == 0) {
                if (!this.mergeItemStack(slotStack, 10, 46, true)) {
                    return null;
                }

                slot.onSlotChange(slotStack, itemStack);
            } else if (i >= 10 && i < 37) {
                if (!this.mergeItemStack(slotStack, 37, 46, false)) {
                    return null;
                }
            } else if (i >= 37 && i < 46) {
                if (!this.mergeItemStack(slotStack, 10, 37, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(slotStack, 10, 46, false)) {
                return null;
            }

            if (slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.stackSize == itemStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(entityPlayer, slotStack);
        }

        return itemStack;
    }

    @Override
    public boolean canMergeSlot(ItemStack itemStack, Slot slot) {
        return slot.inventory != craftResult && super.canMergeSlot(itemStack, slot);
    }

}
