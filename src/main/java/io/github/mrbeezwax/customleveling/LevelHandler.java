package io.github.mrbeezwax.customleveling;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class implements the listener. It checks for when a player
 * gains xp.
 *
 * @author John Harrison
 */
public class LevelHandler implements Listener {
    private Plugin plugin;

    /**
     * Gets the main instance of the plugin.
     *
     * @param pluginIn is the main instance of the plugin.
     */
    public LevelHandler(Plugin pluginIn) {
        plugin = pluginIn;
    }

    /**
     * Checks to see if a player gets xp.
     *
     * @param event is the PlayerExpChange event
     */
    @EventHandler
    public void onXpGain(PlayerExpChangeEvent event) {

        // Takes in the levels defined in the plugin
        ArrayList<String> levelList = new ArrayList<>(plugin.getConfig().getStringList("levels"));

        // If there are levels defined in the config
        if (!levelList.get(0).equalsIgnoreCase("none")) {
            Player player = event.getPlayer();
            for (String s : levelList) {
                String[] levels = s.split(" ");
                int levelCompare = Integer.parseInt(levels[0]) - 1;
                int xpAmount = Integer.parseInt(levels[1]);
                int playerLvl = player.getLevel();
                if (playerLvl == levelCompare) {
                    float xpBarCurrent = player.getExp() * xpAmount;
                    float xpBarNew = (xpBarCurrent + event.getAmount()) / xpAmount;
                    /* Old Code
                    event.setAmount(0);
                    player.setExp(xpBarNew);
                    */
                    event.setAmount(0);
                    int levelEarned = (int) xpBarNew;
                    float remainingXP = xpBarNew - levelEarned;
                    player.setExp(remainingXP);
                    player.giveExpLevels(levelEarned);
                    return;
                }
            }

            // If the player reaches the highest level.
            player.sendMessage(ChatColor.GREEN + "[Custom Leveling]" + ChatColor.WHITE + " You have reached the highest level!");
            event.setAmount(0);
        }
    }


}
