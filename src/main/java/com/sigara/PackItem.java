package com.sigara;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Paket / kutu: sağ tık -> içinden 1 sigara/puro çıkar. Dayanıklılık çubuğu kalan adedi gösterir. */
public class PackItem extends Item {
    private final Item content;

    public PackItem(Properties props, Item content) {
        super(props);
        this.content = content;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack pack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            ItemStack out = new ItemStack(content);
            if (!player.getInventory().add(out)) player.drop(out, false);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.4F, 1.3F);
            if (!player.getAbilities().instabuild) {
                int d = pack.getDamageValue() + 1;
                if (d >= pack.getMaxDamage()) pack.shrink(1);
                else pack.setDamageValue(d);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
