package ca.fireball1725.mods.firelib2.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class RotationHelper {
  public static AxisAlignedBB rotateBB(AxisAlignedBB input, Direction facing) {
    switch (facing) {
      case NORTH:
      case SOUTH:
        return new AxisAlignedBB(input.minZ, input.minY, input.minX, input.maxZ, input.maxY, input.maxX);
      default:
        return input;
    }
  }

  public static VoxelShape rotateShape(VoxelShape input, Direction facing) {
    return VoxelShapes.create(rotateBB(input.getBoundingBox(), facing));
  }
}
