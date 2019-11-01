package ca.fireball1725.mods.firelib2.util;

import ca.fireball1725.mods.firelib2.common.blocks.BlockBase;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class BlockEnumProvider<T extends Enum<T>> {
  private Map<T, BlockBase> enumBlockMap = new HashMap<>();

  public BlockEnumProvider(Class<T> t, Function<T, BlockBase> factory) {
    EnumSet.allOf(t).forEach(mat -> {
      enumBlockMap.put(mat, factory.apply(mat));
    });
  }

  public BlockBase getBlock() {
    Optional<Map.Entry<T, BlockBase>> entry = enumBlockMap.entrySet().stream().findFirst();
    if(entry.isPresent())
      return enumBlockMap.entrySet().stream().findFirst().get().getValue();
    return null;
  }

  public BlockBase getBlockForType(T e) {
    return enumBlockMap.get(e);
  }
}
