/*
 * Copyright (C) 2018  C4
 *
 * This file is part of Cosmetic Beds, a mod made for Minecraft.
 *
 * Cosmetic Beds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cosmetic Beds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cosmetic Beds.  If not, see <https://www.gnu.org/licenses/>.
 */

package c4.cosmeticbeds.client.texturer;

import c4.cosmeticbeds.CosmeticBeds;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BannerTextures;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BedTextures {

    public static final Cache BED_DESIGNS = new Cache("BED", new ResourceLocation(CosmeticBeds.MODID,
            "textures/entity/bed_base.png"), new ResourceLocation(CosmeticBeds.MODID,"textures/entity/").toString());

    @SideOnly(Side.CLIENT)
    public static class Cache {

        private final Map<String, CacheEntry> cacheMap = Maps.newLinkedHashMap();
        private final ResourceLocation cacheResourceLocation;
        private final String cacheResourceBase;
        private final String cacheId;

        public Cache(String id, ResourceLocation baseResource, String resourcePath) {
            this.cacheId = id;
            this.cacheResourceLocation = baseResource;
            this.cacheResourceBase = resourcePath;
        }

        @Nullable
        public ResourceLocation getResourceLocation(String id, EnumDyeColor baseColor, List<BannerPattern> patternList,
                                                    List<EnumDyeColor> colorList) {

            if (id.isEmpty()) {
                return null;
            } else {
                id = this.cacheId + id;
                CacheEntry bedtextures$cacheentry = this.cacheMap.get(id);

                if (bedtextures$cacheentry == null) {

                    if (this.cacheMap.size() >= 256 && !this.freeCacheSlot()) {
                        return BannerTextures.BANNER_BASE_TEXTURE;
                    }
                    List<String> list = Lists.newArrayList();

                    for (BannerPattern bannerpattern : patternList) {
                        list.add(this.cacheResourceBase + bannerpattern.getFileName() + ".png");
                    }
                    bedtextures$cacheentry = new CacheEntry();
                    bedtextures$cacheentry.textureLocation = new ResourceLocation(id);
                    Minecraft.getMinecraft().getTextureManager().loadTexture(bedtextures$cacheentry.textureLocation,
                            new LayeredBedTexture(cacheResourceLocation, list, colorList));
                    this.cacheMap.put(id, bedtextures$cacheentry);
                }
                bedtextures$cacheentry.lastUseMillis = System.currentTimeMillis();
                return bedtextures$cacheentry.textureLocation;
            }
        }

        private boolean freeCacheSlot() {
            long i = System.currentTimeMillis();
            Iterator<String> iterator = this.cacheMap.keySet().iterator();

            while (iterator.hasNext()) {
                String s = iterator.next();
                CacheEntry bedtextures$cacheentry = this.cacheMap.get(s);

                if (i - bedtextures$cacheentry.lastUseMillis > 5000L) {
                    Minecraft.getMinecraft().getTextureManager().deleteTexture(bedtextures$cacheentry.textureLocation);
                    iterator.remove();
                    return true;
                }
            }
            return this.cacheMap.size() < 256;
        }
    }

    @SideOnly(Side.CLIENT)
    static class CacheEntry {

        public long lastUseMillis;
        public ResourceLocation textureLocation;

        private CacheEntry() {}
    }
}
