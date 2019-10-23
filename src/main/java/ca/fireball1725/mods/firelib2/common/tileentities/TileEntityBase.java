package ca.fireball1725.mods.firelib2.common.tileentities;

import javafx.geometry.Side;
import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileEntityBase extends TileEntity {
  private int renderedFragment = 0;

  public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return write(new CompoundNBT());
  }

  @Nullable
  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(pos, getType().hashCode(), getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    super.onDataPacket(net, pkt);
    read(pkt.getNbtCompound());
    markForUpdate();
  }

  protected void markDirtyQuick() {
    if (getWorld() != null) {
      getWorld().markChunkDirty(this.pos, this);
    }
  }

  public void markForUpdate() {
    if (this.renderedFragment > 0) {
      this.renderedFragment |= 0x1;
    } else if (this.getWorld() != null) {
      Block block = this.getWorld().getBlockState(this.pos).getBlock();
      //todo: look at this, is it correct?
      this.getWorld().notifyBlockUpdate(this.pos, this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), 3);

      int xCoord = this.pos.getX();
      int yCoord = this.pos.getY();
      int zCoord = this.pos.getZ();

      // Todo: update detectors?
      this.getWorld().notifyNeighborsOfStateChange(new BlockPos(xCoord, yCoord - 1, zCoord), block);
      this.getWorld().notifyNeighborsOfStateChange(new BlockPos(xCoord, yCoord + 1, zCoord), block);
      this.getWorld().notifyNeighborsOfStateChange(new BlockPos(xCoord - 1, yCoord, zCoord), block);
      this.getWorld().notifyNeighborsOfStateChange(new BlockPos(xCoord + 1, yCoord, zCoord), block);
      this.getWorld().notifyNeighborsOfStateChange(new BlockPos(xCoord, yCoord - 1, zCoord - 1), block);
      this.getWorld().notifyNeighborsOfStateChange(new BlockPos(xCoord, yCoord - 1, zCoord + 1), block);
    }
  }
}
