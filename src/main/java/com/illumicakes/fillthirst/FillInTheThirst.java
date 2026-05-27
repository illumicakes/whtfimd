package com.illumicakes.fillthirst;

import com.mojang.logging.LogUtils;
import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(FillInTheThirst.MODID)
public class FillInTheThirst {
    public static final String MODID = "fillthirst";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FillInTheThirst(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        Config.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Where The F*** Is My Drink loaded - {} drinks configured", Config.DRINKS.size());
    }

    @SubscribeEvent
    public void onRegisterThirstValues(RegisterThirstValueEvent event) {
        for (var entry : Config.DRINKS.entrySet()) {
            ResourceLocation id = ResourceLocation.tryParse(entry.getKey());

            if (id == null) {
                LOGGER.warn("Invalid drink item id in config: {}", entry.getKey());
                continue;
            }

            if (isOptionalNamespaceMissing(id)) {
                continue;
            }

            if (!BuiltInRegistries.ITEM.containsKey(id)) {
                LOGGER.warn("Drink item not found: {}", entry.getKey());
                continue;
            }

            Item item = BuiltInRegistries.ITEM.get(id);

            Config.DrinkValue drink = entry.getValue();

            if (drink.type() == Config.ThirstType.FOOD) {
                event.addFood(item, drink.hydration(), Math.round(drink.quenching()));
            } else {
                event.addDrink(item, drink.hydration(), Math.round(drink.quenching()));
            }
        }

        LOGGER.info("Registered {} thirst entries with Thirst Was Taken", Config.DRINKS.size());
    }

    private boolean isOptionalNamespaceMissing(ResourceLocation id) {
        String namespace = id.getNamespace();
        return !"minecraft".equals(namespace) && !ModList.get().isLoaded(namespace);
    }
}
