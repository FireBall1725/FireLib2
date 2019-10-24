package ca.fireball1725.mods.firelib2.util;

import ca.fireball1725.mods.firelib2.FireLib2;
import ca.fireball1725.mods.firelib2.FireMod;
import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;
import ca.fireball1725.mods.firelib2.common.blocks.IBlockItemProvider;
import ca.fireball1725.mods.firelib2.common.blocks.IItemPropertiesFiller;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class RegistrationHelper {
  private static final Map<String, ModRegistrationData> MODDATAMAP = new ConcurrentHashMap<>();

  public static void registerBlock(Block block) {
    register(block);

    if (block.hasTileEntity(null) && block instanceof BlockBase) {
      register(((BlockBase) block).getTileEntityType());
    }

    Item blockItem = createBlockItem(block);
    registerItem(blockItem);
  }

  public static void registerItem(Item item) {
    register(item);
  }

  public static void registerTileEntity(TileEntityType<? extends TileEntity> tileEntityType) {
    register(tileEntityType);
  }

  public static void registerRecipeSerializer(IRecipeSerializer<?> recipeSerializer) {
    register(recipeSerializer);
  }

  private static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistryEntry<T> object) {
    if (object == null)
      throw new IllegalArgumentException("Cannot register a null object");
    if (object.getRegistryName() == null)
      throw new IllegalArgumentException("Cannot register an object without a registry name");

    if (object instanceof IProvideEvent) {
      MinecraftForge.EVENT_BUS.register(object);
    }

    if (object instanceof IProvideRecipe) {
      FireLib2.LOGGER.info(">>> I Provide a recipe (" + object.getRegistryName() + ")");
    }

    getModData().modDefers.put(object.getRegistryType(), () -> object);
  }

  private static ModRegistrationData getModData() {
    String modId = ModLoadingContext.get().getActiveNamespace();
    ModRegistrationData data = MODDATAMAP.get(modId);

    if (data == null) {
      FireMod mod = FireLib2.FIREMODS.get(modId);
      data = new ModRegistrationData(mod.getLogger());
      MODDATAMAP.put(mod.getModId(), data);
      mod.getEventBus().addListener(RegistrationHelper::onRegistryEvent);
    }

    return data;
  }

  public static void onRegistryEvent(RegistryEvent.Register<?> event) {
    getModData().register(event.getRegistry());
  }

  private static Item createBlockItem(Block block) {
    Item.Properties itemProperties = new Item.Properties();
    ResourceLocation registryName = Objects.requireNonNull(block.getRegistryName());

    if (block instanceof IItemPropertiesFiller)
      ((IItemPropertiesFiller) block).fillProperties(itemProperties);

    BlockItem blockItem;
    if (block instanceof IBlockItemProvider)
      blockItem = ((IBlockItemProvider) block).provideBlockItem(block, itemProperties);
    else blockItem = new BlockItem(block, itemProperties);

    return blockItem.setRegistryName(registryName);
  }

  private static class ModRegistrationData {
    final Logger modLogger;
    final ArrayListMultimap<Class<?>, Supplier<IForgeRegistryEntry<?>>> modDefers = ArrayListMultimap.create();

    ModRegistrationData(Logger logger) {
      this.modLogger = logger;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    void register(IForgeRegistry registry) {
      Class<?> registryType = registry.getRegistrySuperType();

      if (modDefers.containsKey(registryType)) {
        Collection<Supplier<IForgeRegistryEntry<?>>> modEntries = modDefers.get(registryType);
        if (!modEntries.isEmpty()) modLogger.info("Registering {}s", registryType.getSimpleName());
        modEntries.forEach(supplier -> {
          IForgeRegistryEntry<?> entry = supplier.get();
          registry.register(entry);
          modLogger.debug("Registered {}", entry.getRegistryName());
        });
      }
    }
  }
}