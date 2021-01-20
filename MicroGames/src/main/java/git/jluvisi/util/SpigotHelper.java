package git.jluvisi.util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This class is used to convert, make, or change certain values or items in the
 * Spigot API with one single method.
 */
public class SpigotHelper {

    public static ItemStack compactItem(Material m, String displayName, int amount) {
        ItemStack stack = new ItemStack(m, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack compactItem(Material m, String displayName, int amount, List<String> lore) {
        ItemStack stack = new ItemStack(m, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack compactItem(Material m, String displayName, int amount, List<String> lore,
            Enchantment[] enchants, int[] levels, boolean unbreakable) {
        ItemStack stack = new ItemStack(m, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setUnbreakable(unbreakable);
        for (int i = 0; i < enchants.length; i++) {
            stack.addUnsafeEnchantment(enchants[i], levels[i]);
        }
        stack.setItemMeta(meta);
        return stack;

    }

}
