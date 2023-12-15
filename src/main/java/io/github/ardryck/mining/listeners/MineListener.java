package io.github.ardryck.mining.listeners;

import io.github.ardryck.mining.chance.Chance;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MineListener implements Listener {

    private final Random random;

    {
        random = new Random();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        int y = block.getY();

        boolean extreme_hills = block.getBiome().name().toLowerCase().startsWith("extreme_hills");

        if (player.getWorld().getName().equalsIgnoreCase("mina")) {
            if (block.getType() == Material.STONE) {
                block.getDrops().clear();
                this.sendDrop(player, extreme_hills ? this.setDrop(3, y) : (block.getY() >= 13 ? this.setDrop(1, y) : this.setDrop(2, y)), block.getLocation());
            }
        } else if (player.getWorld().getName().equalsIgnoreCase("mina_nether")){
            if (block.getType() == Material.QUARTZ_ORE){
                block.getDrops().clear();
            }

            if (block.getType() == Material.NETHERRACK) {
                block.getDrops().clear();
                this.sendDrop(player, this.setDrop(3, 0), block.getLocation());
            }
        }
    }

    private ItemStack setDrop(int type, int y) {
        switch (type) {
            case 1: {
                if (this.percentChance(Chance.GOLD.getAbove())) {
                    return new ItemStack(Material.GOLD_ORE);
                }
                if (this.percentChance(Chance.IRON.getAbove())) {
                    return new ItemStack(Material.IRON_ORE);
                }
                if (this.percentChance(Chance.COAL.getAbove())) {
                    return new ItemStack(Material.COAL);
                }
                break;
            }
            case 2: {
                if (this.percentChance(Chance.DIAMOND.getBellow())) {
                    return new ItemStack(Material.DIAMOND);
                }
                if (this.percentChance(Chance.GOLD.getBellow())) {
                    return new ItemStack(Material.GOLD_ORE);
                }
                if (this.percentChance(Chance.IRON.getBellow())) {
                    return new ItemStack(Material.IRON_ORE);
                }
                if (this.percentChance(Chance.REDSTONE.getBellow())) {
                    return new ItemStack(Material.REDSTONE);
                }
                if (this.percentChance(Chance.LAPIS.getBellow())) {
                    return new ItemStack(Material.INK_SACK, 1, (short) 4);
                }
                if (this.percentChance(Chance.COAL.getBellow())) {
                    return new ItemStack(Material.COAL);
                }
                break;
            }
            case 3: {
                if (this.percentChance(Chance.EMERALD.getExtreme_hills())) {
                    if (y >= 13) setDrop(type, y);
                    return new ItemStack(Material.EMERALD);
                }
                if (this.percentChance(Chance.DIAMOND.getExtreme_hills())) {
                    if (y >= 13) setDrop(type, y);
                    return new ItemStack(Material.DIAMOND);
                }
                if (this.percentChance(Chance.GOLD.getExtreme_hills())) {
                    return new ItemStack(Material.GOLD_ORE);
                }
                if (this.percentChance(Chance.IRON.getExtreme_hills())) {
                    return new ItemStack(Material.IRON_ORE);
                }
                if (this.percentChance(Chance.REDSTONE.getExtreme_hills())) {
                    if (y >= 13) setDrop(type, y);
                    return new ItemStack(Material.REDSTONE);
                }
                if (this.percentChance(Chance.LAPIS.getExtreme_hills())) {
                    if (y >= 13) setDrop(type, y);
                    return new ItemStack(Material.INK_SACK, 1, (short) 4);
                }
                if (this.percentChance(Chance.COAL.getExtreme_hills())) {
                    return new ItemStack(Material.COAL);
                }
                break;
            }
        }
        return new ItemStack(Material.AIR);
    }

    protected boolean percentChance(double percent) {
        if (percent < 0.0 || percent > 100.0) {
            throw new IllegalArgumentException("The percentage cannot be greater than 100 or less than 0");
        }
        double result = this.random.nextDouble() * 100.0;
        return result <= percent;
    }

    private void sendDrop(Player player, ItemStack drop, Location location) {
        try {
            ItemStack item = player.getItemInHand();
            if (item == null || item.getType() == Material.AIR) {
                return;
            }
            if (drop == null || drop.getType() == Material.AIR) {
                return;
            }
            int xp = 0;
            if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                int levelEnchant = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
                int level = levelEnchant - 2;

                if (level <= 0)
                    level = 2;

                drop.setAmount(1);
                xp = 0;
                if (drop.getType() == Material.EMERALD || drop.getType() == Material.DIAMOND) {
                    int a = this.random.nextInt(levelEnchant) + 1;

                    if (a < level)
                        a = level;

                    drop.setAmount(a);
                    xp = 5;
                } else if (drop.getType() == Material.COAL) {
                    int a = this.random.nextInt(levelEnchant) + 1;

                    if (a < level)
                        a = level;

                    drop.setAmount(a);
                    xp = 1;
                } else if (drop.getType() == Material.COBBLESTONE) {
                    drop = null;
                } else if (drop.getType() == Material.LAPIS_BLOCK) {
                    xp = 4;
                    drop = new ItemStack(Material.INK_SACK, 1, (short) 4);
                    int a = this.random.nextInt(levelEnchant) + 1;

                    if (a < level)
                        a = level;

                    drop.setAmount(2 * a + 1);
                } else if (drop.getType() == Material.REDSTONE) {
                    xp = 3;
                    int a = this.random.nextInt(levelEnchant) + 1;

                    if (a < level)
                        a = level;

                    drop.setAmount(2 * a + 1);
                }
            } else if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                drop.setAmount(1);
                xp = 0;
                if (drop.getType() == Material.EMERALD) {
                    drop = new ItemStack(Material.EMERALD_ORE);
                } else if (drop.getType() == Material.DIAMOND) {
                    drop = new ItemStack(Material.DIAMOND_ORE);
                } else if (drop.getType() == Material.COAL) {
                    drop = new ItemStack(Material.COAL_ORE);
                } else if (drop.getType() == Material.LAPIS_BLOCK) {
                    drop = new ItemStack(Material.LAPIS_ORE);
                } else if (drop.getType() == Material.REDSTONE) {
                    drop = new ItemStack(Material.REDSTONE_ORE);
                }
            } else if (item.containsEnchantment(Enchantment.SILK_TOUCH) && !item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
                drop.setAmount(1);
                xp = 0;
                if (drop.getType() == Material.EMERALD) {
                    drop = new ItemStack(Material.EMERALD_ORE);
                } else if (drop.getType() == Material.DIAMOND) {
                    drop = new ItemStack(Material.DIAMOND_ORE);
                } else if (drop.getType() == Material.COAL) {
                    drop = new ItemStack(Material.COAL_ORE);
                } else if (drop.getType() == Material.LAPIS_BLOCK) {
                    drop = new ItemStack(Material.LAPIS_ORE);
                } else if (drop.getType() == Material.REDSTONE) {
                    drop = new ItemStack(Material.REDSTONE_ORE);
                }
            } else {
                drop.setAmount(1);
                if (drop.getType() == Material.EMERALD || drop.getType() == Material.DIAMOND) {
                    xp = 5;
                } else if (drop.getType() == Material.COAL) {
                    xp = 1;
                } else if (drop.getType() == Material.LAPIS_BLOCK) {
                    xp = 4;
                    drop = new ItemStack(Material.INK_SACK, 1, (short) 4);
                    drop.setAmount(3);
                } else if (drop.getType().equals(Material.COBBLESTONE)) {
                    drop = null;
                } else if (drop.getType() == Material.REDSTONE) {
                    xp = 3;
                    drop.setAmount(2);
                }
            }
            if (player.getInventory().firstEmpty() == -1) {
                if (random.nextInt(100) <= 30)
                    player.sendMessage("§cSeu inventário está lotado.");
                location.getWorld().dropItemNaturally(location, drop);
            } else {
                player.getInventory().addItem(drop);
            }
            if (xp > 0) {
                player.giveExp(xp);
            }
        } catch (IllegalArgumentException ignored) {
        }
    }
}

