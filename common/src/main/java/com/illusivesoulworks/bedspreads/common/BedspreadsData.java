package com.illusivesoulworks.bedspreads.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record BedspreadsData(ItemStack bed, ItemStack banner) {

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
}
