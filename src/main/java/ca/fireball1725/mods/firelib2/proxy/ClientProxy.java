package ca.fireball1725.mods.firelib2.proxy;

import ca.fireball1725.mods.firelib2.FireLib2;
import ca.fireball1725.mods.firelib2.FireMod;
import ca.fireball1725.mods.firelib2.util.RegistrationHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.Map;

public class ClientProxy extends CommonProxy {
  @Override
  public void init() {
  }

  @Override
  public World getClientWorld() {
    return Minecraft.getInstance().world;
  }

  @Override
  public PlayerEntity getClientPlayer() {
    return Minecraft.getInstance().player;
  }
}
