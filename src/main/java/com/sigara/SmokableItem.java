package com.sigara;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;

/** Sigara / puro: sağ tık basılı tut -> içer, duman çıkar. Yan etki yok. */
public class SmokableItem extends Item {
    private final int duration;
    private final String buttId;      // bitince düşen izmarit (null olabilir)
    private final boolean reusable;   // pipo / nargile

    public SmokableItem(Properties props, int duration, String buttId, boolean reusable) {
        super(props);
        this.duration = duration;
        this.buttId = buttId;
        this.reusable = reusable;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isUnderWater()) {
            if (!level.isClientSide()) player.displayClientMessage(Component.translatable("message.sigara.underwater"), true);
            return InteractionResult.FAIL;
        }
        ItemStack lighter = Smoking.findLighter(player);
        if (lighter.isEmpty()) {
            if (!level.isClientSide()) player.displayClientMessage(Component.translatable("message.sigara.no_lighter"), true);
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) Smoking.flick(level, player, lighter);
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return duration;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.EAT;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remaining) {
        Smoking.tickEffects(level, entity, duration - remaining);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        Smoking.bigExhale(level, entity);
        if (reusable) {
            if (!(entity instanceof Player p && p.getAbilities().instabuild)) {
                int d = stack.getDamageValue() + 1;
                if (d >= stack.getMaxDamage()) stack.shrink(1);
                else stack.setDamageValue(d);
            }
        } else {
            stack.consume(1, entity);
            if (buttId != null && !level.isClientSide() && entity instanceof Player p) {
                Item butt = ModItems.ITEMS.get(buttId);
                if (butt != null) {
                    ItemStack out = new ItemStack(butt);
                    if (!p.getInventory().add(out)) p.drop(out, false);
                }
            }
        }
        return stack;
    }
}
