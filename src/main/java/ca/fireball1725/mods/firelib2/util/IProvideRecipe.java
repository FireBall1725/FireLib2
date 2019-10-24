package ca.fireball1725.mods.firelib2.util;

import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public interface IProvideRecipe {
  void registerRecipes(Consumer<IFinishedRecipe> consumer);
}
