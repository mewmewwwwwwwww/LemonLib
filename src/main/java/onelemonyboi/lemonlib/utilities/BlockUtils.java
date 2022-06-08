package onelemonyboi.lemonlib.utilities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Triple;

public class BlockUtils {
    public static Triple<Double, Double, Double> getRelativePosition(BlockPos pos, BlockHitResult hit) {
        Vec3 vector = hit.getLocation();
        Triple<Double, Double, Double> triple = Triple.of(vector.x() - pos.getX(), vector.y() - pos.getY(), vector.z() - pos.getZ());
        return triple;
    }

    public static Triple<Long, Long, Long> getRelativePositionPixels(BlockPos pos, BlockHitResult hit) {
        Triple<Double, Double, Double> triple = getRelativePosition(pos, hit);
        Triple<Long, Long, Long> exitTriple = Triple.of(Math.round(triple.getLeft() * 16), Math.round(triple.getMiddle() * 16), Math.round(triple.getRight() * 16));
        return exitTriple;
    }
}
