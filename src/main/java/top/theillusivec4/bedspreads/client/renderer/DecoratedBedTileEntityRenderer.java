/*
 * Copyright (C) 2018-2019  C4
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

package top.theillusivec4.bedspreads.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.state.properties.BedPart;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BedTileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import top.theillusivec4.bedspreads.Bedspreads;
import top.theillusivec4.bedspreads.common.DecoratedBedTileEntity;

public class DecoratedBedTileEntityRenderer extends TileEntityRenderer<DecoratedBedTileEntity> {

  private final ModelRenderer headPiece;
  private final ModelRenderer footPiece;
  private final ModelRenderer[] legPieces = new ModelRenderer[4];

  public DecoratedBedTileEntityRenderer(TileEntityRendererDispatcher dispatcher) {
    super(dispatcher);
    this.headPiece = new ModelRenderer(64, 64, 0, 0);
    this.headPiece.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
    this.footPiece = new ModelRenderer(64, 64, 0, 22);
    this.footPiece.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 6.0F, 0.0F);
    this.legPieces[0] = new ModelRenderer(64, 64, 50, 0);
    this.legPieces[1] = new ModelRenderer(64, 64, 50, 6);
    this.legPieces[2] = new ModelRenderer(64, 64, 50, 12);
    this.legPieces[3] = new ModelRenderer(64, 64, 50, 18);
    this.legPieces[0].addBox(0.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[1].addBox(0.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[2].addBox(-16.0F, 6.0F, -16.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[3].addBox(-16.0F, 6.0F, 0.0F, 3.0F, 3.0F, 3.0F);
    this.legPieces[0].rotateAngleX = ((float) Math.PI / 2F);
    this.legPieces[1].rotateAngleX = ((float) Math.PI / 2F);
    this.legPieces[2].rotateAngleX = ((float) Math.PI / 2F);
    this.legPieces[3].rotateAngleX = ((float) Math.PI / 2F);
    this.legPieces[0].rotateAngleZ = 0.0F;
    this.legPieces[1].rotateAngleZ = ((float) Math.PI / 2F);
    this.legPieces[2].rotateAngleZ = ((float) Math.PI * 1.5F);
    this.legPieces[3].rotateAngleZ = (float) Math.PI;
  }

  public static void renderPatterns(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light,
      int overlay, ModelRenderer modelRenderer, Material material,
      List<Pair<BannerPattern, DyeColor>> patterns) {
    modelRenderer
        .render(matrixStack, material.getBuffer(buffer, RenderType::entitySolid), light, overlay);

    for (int i = 0; i < 17 && i < patterns.size(); ++i) {
      Pair<BannerPattern, DyeColor> pair = patterns.get(i);
      float[] afloat = pair.getSecond().getColorComponentValues();
      Material patternMaterial = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
          new ResourceLocation(Bedspreads.MODID, "entity/" + pair.getFirst().getFileName()));
      modelRenderer
          .render(matrixStack, patternMaterial.getBuffer(buffer, RenderType::entityTranslucent),
              light, overlay, afloat[0], afloat[1], afloat[2], 1.0F);
    }
  }

  public void render(@Nonnull DecoratedBedTileEntity tileEntityIn, float partialTicks,
      @Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, int combinedLightIn,
      int combinedOverlayIn) {
    List<Pair<BannerPattern, DyeColor>> list = tileEntityIn.getPatternList();
    World world = tileEntityIn.getWorld();

    if (world != null) {
      BlockState blockstate = tileEntityIn.getBlockState();
      TileEntityMerger.ICallbackWrapper<? extends BedTileEntity> icallbackwrapper = TileEntityMerger
          .func_226924_a_(TileEntityType.BED, BedBlock::func_226863_i_, BedBlock::func_226862_h_,
              ChestBlock.FACING, blockstate, world, tileEntityIn.getPos(),
              (p_228846_0_, p_228846_1_) -> false);
      int i = icallbackwrapper.apply(new DualBrightnessCallback<>()).get(combinedLightIn);
      this.renderPiece(matrixStackIn, bufferIn, blockstate.get(BedBlock.PART) == BedPart.HEAD,
          blockstate.get(BedBlock.HORIZONTAL_FACING), i, combinedOverlayIn, false, list);
    } else {
      this.renderPiece(matrixStackIn, bufferIn, true, Direction.SOUTH, combinedLightIn,
          combinedOverlayIn, false, list);
      this.renderPiece(matrixStackIn, bufferIn, false, Direction.SOUTH, combinedLightIn,
          combinedOverlayIn, true, list);
    }
  }

  private void renderPiece(MatrixStack matrixStack, IRenderTypeBuffer buffer, boolean isHead,
      Direction direction, int light, int overlay, boolean p_228847_8_,
      List<Pair<BannerPattern, DyeColor>> patterns) {
    this.headPiece.showModel = isHead;
    this.footPiece.showModel = !isHead;
    this.legPieces[0].showModel = !isHead;
    this.legPieces[1].showModel = isHead;
    this.legPieces[2].showModel = !isHead;
    this.legPieces[3].showModel = isHead;
    matrixStack.push();
    matrixStack.translate(0.0D, 0.5625D, p_228847_8_ ? -1.0D : 0.0D);
    matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
    matrixStack.translate(0.5D, 0.5D, 0.5D);
    matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F + direction.getHorizontalAngle()));
    matrixStack.translate(-0.5D, -0.5D, -0.5D);
    Material material = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE,
        new ResourceLocation(Bedspreads.MODID, "entity/bed_base"));
    renderPatterns(matrixStack, buffer, light, overlay, this.headPiece, material, patterns);
    renderPatterns(matrixStack, buffer, light, overlay, this.footPiece, material, patterns);
    IVertexBuilder ivertexbuilder = material.getBuffer(buffer, RenderType::entitySolid);
    this.legPieces[0].render(matrixStack, ivertexbuilder, light, overlay);
    this.legPieces[1].render(matrixStack, ivertexbuilder, light, overlay);
    this.legPieces[2].render(matrixStack, ivertexbuilder, light, overlay);
    this.legPieces[3].render(matrixStack, ivertexbuilder, light, overlay);
    matrixStack.pop();
  }
}