package ca.fireball1725.mods.firelib2.common.container.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotOutput extends SlotItemHandler {
  private final PlayerEntity playerEntity;
  private int removeCount;

  public SlotOutput(PlayerEntity playerEntity, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
    this.playerEntity = playerEntity;
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  @Override
  public ItemStack decrStackSize(int amount) {
    if (this.getHasStack()) {
      this.removeCount += Math.min(amount, this.getStack().getCount());
    }

    return super.decrStackSize(amount);
  }

  @Override
  public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
    this.onCrafting(stack);
    super.onTake(thePlayer, stack);
    return stack;
  }

  @Override
  protected void onCrafting(ItemStack stack, int amount) {
    this.removeCount += amount;
    this.onCrafting(stack);
  }

  @Override
  protected void onCrafting(ItemStack stack) {
    stack.onCrafting(this.playerEntity.world, this.playerEntity, this.removeCount);

    this.removeCount = 0;

    //todo: call the tile entity for processing? IProcessItem? maybe?
  }
}
