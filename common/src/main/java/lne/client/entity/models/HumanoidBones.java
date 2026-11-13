package lne.client.entity.models;

import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Arm;

public final class HumanoidBones {
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private Arm mainArm = Arm.RIGHT;

    public HumanoidBones(ModelPart body, ModelPart head, ModelPart rightArm, ModelPart leftArm, ModelPart rightLeg, ModelPart leftLeg) {
        this.body = body;
        this.head = head;
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
    }

    public void track(LivingEntity entity) {
        this.mainArm = entity.getMainArm();
    }

    public ModelPart body() { return body; }
    public ModelPart head() { return head; }
    public ModelPart rightArm() { return rightArm; }
    public ModelPart leftArm() { return leftArm; }
    public ModelPart rightLeg() { return rightLeg; }
    public ModelPart leftLeg() { return leftLeg; }
    public Arm mainArm() { return mainArm; }
}
