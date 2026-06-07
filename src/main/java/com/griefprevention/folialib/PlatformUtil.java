package com.griefprevention.folialib;

/**
 * copied from network.lithos.minecraft.folialib
 * @author Eli
 * @since April 27, 2026
 */
public final class PlatformUtil {
    private static final boolean IS_FOLIA = findFolia();

    private static boolean findFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        }
        catch (ClassNotFoundException exception) {
            return false;
        }
    }

    public static boolean isFolia() {
        return IS_FOLIA;
    }
}
