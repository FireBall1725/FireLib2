package ca.fireball1725.mods.firelib2.client.util;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.CapabilityEnergy;
import java.nio.ByteBuffer;
public class BlockModelRotator extends BufferManipulator {
  public BlockModelRotator(ByteBuffer original) {
    super(original);
  }
  public ByteBuffer getTransformed(float xIn, float yIn, float zIn, float angle, Axis axis, int packedLightCoords) {
    original.rewind();
    mutable.rewind();
    float cos = MathHelper.cos(angle);
    float sin = MathHelper.sin(angle);
    float x, y, z = 0;
    for (int vertex = 0; vertex < vertexCount(original); vertex++) {
      x = getX(original, vertex) - .5f;
      y = getY(original, vertex) - .5f;
      z = getZ(original, vertex) - .5f;
      putPos(mutable, vertex, rotateX(x, y, z, sin, cos, axis) + .5f + xIn,
        rotateY(x, y, z, sin, cos, axis) + .5f + yIn, rotateZ(x, y, z, sin, cos, axis) + .5f + zIn);
      putLight(mutable, vertex, packedLightCoords);
    }
    return mutable;
  }
}