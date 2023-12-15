package io.github.ardryck.mining.commands;

import com.gmail.nossr50.api.ExperienceAPI;
import io.github.ardryck.mining.MiningPlugin;
import io.github.ardryck.mining.utils.ActionBarUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class NetherMineCommand extends Command {

    public NetherMineCommand() {
        super("minanether");
    }
    @Override
    public boolean execute(CommandSender sender, String lb, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player minePlayer = (Player) sender;

        int level = ExperienceAPI.getLevel(minePlayer, "mining");

        if (level < 100) {
            minePlayer.sendMessage("§cVocê deve possuir no minímo o nível 100 de mineração para entrar nessa mina.");
            return false;
        }
        if (MiningPlugin.COMMAND_COOLDOWN.getIfPresent(minePlayer.getUniqueId()) != null && MiningPlugin.COMMAND_COOLDOWN.getIfPresent(minePlayer.getUniqueId()).equalsIgnoreCase("mina_nether")) {
            sender.sendMessage("§cAguarde alguns segundos para utilizar este comando novamente.");
            return false;
        }

        World mineWorld = Bukkit.getWorld("mina_nether");
        int x = new Random().nextInt(100);
        int z = new Random().nextInt(100);
        int y = mineWorld.getHighestBlockYAt(x, z);

        minePlayer.teleport(new Location(mineWorld, x, y, -z));

        minePlayer.playSound(minePlayer.getLocation(), Sound.LEVEL_UP, 1F, 1F);

        minePlayer.sendTitle("§5§lMINA NETHER", "§fBoa sorte aventureiro");

        ActionBarUtil.sendActionBar(minePlayer, "§aDesejamos a você uma ótima mineração!");


        minePlayer.sendMessage("§6§elSOBRE A MINERAÇÃO NETHER!");
        minePlayer.sendMessage("§e ");
        minePlayer.sendMessage("§e * Este mundo é resetado toda vez que o servidor reinicia;");
        minePlayer.sendMessage("§e * Não guarde seus itens neste mundo;");
        minePlayer.sendMessage("§e * não construa bases neste mundo.");
        minePlayer.sendMessage("§e ");
        minePlayer.sendMessage("§e * Aqui o pvp é ativado sempre.");
        minePlayer.sendMessage("");

        MiningPlugin.COMMAND_COOLDOWN.put(minePlayer.getUniqueId(), "mina_nether");
        return true;
    }
}
