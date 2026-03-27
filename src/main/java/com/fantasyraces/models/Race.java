package com.fantasyraces.models;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.List;

public class Race {

    private final String id;
    private final String displayName;
    private final String description;
    private final String iconMaterial;
    private final String luckPermsGroup;

    // Stats
    private final double maxHealthBonus;   // hearts bonus
    private final double speedModifier;    // additive fraction (e.g. 0.05 = +5%)
    private final double damageModifier;   // additive fraction
    private final double knockbackResistance;

    // Abilities
    private final List<PassiveAbility> passives;
    private final List<Condition> conditions;

    public Race(String id, String displayName, String description, String iconMaterial,
                String luckPermsGroup, double maxHealthBonus, double speedModifier,
                double damageModifier, double knockbackResistance,
                List<PassiveAbility> passives, List<Condition> conditions) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.iconMaterial = iconMaterial;
        this.luckPermsGroup = luckPermsGroup;
        this.maxHealthBonus = maxHealthBonus;
        this.speedModifier = speedModifier;
        this.damageModifier = damageModifier;
        this.knockbackResistance = knockbackResistance;
        this.passives = passives;
        this.conditions = conditions;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getIconMaterial() { return iconMaterial; }
    public String getLuckPermsGroup() { return luckPermsGroup; }
    public double getMaxHealthBonus() { return maxHealthBonus; }
    public double getSpeedModifier() { return speedModifier; }
    public double getDamageModifier() { return damageModifier; }
    public double getKnockbackResistance() { return knockbackResistance; }
    public List<PassiveAbility> getPassives() { return passives; }
    public List<Condition> getConditions() { return conditions; }
}
