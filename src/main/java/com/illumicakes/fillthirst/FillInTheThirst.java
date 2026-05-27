package com.illumicakes.fillthirst;

import com.mojang.logging.LogUtils;
import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
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

            Item item = BuiltInRegistries.ITEM.get(id);

            if (item == null) {
                LOGGER.warn("Drink item not found: {}", entry.getKey());
                continue;
            }

            Config.DrinkValue drink = entry.getValue();
            event.addDrink(item, drink.hydration(), Math.round(drink.quenching()));
        }

        LOGGER.info("Registered {} drinks with Thirst Was Taken", Config.DRINKS.size());
    }

    @SubscribeEvent
    public void onItemFinishedUse(LivingEntityUseItemEvent.Finish event) {
        if (ModList.get().isLoaded("thirst")) {
            return;
        }

        LivingEntity entity = event.getEntity();

        if (!(entity instanceof Player player)) {
            return;
        }

        ItemStack stack = event.getItem();
        Item item = stack.getItem();
        String itemId = BuiltInRegistries.ITEM.getKey(item).toString();

        Config.DrinkValue drink = Config.DRINKS.get(itemId);

        if (drink == null) {
            return;
        }

        giveLegendarySurvivalOverhaulThirst(player, drink);
        LOGGER.debug("{} gave {}/{} thirst", itemId, drink.hydration(), drink.quenching());
    }

    private void giveLegendarySurvivalOverhaulThirst(Player player, Config.DrinkValue drink) {
        if (!ModList.get().isLoaded("legendarysurvivaloverhaul")) {
            return;
        }

        try {
            Class<?> thirstUtil = Class.forName("sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil");

            thirstUtil
                    .getMethod("takeDrink", Player.class, int.class, float.class)
                    .invoke(null, player, drink.hydration(), drink.quenching());
        } catch (Exception e) {
            LOGGER.warn("Failed to apply Legendary Survival Overhaul thirst.", e);
        }
    }
}