package net.kastya_limoness.mahalmula_flight2.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.kastya_limoness.mahalmula_flight2.items.MF2Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class MahalmulaShipRenderer extends EntityRenderer<MahalmulaShipEntity> {
    private Minecraft mc = Minecraft.getInstance();
    private ItemStack model = MF2Items.SHIP_MODEL.get().getDefaultInstance();
    private int ticker = 0;

    public MahalmulaShipRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public ResourceLocation getTextureLocation(MahalmulaShipEntity p_110775_1_) {
        return null;
    }

    @Override
    public void render(MahalmulaShipEntity entity, float partialTicks, float p_225623_3_, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        int lightLevel = getLightLevel(entity.level, entity.blockPosition().above());
        Quaternion rotation = Vector3f.YP.rotationDegrees(entity.getTicker());
        model.getOrCreateTag().putInt("skin", entity.getEntityData().get(MahalmulaShipEntity.SKIN_PARAMETER));
        renderItem(model, new double[] { 0d, 1d, 0d },
                rotation, matrixStack, buffer, partialTicks,
                lightLevel, lightLevel, 1.3f);
        ticker = ++ticker % 360;
    }



    private void renderItem(ItemStack stack, double[] translation, Quaternion rotation, PoseStack matrixStack,
                            MultiBufferSource buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(rotation);
        matrixStack.scale(scale, scale, scale);

        BakedModel model = mc.getItemRenderer().getModel(stack, null, null, 1);
        mc.getItemRenderer().render(stack, ItemTransforms.TransformType.FIXED, true, matrixStack, buffer,
                lightLevel, combinedOverlay, model);
        matrixStack.popPose();
    }

    private int getLightLevel(Level world, BlockPos pos) {
        //int bLight = world.getBrightness(LightType.BLOCK, pos);
        int sLight = world.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(0, sLight);
    }
}
