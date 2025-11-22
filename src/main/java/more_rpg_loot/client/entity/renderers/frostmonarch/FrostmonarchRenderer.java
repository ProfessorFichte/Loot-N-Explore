package more_rpg_loot.client.entity.renderers.frostmonarch;

import mod.azure.azurelib.common.model.AzBone;
import mod.azure.azurelib.common.render.AzRendererPipelineContext;
import mod.azure.azurelib.common.render.entity.AzEntityRenderer;
import mod.azure.azurelib.common.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.common.render.layer.AzArmorLayer;
import mod.azure.azurelib.common.render.layer.AzBlockAndItemLayer;
import more_rpg_loot.client.entity.renderers.frosthaunt.FrosthauntAnimatior;
import more_rpg_loot.entity.mob.FrosthauntEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static more_rpg_loot.RPGLoot.MOD_ID;
import static net.minecraft.client.render.model.json.ModelTransformationMode.THIRD_PERSON_LEFT_HAND;
import static net.minecraft.client.render.model.json.ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;


public class FrostmonarchRenderer extends AzEntityRenderer<FrosthauntEntity> {

    private static final Identifier GEO = Identifier.of(
            MOD_ID,
            "geo/entity/frosthaunt.geo.json"
    );

    private static final Identifier TEX = Identifier.of(
            MOD_ID,
            "textures/entity/frosthaunt.png"
    );

    public FrostmonarchRenderer(EntityRendererFactory.Context context) {
        super(
                AzEntityRendererConfig.<FrosthauntEntity>builder(GEO, TEX)
                        .setAnimatorProvider(FrosthauntAnimatior::new)
                        .addRenderLayer(new AzArmorLayer<>() {

                            @Override
                            protected @NotNull ModelPart getModelPartForBone(
                                    AzRendererPipelineContext<UUID, FrosthauntEntity> context,
                                    AzBone bone,
                                    BipedEntityModel<?> model
                            ) {
                                String b = bone.getName();

                                return switch (b) {
                                    case "Head2" -> model.head;
                                    case "Body" -> model.body;

                                    case "RightArm" -> model.rightArm;
                                    case "LeftArm" -> model.leftArm;

                                    case "RightLeg" -> model.rightLeg;
                                    case "LeftLeg" -> model.leftLeg;

                                    default -> model.body;
                                };
                            }

                            @Override
                            protected ItemStack getArmorItemForBone(
                                    AzRendererPipelineContext<UUID, FrosthauntEntity> context,
                                    AzBone bone
                            ) {
                                FrosthauntEntity e = context.animatable();
                                String b = bone.getName();

                                if (b.equals("Head2")) return e.getEquippedStack(EquipmentSlot.HEAD);
                                if (b.equals("Body")) return e.getEquippedStack(EquipmentSlot.CHEST);
                                if (b.equals("RightLeg") || b.equals("LeftLeg"))
                                    return e.getEquippedStack(EquipmentSlot.LEGS);

                                return ItemStack.EMPTY;
                            }
                        })
                        .addRenderLayer(new AzBlockAndItemLayer<>() {

                            @Override
                            public ItemStack itemStackForBone(AzBone bone, FrosthauntEntity e) {
                                String b = bone.getName();
                                if (b.equals("rightitem"))
                                    return e.getEquippedStack(EquipmentSlot.MAINHAND);

                                if (b.equals("leftitem"))
                                    return e.getEquippedStack(EquipmentSlot.OFFHAND);

                                return ItemStack.EMPTY;
                            }

                            @Override
                            protected ModelTransformationMode getTransformTypeForStack(
                                    AzBone bone, ItemStack stack, FrosthauntEntity e
                            ) {
                                String b = bone.getName();

                                if (b.equals("leftitem"))
                                    return THIRD_PERSON_LEFT_HAND;
                                else
                                    return THIRD_PERSON_RIGHT_HAND;
                            }

                            @Override
                            protected void renderItemForBone(
                                    AzRendererPipelineContext<UUID, FrosthauntEntity> context,
                                    AzBone bone,
                                    ItemStack stack,
                                    FrosthauntEntity entity
                            ) {
                                if (stack.isEmpty()) return;
                                if (bone.getName().equals("rightitem")&& !stack.isOf(Items.SHIELD)) {
                                    context.poseStack().multiply(
                                            RotationAxis.NEGATIVE_X.rotationDegrees(90f)
                                    );
                                }
                                if (bone.getName().equals("leftitem") && stack.isOf(Items.SHIELD)) {
                                    context.poseStack().multiply(
                                            RotationAxis.POSITIVE_Y.rotationDegrees(180f)
                                    );
                                }
                                context.poseStack().translate(0, 0.15, 0);

                                super.renderItemForBone(context, bone, stack, entity);
                            }
                        })

                        .build(),
                context
        );
    }
}
