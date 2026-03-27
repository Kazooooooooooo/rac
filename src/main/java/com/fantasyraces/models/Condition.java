package com.fantasyraces.models;

import java.util.List;

public class Condition {

    public enum Trigger {
        BELOW_Y, ABOVE_Y,
        IS_DAYTIME, IS_NIGHTTIME,
        IN_WATER, ON_FIRE,
        IN_BIOME, WORLD_NAME
    }

    private final Trigger trigger;

    // BELOW_Y / ABOVE_Y
    private final double yValue;

    // IN_BIOME
    private final List<String> biomes;

    // WORLD_NAME
    private final String worldName;

    // Effect to apply when trigger is true
    private final PassiveAbility effect;

    public Condition(Trigger trigger, double yValue, List<String> biomes,
                     String worldName, PassiveAbility effect) {
        this.trigger = trigger;
        this.yValue = yValue;
        this.biomes = biomes;
        this.worldName = worldName;
        this.effect = effect;
    }

    public Trigger getTrigger() { return trigger; }
    public double getYValue() { return yValue; }
    public List<String> getBiomes() { return biomes; }
    public String getWorldName() { return worldName; }
    public PassiveAbility getEffect() { return effect; }
}
