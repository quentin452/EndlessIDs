package com.falsepattern.endlessids.mixin.mixins.common.biome.randomthings;

import com.falsepattern.endlessids.mixin.helpers.ChunkBiomeHook;
import lumien.randomthings.Items.ItemBiomeCapsule;
import lumien.randomthings.Items.ItemBiomePainter;
import lumien.randomthings.Items.ModItems;
import lumien.randomthings.Network.Messages.MessagePaintBiome;
import lumien.randomthings.Network.PacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

@Mixin(ItemBiomePainter.class)
public abstract class ItemBiomePainterMixin {
    /**
     * @author FalsePattern
     * @reason getBiomeArray fix
     */
    @Overwrite
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (!par2EntityPlayer.inventory.hasItem(ModItems.biomeCapsule)) {
            return false;
        } else if (par3World.isRemote) {
            return true;
        } else {
            for (int i = 0; i < par2EntityPlayer.inventory.getSizeInventory(); ++i) {
                ItemStack is = par2EntityPlayer.inventory.getStackInSlot(i);
                if (is != null && is.getItem() instanceof ItemBiomeCapsule && is.getItemDamage() != 0) {
                    NBTTagCompound nbt = is.stackTagCompound;
                    if (nbt == null) {
                        nbt = is.stackTagCompound = new NBTTagCompound();
                    }

                    int charges = nbt.getInteger("charges");
                    if (charges > 0 || par2EntityPlayer.capabilities.isCreativeMode) {
                        Chunk c = par3World.getChunkFromBlockCoords(par4, par6);
                        int biomeID = is.getItemDamage() - 1;
                        short[] biomeArray = ((ChunkBiomeHook) c).getBiomeShortArray(); //changed
                        if ((biomeArray[(par6 & 15) << 4 | par4 & 15] & 255) != biomeID) {
                            biomeArray[(par6 & 15) << 4 | par4 & 15] = (short) (biomeID & 0xFFFF); //changed
                            ((ChunkBiomeHook) c).setBiomeShortArray(biomeArray); //changed
                            if (!par2EntityPlayer.capabilities.isCreativeMode) {
                                nbt.setInteger("charges", charges - 1);
                                par2EntityPlayer.inventoryContainer.detectAndSendChanges();
                            }

                            PacketHandler.INSTANCE.sendToDimension(
                                    new MessagePaintBiome(par4, par5, par6, par3World.provider.dimensionId, biomeID),
                                    par3World.provider.dimensionId);
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }
}
