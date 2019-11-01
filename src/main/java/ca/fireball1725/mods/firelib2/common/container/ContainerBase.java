package ca.fireball1725.mods.firelib2.common.container;

import ca.fireball1725.mods.firelib2.util.TileHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class ContainerBase extends Container {
  protected TileEntity tileEntity;
  protected PlayerEntity playerEntity;
  protected IItemHandler playerInventory;

  public ContainerBase(ContainerType<? extends Container> containerType, int windowID, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
    super(containerType, windowID);
    tileEntity = TileHelper.getTileEntity(world, pos, TileEntity.class);
    this.playerEntity = player;
    this.playerInventory = new InvWrapper(playerInventory);
  }

  @Override
  public boolean canInteractWith(PlayerEntity playerIn) {
    return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, tileEntity.getBlockState().getBlock());
  }

  private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
    for (int i = 0; i < amount; i++) {
      addSlot(new SlotItemHandler(handler, index, x, y));
      x += dx;
      index++;
    }
    return index;
  }

  private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
    for (int i = 0; i < verAmount; i++) {
      index = addSlotRange(handler, index, x, y, horAmount, dx);
      y += dy;
    }
    return index;
  }

  protected void addPlayerInventorySlots(int x, int y) {
    // Player Inventory
    addSlotBox(playerInventory, 9, x, y, 9, 18, 3, 18);

    // Hotbar
    addSlotRange(playerInventory, 0, x, y, 9, 18);
  }
}
