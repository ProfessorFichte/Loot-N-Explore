package lne.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class FrostMistParticle extends SpriteBillboardParticle {
    private static final float MAX_ALPHA = 0.35F;
    private final SpriteProvider spriteProvider;
    private final float baseScale;

    protected FrostMistParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, SpriteProvider spriteProvider) {
        super(world, x, y, z, vx, vy, vz);
        this.spriteProvider = spriteProvider;
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        this.velocityMultiplier = 0.96F;
        this.gravityStrength = -0.004F;
        this.collidesWithWorld = false;
        this.maxAge = 30 + this.random.nextInt(25);
        this.baseScale = 0.25F + this.random.nextFloat() * 0.15F;
        this.scale = this.baseScale;
        float shade = 0.9F + this.random.nextFloat() * 0.1F;
        this.setColor(0.78F * shade, 0.92F * shade, 1.0F);
        this.alpha = 0.0F;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.dead) return;
        this.setSpriteForAge(this.spriteProvider);
        float progress = (float) this.age / (float) this.maxAge;
        float fadeIn = MathHelper.clamp(progress / 0.2F, 0.0F, 1.0F);
        float fadeOut = MathHelper.clamp((1.0F - progress) / 0.6F, 0.0F, 1.0F);
        this.alpha = MAX_ALPHA * fadeIn * fadeOut;
        this.scale = this.baseScale * (1.0F + progress * 0.8F);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
            return new FrostMistParticle(world, x, y, z, vx, vy, vz, this.spriteProvider);
        }
    }
}
