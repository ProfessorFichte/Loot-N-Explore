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

public class BookDustParticle extends SpriteBillboardParticle {
    private static final float START_ALPHA = 0.8F;

    protected BookDustParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, SpriteProvider spriteProvider) {
        super(world, x, y, z, vx, vy, vz);
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        this.velocityMultiplier = 0.9F;
        this.gravityStrength = 0.02F;
        this.maxAge = 20 + this.random.nextInt(15);
        this.scale = 0.06F + this.random.nextFloat() * 0.05F;
        float shade = 0.72F + this.random.nextFloat() * 0.12F;
        this.setColor(shade, shade, shade);
        this.alpha = START_ALPHA;
        this.setSprite(spriteProvider);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.dead) return;
        this.alpha = START_ALPHA * (1.0F - (float) this.age / (float) this.maxAge);
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
            return new BookDustParticle(world, x, y, z, vx, vy, vz, this.spriteProvider);
        }
    }
}
