package net.kastya_limoness.mahalmula_flight2.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.kastya_limoness.mahalmula_flight2.MahalmulaFlightII;
import net.kastya_limoness.mahalmula_flight2.items.MF2Items;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class MahalmulaShipRenderer extends EntityRenderer<MahalmulaShipEntity> {

    private Minecraft mc = Minecraft.getInstance();
    private ItemStack model = MF2Items.SHIP_MODEL.get().getDefaultInstance();
    private int ticker = 0;
    public MahalmulaShipRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(MahalmulaShipEntity p_110775_1_) {
        return null;
    }

    @Override
    public void render(MahalmulaShipEntity entity, float partialTicks, float p_225623_3_, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light) {
        int lightLevel = getLightLevel(entity.level, entity.blockPosition().above());
        Quaternion rotation = Vector3f.YP.rotationDegrees(entity.getTicker());
        model.getOrCreateTag().putInt("skin", entity.getEntityData().get(MahalmulaShipEntity.SKIN_PARAMETER));
        renderItem(model, new double[] { 0d, 1d, 0d },
                rotation, matrixStack, buffer, partialTicks,
                lightLevel, lightLevel, 1.3f);
        ticker = ++ticker % 360;
    }

    private void renderItem(ItemStack stack, double[] translation, Quaternion rotation, MatrixStack matrixStack,
                            IRenderTypeBuffer buffer, float partialTicks, int combinedOverlay, int lightLevel, float scale) {
        matrixStack.pushPose();
        matrixStack.translate(translation[0], translation[1], translation[2]);
        matrixStack.mulPose(rotation);
        matrixStack.scale(scale, scale, scale);

        IBakedModel model = mc.getItemRenderer().getModel(stack, null, null);
        mc.getItemRenderer().render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrixStack, buffer,
                lightLevel, combinedOverlay, model);
        matrixStack.popPose();
    }

    private int getLightLevel(World world, BlockPos pos) {
        //int bLight = world.getBrightness(LightType.BLOCK, pos);
        int sLight = world.getBrightness(LightType.SKY, pos);
        return LightTexture.pack(0, sLight);
    }
}
