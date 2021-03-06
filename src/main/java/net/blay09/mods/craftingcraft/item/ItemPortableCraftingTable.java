package net.blay09.mods.craftingcraft.item;

import net.blay09.mods.craftingcraft.CraftingCraft;
import net.blay09.mods.craftingcraft.net.GuiHandler;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemPortableCraftingTable extends Item {

    public static final String name = "portableCraftingTable";

    public ItemPortableCraftingTable() {
        setRegistryName(CraftingCraft.MOD_ID, name);
        setUnlocalizedName(getRegistryName().toString());
        setHasSubtypes(true);
        setMaxStackSize(1);
        setCreativeTab(CraftingCraft.creativeTab);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTab, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer, EnumHand hand) {
        openPortableCrafting(entityPlayer, itemStack);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        openPortableCrafting(entityPlayer, itemStack);
        return EnumActionResult.SUCCESS;
    }

    public void openPortableCrafting(EntityPlayer entityPlayer, ItemStack itemStack) {
        if(!entityPlayer.worldObj.isRemote) {
            if (itemStack.getItemDamage() == 1) {
                entityPlayer.openGui(CraftingCraft.instance, GuiHandler.GUI_INVENTORY_CRAFTING, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
            } else {
                entityPlayer.openGui(CraftingCraft.instance, GuiHandler.GUI_PORTABLE_CRAFTING, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemModelMesher mesher) {
        ModelBakery.registerItemVariants(this, new ResourceLocation(CraftingCraft.MOD_ID, "portableCraftingTable"), new ResourceLocation(CraftingCraft.MOD_ID, "inventoryCraftingTable"));

        mesher.register(this, 0, new ModelResourceLocation(CraftingCraft.MOD_ID + ":portableCraftingTable", "inventory"));
        mesher.register(this, 1, new ModelResourceLocation(CraftingCraft.MOD_ID + ":inventoryCraftingTable", "inventory"));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        switch(itemStack.getItemDamage()) {
            case 0: return "item.craftingcraft:portableCraftingTable";
            case 1: return "item.craftingcraft:inventoryCraftingTable";
        }
        return super.getUnlocalizedName(itemStack);
    }

}
