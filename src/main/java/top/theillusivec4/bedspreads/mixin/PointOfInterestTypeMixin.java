package top.theillusivec4.bedspreads.mixin;

import java.util.Map;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.village.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeMixin {

  @Accessor(value = "POIT_BY_BLOCKSTATE")
  static Map<BlockState, PointOfInterestType> getPoit() {
    throw new AssertionError();
  }

  @Accessor
  void setBlockStates(Set<BlockState> blockStates);

  @Accessor
  Set<BlockState> getBlockStates();
}
