package com.rareventure.quietcraft;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Configurable parameters are all stored here for convenience and speed.
 */
public class Config {
    private static final String CONFIG_FILENAME = "config.yml";
    private static final String DEFAULT_CONFIG_RESOURCE_PATH = "/default_config.yml";
    public static final Material SOUL_SHARD_MATERIAL = Material.PRISMARINE_SHARD;
    public static final Material FAKE_SOUL_SHARD_RESULT = Material.WOOD_BUTTON;
    public static final String FAKE_SOUL_RESULT_DISPLAY_NAME = "Cracked soul gem";
    public static final List<String> FAKE_SOUL_RESULT_LORE = Arrays.asList("Thee dare taunt the gods????");
    public static final double OVERWORLD_TO_NETHER_DIST_RATIO = 8.;
    public static final double GOOD_PORTAL_STARTING_DIST = 5.;
    public static final double GOOD_PORTAL_DIST_MULTIPLER = 1.3;

    /**
     * Automatically created portals are protected from physics events for up to the given time
     * to prevent them being destroyed by the minecraft engine while we creating them.
     */
    // 10 seconds (in case of server lags causing portals to be destroyed)
    public static final long TIME_TO_CLEAR_STALE_PORTAL_AREAS_MS = 10000;
    public static MathUtil.RandomNormalParams OVERWORLD_SPAWN_RNP;

    public static String PORTAL_KEY_NAME;

    /**
     * The id of the nether world
     */
    public final static int NETHER_WORLD_ID = 1;
    /**
     * The percentage distance after max dist where the speaker can be identified, but the
     * message not heard
     */
    static double INAUDIBLE_DIST_RATIO_SQR;
    /**
     * The percentage distance after max dist where the speaker can't be identified, nor the
     * message not heard
     */
    static double INAUDIBLE_DIST_RATIO2_SQR;

    static MathUtil.RandomNormalParams NETHER_SPAWN_RNP;

    static int SOULS_PER_REBIRTH;

    static Material SOUL_MATERIAL_TYPE;

    static String SOUL_DISPLAY_NAME;

    static List<String> SOUL_LORE;

    public static String SOUL_SHARD_DISPLAY_NAME;
    public static List<String> SOUL_SHARD_LORE;


    static Material PORTAL_KEY_MATERIAL_TYPE;

    static List<String> PORTAL_KEY_LORE;
    /**
     * The maximum net number of souls that can leave a world per hour by teleporting
     */
    static float MAX_ALLOWED_SOUL_OUTFLOW_PER_HOUR;

    static int MAX_WORLDS;
    /**
     * The max distance from a portal for a player to have a portal key in order to
     * have the portal created when lit.
     */
    static int MAX_KEY_PORTAL_DISTANCE;

    static MathUtil.RandomNormalParams NETHER_PORTAL_WIDTH_PARAMS;
    /**
     * The ratio between height and width. It's important to make sure that
     * given the minimum width in NETHER_PORTAL_WIDTH_PARAMS, that this
     * value creates a valid height.
     */
    static double PORTAL_HEIGHT_TO_WIDTH;

    static MathUtil.RandomNormalParams OVERWORLD_PORTAL_WIDTH_PARAMS;

    /**
     * When deciding whether to recycle a world, the time a visited world must have no
     * log events before being recycled (with or without active players)
     * */
    static long MAX_RECYCLE_LAST_PLAYER_LOG_MS;

    public static List<String> WELCOME_MSG;
    public static List<String> SPAWN_SIGN_MSG;
    public static boolean OVERWORLD_GIVES_SOULS_ON_DEATH;

    /**
     * Every so often, every player on the server earns a soul shard while playing.
     */
    public static double SOUL_SHARD_GIVEAWAY_MINUTES;
    public static int MAX_SOULS_HELD_THROUGH_DEATH;
    public static String ENTER_NETHER_WARNING;
    public static String NETHER_DEATH_WITH_SOULS_MSG;
    public static double SOUL_DROP_MINUTES;

