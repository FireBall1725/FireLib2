package ca.fireball1725.mods.firelib2.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {
  World getClientWorld();
  PlayerEntity getClientPlayer();
}
