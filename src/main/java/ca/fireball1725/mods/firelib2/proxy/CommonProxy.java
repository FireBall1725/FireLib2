package ca.fireball1725.mods.firelib2.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class CommonProxy implements IProxy {
  @Override
  public void init() {

  }

  @Override
  public World getClientWorld() {
    return null;
  }

  @Override
  public PlayerEntity getClientPlayer() {
    return null;
  }
}
