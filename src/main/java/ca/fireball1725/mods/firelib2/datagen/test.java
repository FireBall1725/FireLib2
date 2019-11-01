package ca.fireball1725.mods.firelib2.datagen;

import ca.fireball1725.mods.firelib2.FireLib2;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class test extends RecipeProvider {
  public test(DataGenerator generatorIn) {
    super(generatorIn);
    FireLib2.LOGGER.info(">>> Registered Provider");
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    FireLib2.LOGGER.info(">>> Register Recipes Event " + consumer.toString());
    ShapedRecipeBuilder.shapedRecipe(Blocks.PUMPKIN)
      .patternLine("xxx")
      .patternLine("x#x")
      .patternLine("xxx")
      .key('x', Blocks.COBBLESTONE)
      .key('#', Tags.Items.DYES_RED)
      .setGroup("mytutorial")
      .addCriterion("cobblestone", InventoryChangeTrigger.Instance.forItems(Blocks.COBBLESTONE))
      .build(consumer);
  }
}
