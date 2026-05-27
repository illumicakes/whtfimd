package com.illumicakes.fillthirst;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class Config {
    public static final Map<String, DrinkValue> DRINKS = new LinkedHashMap<>();

    public enum ThirstType {
        DRINK,
        FOOD;

        public static ThirstType fromConfig(String value) {
            return "food".equalsIgnoreCase(value) ? FOOD : DRINK;
        }

        public String configName() {
            return name().toLowerCase();
        }
    }

    public record DrinkValue(int hydration, float quenching, ThirstType type) {
        public DrinkValue(int hydration, float quenching) {
            this(hydration, quenching, ThirstType.DRINK);
        }
    }

    public static void init() {
        loadJsonConfig();
    }

    private static void loadJsonConfig() {
        try {
            Path configDir = FMLPaths.CONFIGDIR.get().resolve("fillthirst");
            Files.createDirectories(configDir);

            Path drinksJson = configDir.resolve("thirst_drinks.json");

            if (!Files.exists(drinksJson)) {
                generateDefaultConfig(drinksJson);
            }

            DRINKS.clear();

            Gson gson = new Gson();
            try (var reader = Files.newBufferedReader(drinksJson)) {
                JsonObject json = gson.fromJson(reader, JsonObject.class);

                if (json == null) {
                    FillInTheThirst.LOGGER.warn("thirst_drinks.json is empty.");
                    return;
                }

                json.entrySet().forEach(entry -> {
                    JsonObject val = entry.getValue().getAsJsonObject();

                    ThirstType type = val.has("type")
                            ? ThirstType.fromConfig(val.get("type").getAsString())
                            : ThirstType.DRINK;

                    int hydration = val.has("hydration")
                            ? val.get("hydration").getAsInt()
                            : 0;

                    float quenching = val.has("quenching")
                            ? val.get("quenching").getAsFloat()
                            : 0.0F;

                    DRINKS.put(entry.getKey(), new DrinkValue(hydration, quenching, type));
                });
            }

            FillInTheThirst.LOGGER.info("Loaded {} thirst entries from config", DRINKS.size());
        } catch (Exception e) {
            FillInTheThirst.LOGGER.error("Failed to load thirst config", e);
        }
    }

    private static void generateDefaultConfig(Path path) throws IOException {
        String json = """
                {
                  "youkaishomecoming:espresso": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:americano": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:ristretto": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:latte": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:affogato": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:con_panna": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:cappuccino": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:macchiato": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:mocha": {"hydration": 8, "quenching": 12.0},

                  "youkaishomecoming:green_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:white_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:black_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:oolong_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:dark_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:yellow_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:cornflower_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:tea_mocha": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:saidi_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:sakura_honey_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:genmai_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:scarlet_tea": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:green_water": {"hydration": 8, "quenching": 12.0},

                  "youkaishomecoming:black_grape_juice": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:red_grape_juice": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:white_grape_juice": {"hydration": 8, "quenching": 12.0},
                  "youkaishomecoming:red_wine": {"hydration": 10, "quenching": 14.0},
                  "youkaishomecoming:white_wine": {"hydration": 10, "quenching": 14.0},
                  "youkaishomecoming:burgundy": {"hydration": 10, "quenching": 14.0},
                  "youkaishomecoming:champagne": {"hydration": 10, "quenching": 14.0},
                  "youkaishomecoming:van_allen": {"hydration": 10, "quenching": 14.0},

                  "youkaishomecoming:mio": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:mead": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:sparrow_sake": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:kiku": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:hakutsuru": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:kappa_village": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:suigei": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:daiginjo": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:dassai": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:tengu_tango": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:full_moons_eve": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:scarlet_mist": {"hydration": 12, "quenching": 18.0},
                  "youkaishomecoming:wind_priestesses": {"hydration": 12, "quenching": 18.0},

                  "youkaishomecoming:milk_popsicle": {"hydration": 6, "quenching": 8.0},
                  "youkaishomecoming:big_popsicle": {"hydration": 6, "quenching": 8.0},
                  "youkaishomecoming:shirayuki": {"hydration": 6, "quenching": 8.0},
                  "youkaishomecoming:avgolemono": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "youkaishomecoming:higan_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "youkaishomecoming:poor_god_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "youkaishomecoming:power_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "youkaishomecoming:mushroom_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "youkaishomecoming:miso_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "youkaishomecoming:seafood_miso_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},

                  "kaleidoscope_tavern:watermelon_juice": {"hydration": 8, "quenching": 12.0},
                  "kaleidoscope_tavern:grape_bucket": {"hydration": 8, "quenching": 12.0},
                  "kaleidoscope_tavern:ice_grape_bucket": {"hydration": 8, "quenching": 12.0},
                  "kaleidoscope_tavern:gold_grape_bucket": {"hydration": 8, "quenching": 12.0},
                  "kaleidoscope_tavern:green_grape_bucket": {"hydration": 8, "quenching": 12.0},
                  "kaleidoscope_tavern:sweet_berries_bucket": {"hydration": 8, "quenching": 12.0},
                  "kaleidoscope_tavern:glow_berries_bucket": {"hydration": 8, "quenching": 12.0},

                  "kaleidoscope_tavern:wine": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:champagne": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:carignan": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:sakura_wine": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:plum_wine": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:ice_wine": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:polaris_sweet_white": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:honey_wine": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:riesling_dry_white": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:sunset_glow": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:madame_shexiang": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:sweet_berry_wine": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:mother_snow": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:luminous_bride": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:glowflower_brew": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_tavern:sauvignon_blanc_dry_white": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_dim_wine:wine": {"hydration": 10, "quenching": 14.0},
                  "kaleidoscope_dim_wine:champagne": {"hydration": 10, "quenching": 14.0},

                  "kaleidoscope_tavern:vodka": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_tavern:brandy": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_tavern:whiskey": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_tavern:red_queen": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_tavern:miners_star": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_tavern:rum": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_tavern:sherry": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_dim_wine:vodka": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_dim_wine:brandy": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_dim_wine:whiskey": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_dim_wine:rum": {"hydration": 12, "quenching": 18.0},
                  "kaleidoscope_tavern:vinegar": {"hydration": 1, "quenching": 0.2},

                  "kaleidoscope_cookery:pork_bone_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:seafood_miso_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:fearsome_thick_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:lamb_and_radish_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:braised_beef_with_potatoes": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:wild_mushroom_rabbit_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:tomato_beef_brisket_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:pufferfish_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:borscht": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:beef_meatball_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:chicken_and_mushroom_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "kaleidoscope_cookery:donkey_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},

                  "extradelight:glow_berry_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:sweet_berry_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:tomato_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:cactus_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:lemon_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:lime_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:orange_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:grapefruit_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:pickle_juice": {"hydration": 8, "quenching": 12.0},
                  "extradelight:lemonade": {"hydration": 8, "quenching": 12.0},
                  "extradelight:limeade": {"hydration": 8, "quenching": 12.0},
                  "extradelight:orangeade": {"hydration": 8, "quenching": 12.0},
                  "extradelight:ginger_beer": {"hydration": 10, "quenching": 14.0},

                  "extradelight:milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:chocolate_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:glow_berry_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:sweet_berry_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:pumpkin_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:honey_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:apple_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:cookie_dough_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:mint_chip_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:nut_butter_milkshake": {"hydration": 6, "quenching": 8.0},
                  "extradelight:chocolate_milk": {"hydration": 6, "quenching": 8.0},
                  "extradelight:eggnog": {"hydration": 6, "quenching": 8.0},
                  "extradelight:horchata": {"hydration": 6, "quenching": 8.0},
                  "extradelight:coffee": {"hydration": 6, "quenching": 8.0},
                  "extradelight:tea": {"hydration": 6, "quenching": 8.0},
                  "extradelight:dalgona_coffee": {"hydration": 6, "quenching": 8.0},

                  "extradelight:cactus_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:carrot_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:fish_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:potato_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:tomato_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:miso_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:hazelnut_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:mulligatawny_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:onion_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:oxtail_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:sauerkraut_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:chicken_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:lamb_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:pork_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:beef_stew_rice": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:chicken_stew_rice": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:fish_stew_rice": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:lamb_stew_rice": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:pork_stew_rice": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "extradelight:rabbit_stew_rice": {"hydration": 4, "quenching": 6.0, "type": "food"},

                  "fruitsdelight:hamimelon_juice": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:hawberry_tea": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:orange_juice": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:lemon_juice": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:pear_juice": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:mango_tea": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:peach_tea": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:lychee_cherry_tea": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:mangosteen_tea": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:kiwi_juice": {"hydration": 8, "quenching": 13.0},
                  "fruitsdelight:bellini_cocktail": {"hydration": 12, "quenching": 18.0},
                  "fruitsdelight:mango_milkshake": {"hydration": 6, "quenching": 8.0},
                  "fruitsdelight:bayberry_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:fig_chicken_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:orange": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:lychee": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:pineapple": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:kiwi": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:peach": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:hamimelon": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:mango": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "fruitsdelight:pear": {"hydration": 6, "quenching": 10.0, "type": "food"},
                  "fruitsdelight:baked_pear": {"hydration": 8, "quenching": 13.0, "type": "food"},
                  "fruitsdelight:hamimelon_popsicle": {"hydration": 8, "quenching": 13.0, "type": "food"},
                  "fruitsdelight:kiwi_popsicle": {"hydration": 8, "quenching": 13.0, "type": "food"},
                  "fruitsdelight:hamimelon_shaved_ice": {"hydration": 10, "quenching": 16.0, "type": "food"},
                  "fruitsdelight:pear_with_rock_sugar": {"hydration": 14, "quenching": 20.0, "type": "food"},

                  "hearthandharvest:blueberry_juice": {"hydration": 8, "quenching": 12.0},
                  "hearthandharvest:raspberry_juice": {"hydration": 8, "quenching": 12.0},
                  "hearthandharvest:cherry_juice": {"hydration": 8, "quenching": 12.0},
                  "hearthandharvest:red_grape_juice": {"hydration": 8, "quenching": 12.0},
                  "hearthandharvest:glow_berry_juice": {"hydration": 8, "quenching": 12.0},
                  "hearthandharvest:green_grape_juice": {"hydration": 8, "quenching": 12.0},
                  "hearthandharvest:sweet_berry_juice": {"hydration": 8, "quenching": 12.0},
                  "hearthandharvest:sweet_berry_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:blueberry_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:raspberry_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:red_grape_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:green_grape_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:cherry_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:glow_berry_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:melon_wine": {"hydration": 10, "quenching": 14.0},
                  "hearthandharvest:mead": {"hydration": 12, "quenching": 18.0},
                  "hearthandharvest:root_beer": {"hydration": 12, "quenching": 18.0},
                  "hearthandharvest:hard_cider": {"hydration": 12, "quenching": 18.0},
                  "hearthandharvest:moonshine": {"hydration": 12, "quenching": 18.0},
                  "hearthandharvest:goat_milk_bottle": {"hydration": 6, "quenching": 8.0},
                  "hearthandharvest:chocolate_milk_bottle": {"hydration": 6, "quenching": 8.0},
                  "hearthandharvest:corn_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},

                  "crabbersdelight:kelp_shake": {"hydration": 12, "quenching": 18.0},
                  "crabbersdelight:sea_pickle_juice": {"hydration": 8, "quenching": 12.0},
                  "crabbersdelight:coconut_milk": {"hydration": 8, "quenching": 12.0},

                  "eternal_starlight_delight:abyssal_juice": {"hydration": 12, "quenching": 18.0},
                  "eternal_starlight_delight:ether_bottle": {"hydration": 12, "quenching": 18.0},
                  "eternal_starlight_delight:starlight_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "eternal_starlight_delight:marimold_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "eternal_starlight_delight:starmina_noodle_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},

                  "miners_delight:water_cup": {"hydration": 8, "quenching": 12.0},
                  "miners_delight:milk_cup": {"hydration": 6, "quenching": 8.0},
                  "miners_delight:hot_cocoa_cup": {"hydration": 6, "quenching": 8.0},
                  "miners_delight:cave_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:cave_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:beetroot_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:mushroom_stew_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:rabbit_stew_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:baked_cod_stew_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:noodle_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:beef_stew_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:chicken_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:fish_stew_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:pumpkin_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:vegetable_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:egg_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:rock_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:spicy_hoglin_stew_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:spicy_noodle_soup_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "miners_delight:strider_stew_cup": {"hydration": 4, "quenching": 6.0, "type": "food"},

                  "mynethersdelight:egg_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "mynethersdelight:strider_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "mynethersdelight:spicy_noodle_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "mynethersdelight:spicy_hoglin_stew": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "mynethersdelight:rock_soup": {"hydration": 4, "quenching": 6.0, "type": "food"},
                  "mynethersdelight:tear_popsicle": {"hydration": 5, "quenching": 7.0, "type": "food"},

                  "ubesdelight:halo_halo": {"hydration": 5, "quenching": 7.0, "type": "food"},
                  "ubesdelight:milk_tea_ube": {"hydration": 6, "quenching": 8.0}
                }
                """;

        Files.writeString(path, json);
        FillInTheThirst.LOGGER.info("Generated default thirst_drinks.json");
    }
}
