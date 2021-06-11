package top.theillusivec4.bedspreads.loader.mixin;

import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PointOfInterestType.class)
public interface MixinPointOfInterestType {

  @Accessor(value = "BLOCK_STATE_TO_POINT_OF_INTEREST_TYPE")
  static Map<BlockState, PointOfInterestType> getPoit() {
    throw new AssertionError();
  }

  @Mutable
  @Accessor
  void setBlockStates(Set<BlockState> blockStates);

  @Accessor
  Set<BlockState> getBlockStates();
}
