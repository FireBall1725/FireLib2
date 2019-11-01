package ca.fireball1725.mods.firelib2;

import ca.fireball1725.mods.firelib2.proxy.ClientProxy;
import ca.fireball1725.mods.firelib2.proxy.IProxy;
import ca.fireball1725.mods.firelib2.proxy.ServerProxy;
import ca.fireball1725.mods.firelib2.util.PlayerHelper;
import ca.fireball1725.mods.firelib2.util.RegistrationHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mod("firelib2")
public class FireLib2 extends FireMod {
  public static final Logger LOGGER = LogManager.getLogger("firelib2");
  public static final Map<String, FireMod> FIREMODS = new ConcurrentHashMap<>();
  public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

  public FireLib2() {
    super();
    OBJLoader.INSTANCE.addDomain("firelib2");
    PlayerHelper.loadVipPlayers();
  }

  public static void registerMod(FireMod fireMod) {
    if (FireLib2.FIREMODS.containsKey(fireMod.getModId())) {
      FireLib2.LOGGER.warn("Cannot register {} because it has already been registered.", fireMod.getModId());
      return;
    }

    FireLib2.FIREMODS.put(fireMod.getModId(), fireMod);

    if (fireMod.getBlocks() != null)
      fireMod.getBlocks().forEach(RegistrationHelper::registerBlock);
    if (fireMod.getItems() != null)
      fireMod.getItems().forEach(RegistrationHelper::registerItem);
    if (fireMod.getRecipeSerializers() != null)
      fireMod.getRecipeSerializers().forEach(RegistrationHelper::registerRecipeSerializer);
  }

  @Override
  public ArrayList<Block> getBlocks() {
    return null;
  }

  @Override
  public ArrayList<Item> getItems() {
    return null;
  }

  @Override
  public ArrayList<IRecipeSerializer<?>> getRecipeSerializers() {
    return null;
  }
}
