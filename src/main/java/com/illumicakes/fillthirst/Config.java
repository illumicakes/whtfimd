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
                  "youkaishomecoming:affogato": {"hydration": 4, "quenching": 2.4}
                }
                """;

        Files.writeString(path, json);
        FillInTheThirst.LOGGER.info("Generated default thirst_drinks.json");
    }
}