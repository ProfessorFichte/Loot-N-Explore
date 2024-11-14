package more_rpg_loot.client.entity.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;

public class GlazeEntityModel <T extends Entity> extends SinglePartEntityModel<T> {
    private final ModelPart root;
    private final ModelPart[] rods;
    private final ModelPart head;

    public GlazeEntityModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.rods = new ModelPart[12];
        Arrays.setAll(this.rods, (index) -> {
            return root.getChild(getRodName(index));
        });
    }

    private static String getRodName(int index) {
        return "part" + index;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), ModelTransform.NONE);
        float f = 0.0F;
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 16).cuboid(0.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F);

        int i;
        float g;
        float h;
        float j;
        for(i = 0; i < 6; ++i) {
            g = MathHelper.cos(f) * 9.0F;
            h = -2.0F + MathHelper.cos((float)(i * 2) * 0.25F);
            j = MathHelper.sin(f) * 9.0F;
            modelPartData.addChild(getRodName(i), modelPartBuilder, ModelTransform.pivot(g, h, j));
            ++f;
        }

        f = 0.7853982F;

        for(i = 6; i < 12; ++i) {
            g = MathHelper.cos(f) * 5.0F;
            h = 11.0F + MathHelper.cos((float)i * 1.5F * 0.5F);
            j = MathHelper.sin(f) * 5.0F;
            modelPartData.addChild(getRodName(i), modelPartBuilder, ModelTransform.pivot(g, h, j));
            ++f;
        }

        return TexturedModelData.of(modelData, 64, 32);
    }

    public ModelPart getPart() {
        return this.root;
    }

    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        float f = animationProgress * 3.1415927F * -0.1F;

        int i;
        for(i = 0; i < 6; ++i) {
            this.rods[i].pivotY = -12.0F + MathHelper.cos(((float)(i * 2) + animationProgress) * 0.25F);
            this.rods[i].pivotX = MathHelper.cos(f) * 8.0F;
            this.rods[i].pivotZ = MathHelper.sin(f) * 8.0F;
            ++f;
        }

        f = 0.7853982F + animationProgress * 3.1415927F * 0.03F;

        for(i = 6; i < 12; ++i) {
            this.rods[i].pivotY = 6.0F + MathHelper.cos(((float)i * 1.5F + animationProgress) * 0.5F);
            this.rods[i].pivotX = MathHelper.cos(f) * 4.0F;
            this.rods[i].pivotZ = MathHelper.sin(f) * 4.0F;
            ++f;
        }

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
    }
}
