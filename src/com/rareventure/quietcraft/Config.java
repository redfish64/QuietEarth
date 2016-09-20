package com.rareventure.quietcraft;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Configurable parameters are all stored here for convenience and speed.
 */
public class Config {
    private static final String CONFIG_FILENAME = "config.yml";
    private static final String DEFAULT_CONFIG_RESOURCE_PATH = "/default_config.yml";
    public static MathUtil.RandomNormalParams OVERWORLD_SPAWN_RNP;

    public static String PORTAL_KEY_NAME;
    /**
     * When portals are destroyed and exploded, this specifies the ratio of width of portal
     * to explosion power (as defined by World.createExplosion())
     */
    public static MathUtil.RandomNormalParams PORTAL_EXPLOSION_SIZE_PERC_RNP;

    public static MathUtil.RandomNormalParams NETHER_PORTAL_RNP;
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

    public static void reloadConfig()
    {
        OVERWORLD_SPAWN_RNP =
                new MathUtil.RandomNormalParams(
                        QuietCraftPlugin.cfg.getInt("overworld_spawn.mean"),
                        QuietCraftPlugin.cfg.getInt("overworld_spawn.std"),
                        QuietCraftPlugin.cfg.getInt("overworld_spawn.min"),
                        QuietCraftPlugin.cfg.getInt("overworld_spawn.max"));

        PORTAL_KEY_NAME =
                QuietCraftPlugin.cfg.getString("portal_key_name");

        PORTAL_EXPLOSION_SIZE_PERC_RNP =
                new MathUtil.RandomNormalParams(
                        QuietCraftPlugin.cfg.getDouble("portal_explosion_perc.mean"),
                        QuietCraftPlugin.cfg.getDouble("portal_explosion_perc.std"),
                        QuietCraftPlugin.cfg.getDouble("portal_explosion_perc.min"),
                        QuietCraftPlugin.cfg.getDouble("portal_explosion_perc.max"));

        NETHER_PORTAL_RNP =
                new MathUtil.RandomNormalParams(
                        QuietCraftPlugin.cfg.getInt("nether_portal.mean"),
                        QuietCraftPlugin.cfg.getInt("nether_portal.std"),
                        QuietCraftPlugin.cfg.getInt("nether_portal.min"),
                        QuietCraftPlugin.cfg.getInt("nether_portal.max"));

        INAUDIBLE_DIST_RATIO_SQR =
                MathUtil.sqr(QuietCraftPlugin.cfg.getDouble("inaudible_dist_perc")/100.);

        INAUDIBLE_DIST_RATIO2_SQR =
                MathUtil.sqr(QuietCraftPlugin.cfg.getDouble("inaudible_dist_perc2")/100.);

        NETHER_SPAWN_RNP =
                new MathUtil.RandomNormalParams(
                        QuietCraftPlugin.cfg.getInt("nether_spawn.mean"),
                        QuietCraftPlugin.cfg.getInt("nether_spawn.std"),
                        QuietCraftPlugin.cfg.getInt("nether_spawn.min"),
                        QuietCraftPlugin.cfg.getInt("nether_spawn.max"));

        SOULS_PER_REBIRTH = QuietCraftPlugin.cfg.getInt("souls_per_rebirth");

        SOUL_MATERIAL_TYPE =
                Material.getMaterial(QuietCraftPlugin.cfg.getString("soul_material_type"));

        SOUL_DISPLAY_NAME =
                QuietCraftPlugin.cfg.getString("soul_display_name");

        SOUL_LORE =
                QuietCraftPlugin.cfg.getStringList("soul_lore");

        PORTAL_KEY_MATERIAL_TYPE =
                Material.getMaterial(QuietCraftPlugin.cfg.getString("portal_key_material_type"));

        PORTAL_KEY_LORE =
                QuietCraftPlugin.cfg.getStringList("portal_key_lore");

        MAX_ALLOWED_SOUL_OUTFLOW_PER_HOUR =
                (float) QuietCraftPlugin.cfg.getDouble("max_allowed_soul_outflow_per_day")/24f;

        MAX_WORLDS =
                QuietCraftPlugin.cfg.getInt("max_worlds");

        MAX_KEY_PORTAL_DISTANCE =
                QuietCraftPlugin.cfg.getInt("max_key_portal_distance");

        NETHER_PORTAL_WIDTH_PARAMS =
                new MathUtil.RandomNormalParams(
                        QuietCraftPlugin.cfg.getInt("nether_portal_width.mean"),
                        QuietCraftPlugin.cfg.getInt("nether_portal_width.std"),
                        QuietCraftPlugin.cfg.getInt("nether_portal_width.min"),
                        QuietCraftPlugin.cfg.getInt("nether_portal_width.max"));

        PORTAL_HEIGHT_TO_WIDTH =
                QuietCraftPlugin.cfg.getDouble("portal_height_to_width");

        OVERWORLD_PORTAL_WIDTH_PARAMS =
                new MathUtil.RandomNormalParams(
                        QuietCraftPlugin.cfg.getInt("overworld_portal_width.mean"),
                        QuietCraftPlugin.cfg.getInt("overworld_portal_width.std"),
                        QuietCraftPlugin.cfg.getInt("overworld_portal_width.min"),
                        QuietCraftPlugin.cfg.getInt("overworld_portal_width.max"));


        MAX_RECYCLE_LAST_PLAYER_LOG_MS =
                (long)(QuietCraftPlugin.cfg.getDouble("max_recycle_last_player_log_days")
                        * 1000 * 3600 * 24);

        WELCOME_MSG =
                QuietCraftPlugin.cfg.getStringList("welcome_msg");

        SPAWN_SIGN_MSG =
                QuietCraftPlugin.cfg.getStringList("spawn_sign_msg");

        OVERWORLD_GIVES_SOULS_ON_DEATH = QuietCraftPlugin.cfg.getBoolean("overworld_gives_soul_on_death",false);

    }

    public static void saveConfig(QuietCraftPlugin qcp) throws IOException {
        File configFile = new File(qcp.getDataFolder(), CONFIG_FILENAME);
        qcp.getConfig().save(configFile);
    }

    static void readConfigFile(QuietCraftPlugin qcp) {
        //look for normal config file first. If it doesn't exist, read default hardcoded one
        try {
            File configFile = new File(qcp.getDataFolder(), CONFIG_FILENAME);
            if (!configFile.exists()) {
                qcp.getConfig().load(new InputStreamReader(Config.class.
                        getResourceAsStream(DEFAULT_CONFIG_RESOURCE_PATH)));
                qcp.getConfig().save(configFile);
            } else
                qcp.getConfig().load(configFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }

        qcp.cfg = qcp.getConfig();

        reloadConfig();
    }
}
