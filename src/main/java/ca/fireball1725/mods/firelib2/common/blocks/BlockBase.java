package ca.fireball1725.mods.firelib2.common.blocks;

import ca.fireball1725.mods.firelib2.FireLib2;
import ca.fireball1725.mods.firelib2.common.container.ContainerBase;
import ca.fireball1725.mods.firelib2.common.tileentities.TileEntityBase;
import ca.fireball1725.mods.firelib2.util.OrientationTools;
import ca.fireball1725.mods.firelib2.util.QuintupleFunction;
import ca.fireball1725.mods.firelib2.util.RotationType;
import ca.fireball1725.mods.firelib2.util.SextupleFunction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class BlockBase extends Block {
  public static final IProperty<?>[] ROTATION_HORIZONTAL_PROPERTIES = new IProperty[]{BlockStateProperties.HORIZONTAL_FACING};
  public static final IProperty<?>[] ROTATION_FULL_PROPERTIES = new IProperty[]{BlockStateProperties.FACING};
  public static final IProperty<?>[] ROTATION_NONE_PROPERTIES = new IProperty[0];
  private TileEntityType<? extends TileEntity> tileEntityType;
  private ContainerType<? extends Container> containerType;
  private Supplier<TileEntity> tileEntitySupplier;
  private boolean canRotate = false;
  public BlockBase(Properties properties) {
    super(properties);
  }

  public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
    return Direction.getFacingFromVector((float) (entity.posX - clickedBlock.getX()), (float) (entity.posY - clickedBlock.getY()), (float) (entity.posZ - clickedBlock.getZ()));
  }

  public static IProperty<?>[] getProperties(RotationType rotationType) {
    switch (rotationType) {
      case FULL:
        return ROTATION_FULL_PROPERTIES;
      case HORIZONTAL:
        return ROTATION_HORIZONTAL_PROPERTIES;
      case NONE:
      default:
        return ROTATION_NONE_PROPERTIES;
    }
  }

  public static Direction getFrontDirection(RotationType rotationType, BlockState state) {
    switch (rotationType) {
      case FULL:
        return OrientationTools.getOrientation(state);
      case HORIZONTAL:
        return OrientationTools.getOrientationHorizontal(state);
      default:
        return Direction.SOUTH;
    }
  }

  public Item.Properties getItemProperties() {
    return new Item.Properties();
  }

  public void setTileEntity(Supplier<TileEntity> tileEntitySupplier) {
    this.tileEntitySupplier = tileEntitySupplier;
    this.tileEntityType = TileEntityType.Builder.create(Objects.requireNonNull(tileEntitySupplier), this).build(null)
      .setRegistryName(Objects.requireNonNull(getRegistryName()));
  }

  public void setContainer(QuintupleFunction<Integer, World, BlockPos, PlayerInventory, PlayerEntity, ContainerBase> factory) {
    this.containerType = IForgeContainerType.create((windowId, inv, data) -> {
      BlockPos pos = data.readBlockPos();
      return factory.apply(windowId, FireLib2.proxy.getClientWorld(), pos, inv, FireLib2.proxy.getClientPlayer());
    }).setRegistryName(Objects.requireNonNull(getRegistryName()));
  }

  @Nullable
  public TileEntityType<? extends TileEntity> getTileEntityType() {
    return tileEntityType;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return tileEntitySupplier != null;
  }

  public boolean hasGui() {
    return containerType != null;
  }

  @Nullable
  public ContainerType<? extends Container> getContainerType() {
    return containerType;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return tileEntitySupplier.get();
  }

  public void setCanRotate(boolean canRotate) {
    this.canRotate = canRotate;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(getProperties());
  }

  public RotationType getRotationType() {
    return RotationType.FULL;
  }

  protected IProperty<?>[] getProperties() {
    return getProperties(getRotationType());
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    PlayerEntity placer = context.getPlayer();
    BlockPos pos = context.getPos();
    BlockState state = super.getStateForPlacement(context);
    switch (getRotationType()) {
      case FULL:
        return state.with(BlockStateProperties.FACING, OrientationTools.getFacingFromEntity(pos, placer));
      case HORIZONTAL:
        return state.with(BlockStateProperties.HORIZONTAL_FACING, placer.getHorizontalFacing().getOpposite());
      default:
        return state;
    }
  }

  protected Direction getOrientation(BlockPos pos, LivingEntity entity) {
    switch (getRotationType()) {
      case FULL:
        return OrientationTools.determineOrientation(pos, entity);
      case HORIZONTAL:
        return OrientationTools.determineOrientationHorizontal(entity);
      default:
        return null;
    }
  }

  public Direction getFrontDirection(BlockState state) {
    switch (getRotationType()) {
      case FULL:
        return state.get(BlockStateProperties.FACING);
      case HORIZONTAL:
        return state.get(BlockStateProperties.HORIZONTAL_FACING);
      default:
        return Direction.NORTH;
    }
  }

  @Override
  public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction) {
    switch (getRotationType()) {
      case FULL:
        state = state.with(BlockStateProperties.FACING, direction.rotate(state.get(BlockStateProperties.FACING)));
        break;
      case HORIZONTAL:
        state = state.with(BlockStateProperties.HORIZONTAL_FACING, direction.rotate(state.get(BlockStateProperties.HORIZONTAL_FACING)));
        break;
      default:
    }
    TileEntity tileEntity = world.getTileEntity(pos);
    if (tileEntity instanceof TileEntityBase) {
      //((TileEntityBase) tileEntity).rotateBlock(direction);
    }
    return state;
  }

  public Direction getRightDirection(BlockState state) {
    return getFrontDirection(state).rotateYCCW();
  }

  public Direction getLeftDirection(BlockState state) {
    return getFrontDirection(state).rotateY();
  }
}
