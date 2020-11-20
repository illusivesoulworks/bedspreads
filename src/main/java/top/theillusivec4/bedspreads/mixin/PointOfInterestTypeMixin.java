package top.theillusivec4.bedspreads.mixin;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BedPart;
import net.minecraft.village.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.bedspreads.common.DecoratedBedsRegistry;

@Mixin(PointOfInterestType.class)
public abstract class PointOfInterestTypeMixin {

  @ModifyVariable(at = @At("HEAD"), method = "register(Ljava/lang/String;Ljava/util/Set;II)Lnet/minecraft/village/PointOfInterestType;", argsOnly = true)
  private static Set<BlockState> _bedspreads_register(Set<BlockState> blockStates, String key) {

    if (key.equals("home")) {
      Set<BlockState> states =
          DecoratedBedsRegistry.DECORATED_BED_BLOCK.getStateContainer().getValidStates().stream()
              .filter((state) ->
                  state.get(BedBlock.PART) == BedPart.HEAD).collect(Collectors.toSet());
      states.addAll(blockStates);
      return ImmutableSet.copyOf(states);
    }
    return blockStates;
  }
}