    public static void reloadConfig()
    {
        OVERWORLD_SPAWN_RNP =
                new MathUtil.RandomNormalParams(
                        getConfigInt("overworld_spawn.mean"),
                        getConfigInt("overworld_spawn.std"),
                        getConfigInt("overworld_spawn.min"),
                        getConfigInt("overworld_spawn.max"));

        PORTAL_KEY_NAME =
                getConfigString("portal_key_name");

        INAUDIBLE_DIST_RATIO_SQR =
                MathUtil.sqr(getConfigDouble("inaudible_dist_perc")/100.);

        INAUDIBLE_DIST_RATIO2_SQR =
                MathUtil.sqr(getConfigDouble("inaudible_dist_perc2")/100.);

        NETHER_SPAWN_RNP =
                new MathUtil.RandomNormalParams(
                        getConfigInt("nether_spawn.mean"),
                        getConfigInt("nether_spawn.std"),
                        getConfigInt("nether_spawn.min"),
                        getConfigInt("nether_spawn.max"));

        OVERWORLD_SPAWN_RNP =
                new MathUtil.RandomNormalParams(
                        getConfigInt("nether_spawn.mean"),
                        getConfigInt("nether_spawn.std"),
                        getConfigInt("nether_spawn.min"),
                        getConfigInt("nether_spawn.max"));

        SOULS_PER_REBIRTH = getConfigInt("souls_per_rebirth");

        SOUL_MATERIAL_TYPE =
                Material.getMaterial(getConfigString("soul_material_type"));

        SOUL_DISPLAY_NAME =
                getConfigString("soul_display_name");
        SOUL_LORE =
                getConfigStringList("soul_lore");

        SOUL_SHARD_DISPLAY_NAME =
                getConfigString("soul_shard_display_name");
        SOUL_SHARD_LORE =
                getConfigStringList("soul_shard_lore");

        PORTAL_KEY_MATERIAL_TYPE =
                Material.getMaterial(getConfigString("portal_key_material_type"));

        PORTAL_KEY_LORE =
                getConfigStringList("portal_key_lore");

        MAX_ALLOWED_SOUL_OUTFLOW_PER_HOUR =
                (float) getConfigDouble("max_allowed_soul_outflow_per_day")/24f;

        MAX_WORLDS =
                getConfigInt("max_worlds");

        MAX_KEY_PORTAL_DISTANCE =
                getConfigInt("max_key_portal_distance");

        NETHER_PORTAL_WIDTH_PARAMS =
                new MathUtil.RandomNormalParams(
                        getConfigInt("nether_portal_width.mean"),
                        getConfigInt("nether_portal_width.std"),
                        getConfigInt("nether_portal_width.min"),
                        getConfigInt("nether_portal_width.max"));

        PORTAL_HEIGHT_TO_WIDTH =
                getConfigDouble("portal_height_to_width");

        OVERWORLD_PORTAL_WIDTH_PARAMS =
                new MathUtil.RandomNormalParams(
                        getConfigInt("overworld_portal_width.mean"),
                        getConfigInt("overworld_portal_width.std"),
                        getConfigInt("overworld_portal_width.min"),
                        getConfigInt("overworld_portal_width.max"));


        MAX_RECYCLE_LAST_PLAYER_LOG_MS =
                (long)(getConfigDouble("max_recycle_last_player_log_days")
                        * 1000 * 3600 * 24);

        WELCOME_MSG =
                getConfigStringList("welcome_msg");

        SPAWN_SIGN_MSG =
                getConfigStringList("spawn_sign_msg");

        OVERWORLD_GIVES_SOULS_ON_DEATH = getConfigBoolean("overworld_gives_soul_on_death");
        SOUL_SHARD_GIVEAWAY_MINUTES = getConfigDouble("soul_shard_giveaway_minutes");

        MAX_SOULS_HELD_THROUGH_DEATH= getConfigInt("max_souls_held_through_death");

        ENTER_NETHER_WARNING =
                getConfigString("enter_nether_warning");

        NETHER_DEATH_WITH_SOULS_MSG =
                getConfigString("nether_death_with_souls_msg");

        SOUL_DROP_MINUTES =
                getConfigDouble("soul_drop_minutes");
    }

    private static void configTest(String s) {
        if(QuietCraftPlugin.cfg.contains(s))
        {
            QuietCraftPlugin.w("Overriding "+s+" to "+QuietCraftPlugin.cfg.get(s));
        }
        if(!QuietCraftPlugin.defaultCfg.contains(s))
            throw new IllegalStateException("Can't find config field "+s+" in "+DEFAULT_CONFIG_RESOURCE_PATH);
    }

    private static int getConfigInt(String s) {
        configTest(s);
        return QuietCraftPlugin.cfg.getInt(s, QuietCraftPlugin.defaultCfg.getInt(s));
    }

    public static double getConfigDouble(String s) {
        configTest(s);
        return QuietCraftPlugin.cfg.getDouble(s, QuietCraftPlugin.defaultCfg.getDouble(s));
    }

    private static boolean getConfigBoolean(String s) {
        configTest(s);
        return QuietCraftPlugin.cfg.getBoolean(s, QuietCraftPlugin.defaultCfg.getBoolean(s));
    }

    private static String getConfigString(String s) {
        configTest(s);
        return QuietCraftPlugin.cfg.getString(s, QuietCraftPlugin.defaultCfg.getString(s));
    }

    private static List<String> getConfigStringList(String s) {
        configTest(s);

        List<String> r = QuietCraftPlugin.cfg.getStringList(s);
        if(r.isEmpty())
            return QuietCraftPlugin.defaultCfg.getStringList(s);

        return r;
    }

    public static void saveConfig(QuietCraftPlugin qcp) throws IOException {
        File configFile = new File(qcp.getDataFolder(), CONFIG_FILENAME);
        qcp.getConfig().save(configFile);
    }

    static void readConfigFile(QuietCraftPlugin qcp) {
        qcp.cfg = new YamlConfiguration();
        qcp.defaultCfg = new YamlConfiguration();

        //we use the default config as a fallback and config.yml for overrides
        try {
            qcp.defaultCfg.load(new InputStreamReader(Config.class.
                    getResourceAsStream(DEFAULT_CONFIG_RESOURCE_PATH)));

            File configFile = new File(qcp.getDataFolder(), CONFIG_FILENAME);
            if (!configFile.exists()) {
                qcp.i("Creating config file: %s", configFile);
                new FileOutputStream(configFile).close();
            } else
                qcp.cfg.load(configFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }

        reloadConfig();
    }
}
