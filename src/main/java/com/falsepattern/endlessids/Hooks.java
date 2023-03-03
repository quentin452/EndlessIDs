package com.falsepattern.endlessids;

import com.falsepattern.endlessids.config.GeneralConfig;
import com.falsepattern.endlessids.constants.ExtendedConstants;
import com.falsepattern.endlessids.mixin.helpers.IChunkMixin;
import com.falsepattern.endlessids.mixin.helpers.IExtendedBlockStorageMixin;
import lombok.val;
import lombok.var;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;

public class Hooks {

    public static int getBlockData(final IExtendedBlockStorageMixin ebs, final byte[] data, int offset) {
        val b1 = ebs.getB1();
        System.arraycopy(b1, 0, data, offset, b1.length);
        offset += b1.length;
        val b2Low = ebs.getB2Low();
        if (b2Low != null) {
            System.arraycopy(b2Low.data, 0, data, offset, b2Low.data.length);
            offset += b2Low.data.length;
            val b2High = ebs.getB2High();
            if (b2High != null) {
                System.arraycopy(b2High.data, 0, data, offset, b2High.data.length);
                offset += b2High.data.length;
                val b3 = ebs.getB3();
                if (b3 != null) {
                    System.arraycopy(b3, 0, data, offset, b3.length);
                }
            }
        }
        return ebs.getEBSMSBMask();
    }

    public static void setBlockData(final IExtendedBlockStorageMixin ebs, final byte[] data, int offset, final int storageFlag) {
        val b1 = ebs.getB1();
        System.arraycopy(data, offset, b1, 0, b1.length);
        offset += b1.length;
        if (storageFlag == 0b00) {
            ebs.clearB2Low();
            ebs.clearB2High();
            ebs.clearB3();
            return;
        }
        var b2Low = ebs.getB2Low();
        if (b2Low == null) {
            b2Low = ebs.createB2Low();
        }
        System.arraycopy(data, offset, b2Low.data, 0, b2Low.data.length);
        offset += b2Low.data.length;
        if (storageFlag == 0b01) {
            ebs.clearB2High();
            ebs.clearB3();
            return;
        }
        var b2High = ebs.getB2High();
        if (b2High == null) {
            b2High = ebs.createB2High();
        }
        System.arraycopy(data, offset, b2High.data, 0, b2High.data.length);
        offset += b2High.data.length;
        if (storageFlag == 0b10) {
            ebs.clearB3();
            return;
        }
        var b3 = ebs.getB3();
        if (b3 == null) {
            b3 = ebs.createB3();
        }
        System.arraycopy(data, offset, b3, 0, b3.length);
    }

    public static void writeBlockDataToNBT(final NBTTagCompound nbt, final IExtendedBlockStorageMixin ebs) {
        val b1 = ebs.getB1();
        val b2Low = ebs.getB2Low();
        val b2High = ebs.getB2High();
        val b3 = ebs.getB3();
        nbt.setByteArray("Blocks", b1);
        if (b2Low != null) {
            nbt.setByteArray("Add", b2Low.data);
        }
        if (b2High != null) {
            nbt.setByteArray("BlocksB2Hi", b2High.data);
        }
        if (b3 != null) {
            nbt.setByteArray("BlocksB3", b3);
        }
    }

    public static void readBlockDataFromNBT(final IExtendedBlockStorageMixin ebs, final NBTTagCompound nbt) {
        assert nbt.hasKey("Blocks");
        val b1 = nbt.getByteArray("Blocks");
        final byte[] b2Low = nbt.hasKey("Add") ? nbt.getByteArray("Add") : null;
        final byte[] b2High = nbt.hasKey("BlocksB2Hi") ? nbt.getByteArray("BlocksB2Hi") : null;
        final byte[] b3 = nbt.hasKey("BlocksB3") ? nbt.getByteArray("BlocksB3") : null;

        ebs.setB1(b1);
        if (b2Low == null) {
            ebs.clearB2Low();
        } else {
            ebs.setB2Low(new NibbleArray(b2Low, 4));
        }
        if (b2High == null) {
            ebs.clearB2High();
        } else {
            ebs.setB2High(new NibbleArray(b2High, 4));
        }
        ebs.setB3(b3);
    }

    public static int getBlockMeta(final IExtendedBlockStorageMixin ebs, final byte[] data, int offset) {
        val m1Low = ebs.getM1Low();
        System.arraycopy(m1Low.data, 0, data, offset, m1Low.data.length);
        offset += m1Low.data.length;
        val m1High = ebs.getM1High();
        if (m1High != null) {
            System.arraycopy(m1High.data, 0, data, offset, m1High.data.length);
            offset += m1High.data.length;
            val m2 = ebs.getM2();
            if (m2 != null) {
                System.arraycopy(m2, 0, data, offset, m2.length);
            }
        }
        return ebs.getEBSMask();
    }

    public static void setBlockMeta(final IExtendedBlockStorageMixin ebs, final byte[] data, int offset, final int storageFlag) {
        val m1Low = ebs.getM1Low();
        System.arraycopy(data, offset, m1Low.data, 0, m1Low.data.length);
        offset += m1Low.data.length;
        if (storageFlag == 0b01) {
            ebs.clearM1High();
            ebs.clearM2();
            return;
        }
        var m1High = ebs.getM1High();
        if (m1High == null) {
            m1High = ebs.createM1High();
        }
        System.arraycopy(data, offset, m1High.data, 0, m1High.data.length);
        if (storageFlag == 0b10) {
            ebs.clearM2();
            return;
        }
        var m2 = ebs.getM2();
        if (m2 == null) {
            m2 = ebs.createM2();
        }
        System.arraycopy(data, offset, m2, 0, m2.length);
    }

