package more_rpg_loot.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import static more_rpg_loot.RPGLoot.MOD_ID;

public class InnkeeperShelfBlock extends Block {
    public static Identifier ID = new Identifier(MOD_ID, "innkeeper_shelf");
    public InnkeeperShelfBlock(Settings settings) {
        super(settings);
    }
}
