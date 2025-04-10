package com.illusivesoulworks.bedspreads.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.TooltipProvider;

public record BedspreadsData(ItemStack bed, ItemStack banner) implements TooltipProvider {

  public static final BedspreadsData EMPTY = new BedspreadsData(ItemStack.EMPTY, ItemStack.EMPTY);

  public static final Codec<BedspreadsData> CODEC = RecordCodecBuilder
      .create(instance ->
                  instance.group(
                      ItemStack.CODEC.fieldOf(
                              "bed")
                          .forGetter(
                              BedspreadsData::bed),
                      ItemStack.CODEC.fieldOf(
                              "banner")
                          .forGetter(
                              BedspreadsData::banner)
                  ).apply(
                      instance,
                      BedspreadsData::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, BedspreadsData> STREAM_CODEC =
      StreamCodec.composite(
          ItemStack.STREAM_CODEC, BedspreadsData::bed,
          ItemStack.STREAM_CODEC, BedspreadsData::banner,
          BedspreadsData::new
      );

  public static final DataComponentType<BedspreadsData> TYPE =
      DataComponentType.<BedspreadsData>builder().persistent(CODEC)
          .networkSynchronized(STREAM_CODEC).build();

  @Override
  public void addToTooltip(@Nonnull Item.TooltipContext tooltipContext,
                           @Nonnull Consumer<Component> componentConsumer,
                           @Nonnull TooltipFlag tooltipFlag,
                           @Nonnull DataComponentGetter dataComponentGetter) {

    if (!this.bed.isEmpty()) {
      componentConsumer.accept(
          Component.translatable(this.bed.getItem().getDescriptionId())
              .withStyle(ChatFormatting.GRAY));
    }

    if (!this.banner.isEmpty()) {
      componentConsumer.accept(
          Component.translatable(this.banner.getItem().getDescriptionId())
              .withStyle(ChatFormatting.GRAY));
      TooltipDisplay tooltipDisplay =
          this.banner.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
      this.banner.addToTooltip(DataComponents.BANNER_PATTERNS, tooltipContext, tooltipDisplay,
                               componentConsumer, tooltipFlag);
    }
  }
}
