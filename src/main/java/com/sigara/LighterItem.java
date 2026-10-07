package com.sigara;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Çakmak: envanterdeyken sigaraları yakar. Sağ tıklayınca da "klik" diye alev çıkarır. */
public class LighterItem extends Item {
    public LighterItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isUnderWater()) return InteractionResult.FAIL;
        if (!level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            Smoking.flick(level, player, stack);
        }
        return InteractionResult.SUCCESS;
    }
}
