package com.rareventure.quietcraft;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

//TODO 2 on player death player keeps items
/**
 * Manages craft events and creating special receipes, etc.
 */
public class CraftManager {
    private final ShapedRecipe soulRecipe;
    private final ItemStack fakeSoulRecipeResult;
    private QuietCraftPlugin qcp;

    public CraftManager(QuietCraftPlugin qcp) {
        this.qcp = qcp;

        fakeSoulRecipeResult = WorldUtil.createSpecialItem(Config.FAKE_SOUL_SHARD_RESULT,
                Config.FAKE_SOUL_RESULT_DISPLAY_NAME,
                Config.FAKE_SOUL_RESULT_LORE,1);

        soulRecipe = new ShapedRecipe(fakeSoulRecipeResult);
        soulRecipe.shape("%%%","%%%","%%%");
        soulRecipe.setIngredient('%', Config.SOUL_SHARD_MATERIAL);
        qcp.getServer().addRecipe(soulRecipe);
    }

    public void onPrepareItemCraftEvent(PrepareItemCraftEvent event) {
        Recipe r = event.getRecipe();
        if(r instanceof ShapedRecipe)
        {
            ShapedRecipe sr = (ShapedRecipe) r;

            if(isSoulRecipe(event.getInventory().getContents()))
            {
                qcp.i("Player "+
                    Util.castAndCall(Player.class, event.getInventory().getHolder(),
                        p -> p.getName(), "*not player*")+
                        " created a soul");
                event.getInventory().setResult(WorldUtil.createSoulGem(1));
            }
            else if(isRecipeForbidden(event.getInventory().getContents()))
            {
                event.getInventory().setResult(null);
            }
        }

    }

    /**
     * Returns true if the recipe contains our special objects so we can disallow creation of normal stuff
     * with our special stuff.
     */
    private boolean isRecipeForbidden(ItemStack [] items) {
        for(int i = 1; i < items.length; i++)
        {
            if(items[i].getItemMeta() == null)
                continue;
            String displayName = items[i].getItemMeta().getDisplayName();
            if(displayName == null)
                continue;
            if(displayName.equals(Config.SOUL_SHARD_DISPLAY_NAME)
                || displayName.equals(Config.SOUL_DISPLAY_NAME))
                return true;

        }

        return false;
    }

    private boolean isSoulRecipe(ItemStack[] items) {
        //the first item is the result (a soul diamond)
        if(items.length != 10)
            return false;

//        if(!items[0].equals(fakeSoulRecipeResult))
//            return false;

        for(int i = 1; i < items.length; i++)
        {
            Material m = items[i].getType();
            if(m != Config.SOUL_SHARD_MATERIAL)
                return false;
            if(items[i].getItemMeta() == null)
                return false;
            String displayName = items[i].getItemMeta().getDisplayName();
            if(displayName == null || !displayName.equals(Config.SOUL_SHARD_DISPLAY_NAME))
                return false;
        }

        return true;
    }
}
