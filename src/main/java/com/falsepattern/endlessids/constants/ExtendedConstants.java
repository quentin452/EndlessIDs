package com.falsepattern.endlessids.constants;

import com.falsepattern.endlessids.config.GeneralConfig;

@SuppressWarnings("unused")
public class ExtendedConstants {
    //Tunables
    public static final int bitsPerID = 24;
    public static final int bitsPerMetadata = 16;
    public static final int countCorrectionItemBits = 9 - GeneralConfig.extraItemIDBits;
    public static final int countCorrectionBlockBits = 12 - GeneralConfig.extraBlockIDBits;

    public static final int watchableBits = 7;
    public static final int bitsPerBiome = 16;

    public static final int maximumPotionIDs = 65536;
    public static final int maximumEnchantmentIDs = 32768;

    //Align with max item metadata for spawn eggs. Will be treated as short in netcode
    public static final int bitsPerEntity = 15;

    //BlockItemIDs
    public static final int nibblesPerID = bitsPerID / 4;
    public static final int bytesPerID = (nibblesPerID + 1) / 2;
    public static final int nibblesPerMetadata = bitsPerMetadata / 4;
    public static final int bytesPerIDPlusMetadata = (nibblesPerID + nibblesPerMetadata + 1) / 2;

    public static final int blockIDMask = (1 << bitsPerID) - 1;
    public static final int blockIDCount = 1 << (bitsPerID - countCorrectionBlockBits);
    public static final int maxBlockID = blockIDCount - 1;

    public static final int itemIDCount = 1 << (bitsPerID - countCorrectionItemBits);
    public static final int maxItemID = itemIDCount - 1;

    public static final int bitsPerBlock = 8 + bitsPerID + bitsPerMetadata;
    public static final int nibblesPerBlock = bitsPerBlock / 4;
    public static final int nibblesPerSubChunk = nibblesPerBlock * 16 * 16 * 16;
    public static final int bytesPerSubChunk = (nibblesPerSubChunk + 1) / 2;

    //BiomeIDs
    public static final int biomeIDCount = 1 << bitsPerBiome;
    public static final int biomeIDMask = biomeIDCount - 1;
    public static final int biomeIDNull = biomeIDMask;
    public static final int bytesPerBiome = (bitsPerBiome + 7) / 8;

    //DataWatcher
    public static final int watchableCount = 1 << watchableBits;
    public static final int maxWatchableID = watchableCount - 1;
    public static final int watchableMask = (0x7 << watchableBits) | maxWatchableID;

    //EntityIDs
    public static final int entityIDCount = 1 << bitsPerEntity;
    public static final int entityIDMask = entityIDCount - 1;
    public static final int maxEntityID = entityIDMask;
}
