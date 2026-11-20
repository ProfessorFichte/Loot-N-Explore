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
    public record Look(float maxAlpha, int minAge, int ageSpread, float minScale, float scaleSpread, float growth,
                       float fadeIn, float fadeOut, float gravity, float red, float green, float blue) {
    }

    public static final Look FROST = new Look(0.35F, 30, 25, 0.25F, 0.15F, 0.8F, 0.2F, 0.6F, -0.004F, 0.78F, 0.92F, 1.0F);
    public static final Look BOOK_DUST = new Look(0.4F, 12, 8, 0.1F, 0.06F, 0.6F, 0.1F, 0.85F, -0.001F, 0.5F, 0.47F, 0.42F);

    private final SpriteProvider spriteProvider;
    private final Look look;
    private final float baseScale;

    protected FrostMistParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz,
                                SpriteProvider spriteProvider, Look look) {
        super(world, x, y, z, vx, vy, vz);
        this.spriteProvider = spriteProvider;
        this.look = look;
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        this.velocityMultiplier = 0.96F;
        this.gravityStrength = look.gravity();
        this.collidesWithWorld = false;
        this.maxAge = look.minAge() + this.random.nextInt(look.ageSpread());
        this.baseScale = look.minScale() + this.random.nextFloat() * look.scaleSpread();
        this.scale = this.baseScale;
        float shade = 0.9F + this.random.nextFloat() * 0.1F;
        this.setColor(look.red() * shade, look.green() * shade, look.blue() * shade);
        this.alpha = 0.0F;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.dead) return;
        this.setSpriteForAge(this.spriteProvider);
        float progress = (float) this.age / (float) this.maxAge;
        float fadeIn = MathHelper.clamp(progress / look.fadeIn(), 0.0F, 1.0F);
        float fadeOut = MathHelper.clamp((1.0F - progress) / look.fadeOut(), 0.0F, 1.0F);
        this.alpha = look.maxAlpha() * fadeIn * fadeOut;
        this.scale = this.baseScale * (1.0F + progress * look.growth());
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;
        private final Look look;

        public Factory(SpriteProvider spriteProvider) {
            this(spriteProvider, FROST);
        }

        public Factory(SpriteProvider spriteProvider, Look look) {
            this.spriteProvider = spriteProvider;
            this.look = look;
        }

        public static Factory bookDust(SpriteProvider spriteProvider) {
            return new Factory(spriteProvider, BOOK_DUST);
        }

        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double vx, double vy, double vz) {
            return new FrostMistParticle(world, x, y, z, vx, vy, vz, this.spriteProvider, this.look);
        }
    }
}
