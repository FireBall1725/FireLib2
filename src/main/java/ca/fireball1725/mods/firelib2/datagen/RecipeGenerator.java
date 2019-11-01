package ca.fireball1725.mods.firelib2.datagen;

import ca.fireball1725.mods.firelib2.FireLib2;
import ca.fireball1725.mods.firelib2.util.IProvideRecipe;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider {
  private final Block BLOCK;

  public RecipeGenerator(DataGenerator generatorIn, Block block) {
    super(generatorIn);
    this.BLOCK = block;
    FireLib2.LOGGER.info(">>> Registered Provider: " + block.toString());
  }

  @Override
  protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    FireLib2.LOGGER.info(">>> Register Recipes Event " + BLOCK.toString());

    if (BLOCK instanceof IProvideRecipe)
      ((IProvideRecipe)BLOCK).registerRecipes(consumer);
  }
}
