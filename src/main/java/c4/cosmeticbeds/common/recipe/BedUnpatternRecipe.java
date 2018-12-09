package c4.cosmeticbeds.common.recipe;

import c4.cosmeticbeds.CosmeticBeds;
import c4.cosmeticbeds.init.Registry;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class BedUnpatternRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public BedUnpatternRecipe() {
        this.setRegistryName(CosmeticBeds.MODID, "bed_unpattern_recipe");
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty()) {

                if (!itemstack.isEmpty() || itemstack1.getItem() != Registry.COSMETIC_BED_ITEM) {
                    return false;
                } else {
                    itemstack = itemstack1.copy();
                }
            }
        }
        return !itemstack.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty() && itemstack1.getItem() == Registry.COSMETIC_BED_ITEM) {
                itemstack = itemstack1.copy();
            }
        }

        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            NBTTagCompound nbttagcompound = itemstack.getSubCompound("BlockEntityTag");

            if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);

                if (!nbttaglist.isEmpty()) {
                    return ItemBanner.makeBanner(EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base")), nbttaglist);
                }
            }
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);

            if (itemstack.getItem().hasContainerItem(itemstack)) {
                nonnulllist.set(i, itemstack.getItem().getContainerItem(itemstack));
            }
        }
        return nonnulllist;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }
}
