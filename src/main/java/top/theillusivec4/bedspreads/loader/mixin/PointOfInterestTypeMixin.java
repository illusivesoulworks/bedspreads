package top.theillusivec4.bedspreads.loader.mixin;

import java.util.Map;
import java.util.Set;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeMixin {

  @Accessor(value = "BLOCK_STATE_TO_POINT_OF_INTEREST_TYPE")
  static Map<BlockState, PointOfInterestType> getPoit() {
    throw new AssertionError();
  }

  @Accessor
  void setBlockStates(Set<BlockState> blockStates);

  @Accessor
  Set<BlockState> getBlockStates();
}
