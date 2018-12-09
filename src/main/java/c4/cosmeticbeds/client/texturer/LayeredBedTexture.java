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

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class LayeredBedTexture extends AbstractTexture {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ResourceLocation textureLocation;
    private final List<String> listTextures;
    private final List<EnumDyeColor> listDyeColors;

    public LayeredBedTexture(ResourceLocation textureLocationIn, List<String> p_i46101_2_, List<EnumDyeColor> p_i46101_3_)
    {
        this.textureLocation = textureLocationIn;
        this.listTextures = p_i46101_2_;
        this.listDyeColors = p_i46101_3_;
    }

    public void loadTexture(@Nonnull IResourceManager resourceManager) {
        this.deleteGlTexture();
        IResource iresource = null;
        BufferedImage bufferedimage;
        label255:{

            try {
                iresource = resourceManager.getResource(this.textureLocation);
                BufferedImage bufferedimage1 = TextureUtil.readBufferedImage(iresource.getInputStream());
                int i = bufferedimage1.getType();

                if (i == 0) {
                    i = 6;
                }
                bufferedimage = new BufferedImage(bufferedimage1.getWidth(), bufferedimage1.getHeight(), i);
                Graphics graphics = bufferedimage.getGraphics();
                graphics.drawImage(bufferedimage1, 0, 0, null);
                int j = 0;

                while (true) {

                    if (j >= 17 || j >= this.listTextures.size() || j >= this.listDyeColors.size()) {
                        break label255;
                    }
                    IResource iresource2 = null;

                    try {
                        String s = this.listTextures.get(j);
                        String colorName = (this.listDyeColors.get(j)).getDyeColorName();
                        IResource iresource1 = resourceManager.getResource(new ResourceLocation
                                ("minecraft:textures/entity/bed/" + colorName + ".png"));
                        BufferedImage bufferedimage2 = TextureUtil.readBufferedImage(iresource1.getInputStream());

                        if (s != null) {
                            iresource2 = resourceManager.getResource(new ResourceLocation(s));
                            BufferedImage bufferedimage3 = TextureUtil.readBufferedImage(iresource2.getInputStream());

                            if (bufferedimage3.getWidth() == bufferedimage.getWidth() && bufferedimage3.getHeight() == bufferedimage.getHeight() && bufferedimage3.getType() == 6) {

                                for (int l = 0; l < bufferedimage3.getHeight(); ++l) {

                                    for (int i1 = 0; i1 < bufferedimage3.getWidth(); ++i1) {
                                        int j1 = bufferedimage3.getRGB(i1, l);

                                        if ((j1 & -16777216) != 0) {
                                            int k1 = (j1 & 16711680) << 8 & -16777216;
                                            int l1 = bufferedimage1.getRGB(i1, l);
                                            int k = bufferedimage2.getRGB(i1, l);
                                            int i2 = MathHelper.multiplyColor(l1, k) & 16777215;
                                            bufferedimage3.setRGB(i1, l, k1 | i2);
                                        }
                                    }
                                }
                                bufferedimage.getGraphics().drawImage(bufferedimage3, 0, 0, null);
                            }
                        }
                    } finally {
                        IOUtils.closeQuietly(iresource2);
                    }
                    ++j;
                }
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't load layered image", ioexception);
            } finally {
                IOUtils.closeQuietly(iresource);
            }
            return;
        }
        TextureUtil.uploadTextureImage(this.getGlTextureId(), bufferedimage);
    }
}
