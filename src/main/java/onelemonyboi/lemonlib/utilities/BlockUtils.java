package onelemonyboi.lemonlib.utilities;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.apache.commons.lang3.tuple.Triple;

public class BlockUtils {
    public static Triple<Double, Double, Double> getRelativePosition(BlockPos pos, BlockRayTraceResult hit) {
        Vector3d vector = hit.getLocation();
        Triple<Double, Double, Double> triple = Triple.of(vector.x() - pos.getX(), vector.y() - pos.getY(), vector.z() - pos.getZ());
        return triple;
    }

    public static Triple<Long, Long, Long> getRelativePositionPixels(BlockPos pos, BlockRayTraceResult hit) {
        Triple<Double, Double, Double> triple = getRelativePosition(pos, hit);
        Triple<Long, Long, Long> exitTriple = Triple.of(Math.round(triple.getLeft() * 16), Math.round(triple.getMiddle() * 16), Math.round(triple.getRight() * 16));
        return exitTriple;
    }
}