    public static void writeBlockMetaToNBT(IExtendedBlockStorageMixin ebs, NBTTagCompound nbt) {
        val m1Low = ebs.getM1Low();
        val m1High = ebs.getM1High();
        val m2 = ebs.getM2();
        nbt.setByteArray("Data", m1Low.data);
        if (m1High != null) {
            nbt.setByteArray("Data1High", m1High.data);
        }
        if (m2 != null) {
            nbt.setByteArray("Data2", m2);
        }
    }

    public static void readBlockMetaFromNBT(IExtendedBlockStorageMixin ebs, NBTTagCompound nbt) {
        assert nbt.hasKey("Data");
        val m1Low = nbt.getByteArray("Data");
        final byte[] m1High = nbt.hasKey("Data1High") ? nbt.getByteArray("Data1High") : null;
        final byte[] m2 = nbt.hasKey("Data2") ? nbt.getByteArray("Data2") : null;

        ebs.setM1Low(new NibbleArray(m1Low, 4));
        if (m1High == null) {
            ebs.clearM1High();
        } else {
            ebs.setM1High(new NibbleArray(m1High, 4));
        }
        ebs.setM2(m2);
    }

    public static int getIdFromBlockWithCheck(final Block block, final Block oldBlock) {
        final int id = Block.getIdFromBlock(block);
        if (GeneralConfig.catchUnregisteredBlocks && id == -1) {
            throw new IllegalArgumentException("Block " + block +
                                               " is not registered. <-- Say about this to the author of this mod, or you can try to enable \"RemoveInvalidBlocks\" option in EID config.");
        }
        if (id >= 0 && id <= ExtendedConstants.maxBlockID) {
            return id;
        }
        if (id == -1) {
            return Block.getIdFromBlock(oldBlock);
        }
        throw new IllegalArgumentException("id out of range: " + id);
    }

    public static void shortToByteArray(short[] shortArray, int shortOffset, byte[] byteArray, int byteOffset, int shortCount) {
        for (int i = 0; i < shortCount; i++) {
            short s = shortArray[shortOffset + i];
            byteArray[byteOffset + i * 2] = (byte) (s & 255);
            byteArray[byteOffset + i * 2 + 1] = (byte) ((s >>> 8) & 255);
        }
    }

    public static void byteToShortArray(byte[] byteArray, int byteOffset, short[] shortArray, int shortOffset, int byteCount) {
        for (int i = 0; i < byteCount; i++) {
            byte b = byteArray[byteOffset + i];
            shortArray[shortOffset + i / 2] = (short) (i % 2 == 0
                                                       ? (b & 255)
                                                       : (shortArray[shortOffset + i / 2] | ((b & 255) << 8)));
        }
    }

    public static byte[] shortToByteArray(short[] shortArray) {
        byte[] byteArray = new byte[shortArray.length * 2];
        shortToByteArray(shortArray, 0, byteArray, 0, shortArray.length);
        return byteArray;
    }

    public static short[] byteToShortArray(byte[] byteArray) {
        short[] shortArray = new short[(byteArray.length + 1) / 2];
        byteToShortArray(byteArray, 0, shortArray, 0, byteArray.length);
        return shortArray;
    }

    public static void byteToShortArrayLegacy(byte[] byteArray, short[] shortArray) {
        for (int i = 0; i < 256; ++i) {
            shortArray[i] = (short) (((byteArray[i + 256] << 8) & 255) | (byteArray[i] & 255));
        }
    }

    private static void scatter(byte[] byteArray, short[] shortArray) {
        for (int i = 0; i < byteArray.length; i++) {
            shortArray[i] = (short) (byteArray[i] & 0xff);
        }
    }

    public static int readBiomeArrayFromPacket(Chunk chunk, byte[] array, int offset) {
        short[] biomeArray = ((IChunkMixin) chunk).getBiomeShortArray();
        byteToShortArray(array, offset, biomeArray, 0, biomeArray.length * 2);
        return biomeArray.length * 2;
    }

    public static int writeBiomeArrayToPacket(Chunk chunk, byte[] array, int offset) {
        short[] biomeArray = ((IChunkMixin) chunk).getBiomeShortArray();
        shortToByteArray(biomeArray, 0, array, offset, biomeArray.length);
        return biomeArray.length * 2;
    }

    public static void writeChunkBiomeArrayToNbt(Chunk chunk, NBTTagCompound nbt) {
        byte[] byteArray = shortToByteArray(((IChunkMixin) chunk).getBiomeShortArray());
        nbt.setByteArray("Biomes16v2", byteArray);
    }

    public static void readChunkBiomeArrayFromNbt(Chunk chunk, NBTTagCompound nbt) {
        short[] data = ((IChunkMixin) chunk).getBiomeShortArray();
        boolean put = false;
        if (data == null) {
            data = new short[16 * 16];
            put = true;
        }
        if (nbt.hasKey("Biomes16v2", 7)) {
            byteToShortArray(nbt.getByteArray("Biomes16v2"), 0, data, 0, data.length * 2);
        } else if (nbt.hasKey("Biomes16", 7)) { //NotEnoughBiomes DFU
            byteToShortArrayLegacy(nbt.getByteArray("Biomes16"), data);
        } else if (nbt.hasKey("Biomes", 7)) { //Vanilla DFU
            scatter(nbt.getByteArray("Biomes"), data);
        } else {
            put = false;
        }
        if (put) {
            ((IChunkMixin) chunk).setBiomeShortArray(data);
        }
    }
}
