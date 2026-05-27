package com.illumicakes.fillthirst;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final Map<String, DrinkValue> DRINKS = new HashMap<>();

    public record DrinkValue(int hydration, float quenching) {}

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

                    int hydration = val.has("hydration")
                            ? val.get("hydration").getAsInt()
                            : 0;

                    float quenching = val.has("quenching")
                            ? val.get("quenching").getAsFloat()
                            : 0.0F;

                    DRINKS.put(entry.getKey(), new DrinkValue(hydration, quenching));
                });
            }

            FillInTheThirst.LOGGER.info("Loaded {} thirst drinks from config", DRINKS.size());
        } catch (Exception e) {
            FillInTheThirst.LOGGER.error("Failed to load thirst config", e);
        }
    }

    private static void generateDefaultConfig(Path path) throws IOException {
        String json = """
                {
                  "youkaishomecoming:green_tea": {"hydration": 6, "quenching": 4.0},
                  "youkaishomecoming:sake": {"hydration": 4, "quenching": 2.5},
                  "youkaishomecoming:matcha": {"hydration": 7, "quenching": 5.0},
                  "youkaishomecoming:americano": {"hydration": 3, "quenching": 1.5},
                  "youkaishomecoming:black_tea": {"hydration": 5, "quenching": 3.5},
                  "youkaishomecoming:blood_bottle": {"hydration": 3, "quenching": 2.0},
                  "youkaishomecoming:cappuccino": {"hydration": 4, "quenching": 2.0},
                  "youkaishomecoming:champagne": {"hydration": 3, "quenching": 1.8},
                  "youkaishomecoming:espresso": {"hydration": 2, "quenching": 1.0},
                  "youkaishomecoming:latte": {"hydration": 5, "quenching": 2.5},
                  "youkaishomecoming:oolong_tea": {"hydration": 6, "quenching": 4.1},
                  "youkaishomecoming:red_wine": {"hydration": 3, "quenching": 2.0},
                  "youkaishomecoming:white_wine": {"hydration": 3, "quenching": 2.1},
                  "youkaishomecoming:affogato": {"hydration": 4, "quenching": 2.4},

                  "kaleidoscope_tavern:wine": {"hydration": 4, "quenching": 2.0},
                  "kaleidoscope_tavern:champagne": {"hydration": 3, "quenching": 1.8},
                  "kaleidoscope_tavern:vodka": {"hydration": 2, "quenching": 0.8},
                  "kaleidoscope_tavern:brandy": {"hydration": 3, "quenching": 1.2},
                  "kaleidoscope_tavern:carignan": {"hydration": 4, "quenching": 2.0},
                  "kaleidoscope_tavern:sakura_wine": {"hydration": 4, "quenching": 2.4},
                  "kaleidoscope_tavern:plum_wine": {"hydration": 4, "quenching": 2.3},
                  "kaleidoscope_tavern:whiskey": {"hydration": 2, "quenching": 0.9},
                  "kaleidoscope_tavern:ice_wine": {"hydration": 4, "quenching": 2.6},
                  "kaleidoscope_tavern:polaris_sweet_white": {"hydration": 4, "quenching": 2.5},
                  "kaleidoscope_tavern:honey_wine": {"hydration": 5, "quenching": 2.8},
                  "kaleidoscope_tavern:red_queen": {"hydration": 3, "quenching": 1.6},
                  "kaleidoscope_tavern:miners_star": {"hydration": 3, "quenching": 1.7},
                  "kaleidoscope_tavern:rum": {"hydration": 2, "quenching": 0.9},
                  "kaleidoscope_tavern:riesling_dry_white": {"hydration": 4, "quenching": 2.2},
                  "kaleidoscope_tavern:sunset_glow": {"hydration": 4, "quenching": 2.3},
                  "kaleidoscope_tavern:madame_shexiang": {"hydration": 4, "quenching": 2.1},
                  "kaleidoscope_tavern:sweet_berry_wine": {"hydration": 5, "quenching": 2.8},
                  "kaleidoscope_tavern:sherry": {"hydration": 3, "quenching": 1.7},
                  "kaleidoscope_tavern:mother_snow": {"hydration": 4, "quenching": 2.4},
                  "kaleidoscope_tavern:luminous_bride": {"hydration": 4, "quenching": 2.5},
                  "kaleidoscope_tavern:glowflower_brew": {"hydration": 4, "quenching": 2.4},
                  "kaleidoscope_tavern:sauvignon_blanc_dry_white": {"hydration": 4, "quenching": 2.2},
                  "kaleidoscope_tavern:vinegar": {"hydration": 1, "quenching": 0.2},
                  "kaleidoscope_tavern:watermelon_juice": {"hydration": 7, "quenching": 5.0},

                  "kaleidoscope_tavern:grape_bucket": {"hydration": 8, "quenching": 5.0},
                  "kaleidoscope_tavern:ice_grape_bucket": {"hydration": 8, "quenching": 5.2},
                  "kaleidoscope_tavern:gold_grape_bucket": {"hydration": 8, "quenching": 5.5},
                  "kaleidoscope_tavern:green_grape_bucket": {"hydration": 8, "quenching": 5.0},
                  "kaleidoscope_tavern:sweet_berries_bucket": {"hydration": 7, "quenching": 4.6},
                  "kaleidoscope_tavern:glow_berries_bucket": {"hydration": 7, "quenching": 4.8},

                  "kaleidoscope_cookery:pork_bone_soup": {"hydration": 5, "quenching": 3.0},
                  "kaleidoscope_cookery:seafood_miso_soup": {"hydration": 5, "quenching": 3.2},
                  "kaleidoscope_cookery:fearsome_thick_soup": {"hydration": 4, "quenching": 2.4},
                  "kaleidoscope_cookery:lamb_and_radish_soup": {"hydration": 5, "quenching": 3.0},
                  "kaleidoscope_cookery:braised_beef_with_potatoes": {"hydration": 4, "quenching": 2.5},
                  "kaleidoscope_cookery:wild_mushroom_rabbit_soup": {"hydration": 5, "quenching": 3.1},
                  "kaleidoscope_cookery:tomato_beef_brisket_soup": {"hydration": 5, "quenching": 3.3},
                  "kaleidoscope_cookery:pufferfish_soup": {"hydration": 4, "quenching": 2.0},
                  "kaleidoscope_cookery:borscht": {"hydration": 5, "quenching": 3.2},
                  "kaleidoscope_cookery:beef_meatball_soup": {"hydration": 5, "quenching": 3.0},
                  "kaleidoscope_cookery:chicken_and_mushroom_stew": {"hydration": 4, "quenching": 2.8},
                  "kaleidoscope_cookery:donkey_soup": {"hydration": 4, "quenching": 2.4},

                  "kaleidoscope_dim_wine:wine": {"hydration": 4, "quenching": 2.0},
                  "kaleidoscope_dim_wine:champagne": {"hydration": 3, "quenching": 1.8},
                  "kaleidoscope_dim_wine:vodka": {"hydration": 2, "quenching": 0.8},
                  "kaleidoscope_dim_wine:brandy": {"hydration": 3, "quenching": 1.2},
                  "kaleidoscope_dim_wine:whiskey": {"hydration": 2, "quenching": 0.9},
                  "kaleidoscope_dim_wine:rum": {"hydration": 2, "quenching": 0.9}
                }
                """;

        Files.writeString(path, json);
        FillInTheThirst.LOGGER.info("Generated default thirst_drinks.json");
    }
}