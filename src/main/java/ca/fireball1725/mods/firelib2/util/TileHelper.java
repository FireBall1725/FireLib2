package ca.fireball1725.mods.firelib2.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class TileHelper {
  @Nullable
  public static <T> T getTileEntity(World world, BlockPos pos, Class<T> tClass) {
    TileEntity tileEntity = world.getTileEntity(pos);
    return tClass.isInstance(tileEntity) ? (T)tileEntity : null;
  }

  public static boolean dropItemStack(ItemStack itemStack, World world, BlockPos pos) {
    if (!world.isRemote && !itemStack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && !world.restoringBlockSnapshots) {
      float f = 0.5F;
      double d0 = (double)(world.rand.nextFloat() * 0.5F) + 0.25D;
      double d1 = (double)(world.rand.nextFloat() * 0.5F) + 0.25D;
      double d2 = (double)(world.rand.nextFloat() * 0.5F) + 0.25D;
      ItemEntity itementity = new ItemEntity(world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemStack);
      itementity.setDefaultPickupDelay();
      world.addEntity(itementity);
      return true;
    }

    return false;
  }
}
