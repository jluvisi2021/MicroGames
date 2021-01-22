package git.jluvisi.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * This class is used to convert, make, or change certain values or items in the
 * Spigot API with one single method.
 */
public class SpigotHelper {

    /**
     * Returns if a material as a string is null
     *
     * <pre>
     * return Material.getMaterial(s) != null;
     * </pre>
     *
     * @param s
     * @return
     */
    public static boolean isValidMaterial(String s) {
        return Material.getMaterial(s) != null;
    }

    /**
     * Returns an item stack with a set display name and amount.
     *
     * @param m
     * @param displayName
     * @param amount
     * @return
     */
    public static ItemStack compactItem(Material m, String displayName, int amount) {
        ItemStack stack = new ItemStack(m, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Returns an item stack with a display name, amount, and a specified lore.
     *
     * @param m
     * @param displayName
     * @param amount
     * @param lore
     * @return
     */
    public static ItemStack compactItem(Material m, String displayName, int amount, List<String> lore) {
        ItemStack stack = new ItemStack(m, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    /**
     * Returns an ItemStack with a specified name, amount, lore, enchants, and if it
     * is unbreakable.
     *
     * @param m
     * @param displayName
     * @param amount
     * @param lore
     * @param enchants
     * @param unbreakable
     * @return
     */
    public static ItemStack compactItem(Material m, String displayName, int amount, List<String> lore,
            HashMap<Enchantment, Integer> enchants, boolean unbreakable) {
        ItemStack stack = new ItemStack(m, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setUnbreakable(unbreakable);

        final Iterator<Entry<Enchantment, Integer>> it = enchants.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Enchantment, Integer> pair = (Map.Entry<Enchantment, Integer>) it.next();
            stack.addUnsafeEnchantment(pair.getKey(), pair.getValue());
            it.remove();
        }

        stack.setItemMeta(meta);
        return stack;

    }

}
