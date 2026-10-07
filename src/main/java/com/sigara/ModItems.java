package com.sigara;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class ModItems {
    /** Kayıt sırasına göre tüm eşyalar (creative sekmesi de bu sırayı kullanır). */
    public static final Map<String, Item> ITEMS = new LinkedHashMap<>();

    private ModItems() {}

    private static Item reg(String name, Function<Item.Properties, Item> factory, Item.Properties props) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, SigaraMod.id(name));
        Item item = factory.apply(props.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        ITEMS.put(name, item);
        return item;
    }

    /** name, içme süresi (tick, 20 tick = 1 sn) */
    private static void smokable(String name, int ticks, String butt) {
        reg(name, p -> new SmokableItem(p, ticks, butt, false), new Item.Properties().stacksTo(64));
    }

    /** Pipo / nargile: tükenmez, her içişte dayanıklılık azalır. */
    private static void reusable(String name, int ticks, int uses) {
        reg(name, p -> new SmokableItem(p, ticks, null, true), new Item.Properties().durability(uses));
    }

    /** Paket: sağ tıkla -> içindeki eşyadan 1 tane çıkar. count = paketteki adet. */
    private static void pack(String name, String content, int count) {
        Item c = ITEMS.get(content);
        reg(name, p -> new PackItem(p, c), new Item.Properties().durability(count));
    }

    /** durability <= 0 ise sonsuz kullanımlık. */
    private static void lighter(String name, int durability) {
        Item.Properties props = durability > 0 ? new Item.Properties().durability(durability)
                                               : new Item.Properties().stacksTo(1);
        reg(name, LighterItem::new, props);
    }

    public static void init() {
        // Sigaralar
        smokable("cigarette_classic", 120, "cigarette_butt");
        smokable("cigarette_slim", 100, "cigarette_butt");
        smokable("cigarette_menthol", 120, "cigarette_butt");
        smokable("cigarette_king", 160, "cigarette_butt");
        smokable("cigarette_premium", 140, "cigarette_butt");
        smokable("cigarette_rolled", 130, "cigarette_butt");
        // Purolar
        smokable("cigar_corona", 400, "cigar_stub");
        smokable("cigar_maduro", 440, "cigar_stub");
        smokable("cigarillo", 220, "cigar_stub");
        // Paketler / kutular
        pack("pack_classic", "cigarette_classic", 20);
        pack("pack_slim", "cigarette_slim", 20);
        pack("pack_menthol", "cigarette_menthol", 20);
        pack("pack_king", "cigarette_king", 20);
        pack("pack_premium", "cigarette_premium", 20);
        pack("pouch_rolling", "cigarette_rolled", 30);
        pack("box_cigar_corona", "cigar_corona", 10);
        pack("box_cigar_maduro", "cigar_maduro", 10);
        pack("pack_cigarillo", "cigarillo", 8);
        pack("carton_classic", "pack_classic", 10);
        pack("carton_menthol", "pack_menthol", 10);
        pack("carton_premium", "pack_premium", 10);
        // Pipo ve nargile
        reusable("tobacco_pipe", 360, 40);
        reusable("hookah", 500, 60);
        // Çakmaklar
        lighter("lighter_disposable", 80);
        lighter("lighter_chrome", 0);
        lighter("lighter_gold", 0);
        lighter("lighter_black", 0);
        lighter("lighter_torch", 0);
        lighter("matchbox", 40);
        // İzmaritler (içilen sigara/puro bitince envantere düşer)
        reg("cigarette_butt", Item::new, new Item.Properties().stacksTo(64));
        reg("cigar_stub", Item::new, new Item.Properties().stacksTo(64));
    }
}
