package com.fantasyraces.managers;

import com.fantasyraces.FantasyRaces;
import com.fantasyraces.models.Condition;
import com.fantasyraces.models.PassiveAbility;
import com.fantasyraces.models.Race;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class ConditionManager {

    private static final int TICK_INTERVAL = 40; // Every 2 seconds
    private static final int EFFECT_DURATION = 60; // 3 seconds (with 2s tick = always active)

    private final FantasyRaces plugin;
    private BukkitTask task;

    public ConditionManager(FantasyRaces plugin) {
        this.plugin = plugin;
    }

    public void startTask() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, TICK_INTERVAL, TICK_INTERVAL);
    }

    public void stopTask() {
        if (task != null) task.cancel();
    }

    private void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            String raceId = plugin.getPlayerDataManager().getPlayerRace(uuid);
            if (raceId == null) continue;

            Race race = plugin.getRaceManager().getRace(raceId);
            if (race == null) continue;

            // Apply passives
            for (PassiveAbility passive : race.getPassives()) {
                applyEffect(player, passive);
            }

            // Evaluate conditions
            for (Condition condition : race.getConditions()) {
                if (evaluateTrigger(player, condition)) {
                    applyEffect(player, condition.getEffect());
                }
            }
        }
    }

    private boolean evaluateTrigger(Player player, Condition condition) {
        return switch (condition.getTrigger()) {
            case BELOW_Y      -> player.getLocation().getY() < condition.getYValue();
            case ABOVE_Y      -> player.getLocation().getY() > condition.getYValue();
            case IS_DAYTIME   -> {
                long time = player.getWorld().getTime();
                yield time >= 0 && time < 13000;
            }
            case IS_NIGHTTIME -> {
                long time = player.getWorld().getTime();
                yield time >= 13000 && time < 24000;
            }
            case IN_WATER     -> player.isInWater();
            case ON_FIRE      -> player.getFireTicks() > 0;
            case IN_BIOME     -> {
                String biomeKey = player.getLocation().getBlock().getBiome().name();
                yield condition.getBiomes() != null &&
                        condition.getBiomes().stream()
                                .anyMatch(b -> b.equalsIgnoreCase(biomeKey));
            }
            case WORLD_NAME   -> {
                String worldName = condition.getWorldName();
                yield worldName != null &&
                        player.getWorld().getName().equalsIgnoreCase(worldName);
            }
        };
    }

    private void applyEffect(Player player, PassiveAbility ability) {
        PotionEffectType pet = ability.toBukkitType();
        if (pet == null) return;

        // Don't re-apply if already at equal or stronger level
        PotionEffect existing = player.getPotionEffect(pet);
        if (existing != null && existing.getAmplifier() >= ability.getAmplifier()
                && existing.getDuration() > 20) return;

        player.addPotionEffect(new PotionEffect(pet, EFFECT_DURATION, ability.getAmplifier(), true, false, false));
    }

    /**
     * Removes all passive effects associated with a race from a player.
     */
    public void removeRaceEffects(Player player, Race race) {
        if (race == null) return;

        for (PassiveAbility passive : race.getPassives()) {
            player.removePotionEffect(passive.toBukkitType());
        }
        for (Condition condition : race.getConditions()) {
            player.removePotionEffect(condition.getEffect().toBukkitType());
        }
    }
}
