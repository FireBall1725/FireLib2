package ca.fireball1725.mods.firelib2.datagen;

import ca.fireball1725.mods.firelib2.FireLib2;
import ca.fireball1725.mods.firelib2.FireMod;
import ca.fireball1725.mods.firelib2.common.recipes.RecipeSubscriber;
import ca.fireball1725.mods.firelib2.util.IProvideRecipe;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.RecipeProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import sun.rmi.log.LogHandler;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    FireLib2.LOGGER.info("Starting data generation");
    DataGenerator generator = event.getGenerator();
    if (event.includeServer()) {

      for (Map.Entry<String, FireMod> entry : FireLib2.FIREMODS.entrySet()) {
        FireMod fireMod = entry.getValue();
        if (fireMod.getBlocks() != null) {
          fireMod.getBlocks().forEach(block -> {
            if (block instanceof IProvideRecipe) {
              generator.addProvider(new RecipeGenerator(generator, block));
            }
          });
        }
      }

      generator.addProvider(new test(generator));
    }
  }
}
