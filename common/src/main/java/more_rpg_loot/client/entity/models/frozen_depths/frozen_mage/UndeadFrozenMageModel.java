package more_rpg_loot.client.entity.models.frozen_depths.frozen_mage;

import more_rpg_loot.entity.frozen_depths.mob.frozen_mage.UndeadFrozenMageEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 5.1.4
// Converted for Fabric 1.21.1 with Yarn mappings

public class UndeadFrozenMageModel<T extends UndeadFrozenMageEntity> extends SinglePartEntityModel<T> {
    private final ModelPart Main;
    private final ModelPart waist;
    private final ModelPart Body;
    private final ModelPart cloak;
    private final ModelPart Head;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart leftItem;
    private final ModelPart ice_orb;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;

    public UndeadFrozenMageModel(ModelPart root) {
        this.Main = root.getChild("Main");
        this.waist = this.Main.getChild("waist");
        this.Body = this.waist.getChild("Body");
        this.cloak = this.Body.getChild("cloak");
        this.Head = this.Body.getChild("Head");
        this.RightArm = this.Body.getChild("RightArm");
        this.LeftArm = this.Body.getChild("LeftArm");
        this.leftItem = this.LeftArm.getChild("leftItem");
        this.ice_orb = this.leftItem.getChild("ice_orb");
        this.RightLeg = this.Main.getChild("RightLeg");
        this.LeftLeg = this.Main.getChild("LeftLeg");
    }

    @Override
    public ModelPart getPart() {
        return this.Main;
    }

    public static TexturedModelData createBodyLayer() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData Main = modelPartData.addChild("Main", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData waist = Main.addChild("waist", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData Body = waist.addChild("Body", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F))
        .uv(0, 30).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 14.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cloak = Body.addChild("cloak", ModelPartBuilder.create(), ModelTransform.of(-0.5F, -13.0F, 0.5F, 0.0436F, 0.0F, 0.0F));

        ModelPartData Body_r1 = cloak.addChild("Body_r1", ModelPartBuilder.create().uv(26, 32).cuboid(-6.0F, 0.3731F, -1.18F, 13.0F, 20.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        ModelPartData Head = Body.addChild("Head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.025F))
        .uv(32, 16).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.275F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));

        ModelPartData RightArm = Body.addChild("RightArm", ModelPartBuilder.create().uv(56, 2).cuboid(-2.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F))
        .uv(8, 16).cuboid(-3.0F, -1.5F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.05F)), ModelTransform.pivot(-4.0F, -11.0F, 0.0F));

        ModelPartData LeftArm = Body.addChild("LeftArm", ModelPartBuilder.create().uv(56, 2).mirrored().cuboid(0.0F, -1.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
        .uv(8, 16).mirrored().cuboid(0.0F, -1.5F, -1.5F, 3.0F, 6.0F, 3.0F, new Dilation(0.05F)).mirrored(false), ModelTransform.pivot(4.0F, -11.0F, 0.0F));

        ModelPartData leftItem = LeftArm.addChild("leftItem", ModelPartBuilder.create(), ModelTransform.pivot(2.0F, 8.0F, 1.0F));

        ModelPartData ice_orb = leftItem.addChild("ice_orb", ModelPartBuilder.create().uv(22, 16).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.25F, 0.5F, -5.0F, 0.0F, -1.789F, 0.7854F));

        ModelPartData cube_r1 = ice_orb.addChild("cube_r1", ModelPartBuilder.create().uv(2, 52).cuboid(-5.5F, -5.5F, 0.0F, 11.0F, 11.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r2 = ice_orb.addChild("cube_r2", ModelPartBuilder.create().uv(2, 52).cuboid(-1.7479F, -4.7929F, 1.5561F, 11.0F, 11.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.7521F, 0.7071F, -1.5561F, 0.0F, 0.0F, -3.1416F));

        ModelPartData RightLeg = Main.addChild("RightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -12.0F, 0.0F));

        ModelPartData LeftLeg = Main.addChild("LeftLeg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(2.0F, -12.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        // Vanilla walk-cycle blending fights the cast pose if residual limb-swing momentum is
        // still present right as casting starts (navigation.stop() doesn't zero it out instantly) -
        // skip it while casting so the cast animation isn't intermittently masked.
        if (!entity.isCasting()) {
            this.animateMovement(UndeadFrozenMageAnimations.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
        }
        this.updateAnimation(entity.idleAnimationState, UndeadFrozenMageAnimations.idle, ageInTicks);
        this.updateAnimation(entity.castAnimationState, UndeadFrozenMageAnimations.cast, ageInTicks);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        Main.render(matrices, vertices, light, overlay, color);
    }
}
