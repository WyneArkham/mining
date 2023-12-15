package io.github.ardryck.mining;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.ardryck.mining.commands.MineCommand;
import io.github.ardryck.mining.commands.NetherMineCommand;
import io.github.ardryck.mining.listeners.MineListener;
import lombok.Getter;
import io.github.ardryck.mining.listeners.PlayerListener;
import org.bukkit.*;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MiningPlugin extends JavaPlugin {

    @Getter
    public static MiningPlugin instance;

    public static Cache<UUID, String> COMMAND_COOLDOWN;

    @Override
    public void onLoad() {
        File mineDirectory = new File("mina");
        if (mineDirectory.exists())deleteFile(mineDirectory);

        File netherDirectory = new File("mina_nether");
        if (netherDirectory.exists())deleteFile(netherDirectory);

        COMMAND_COOLDOWN = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).concurrencyLevel(2).build();
    }

    @Override
    public void onEnable() {
        getLogger().info("[MinePlugin] inicializando o plugin...");
        instance = this;

        getLogger().info("Mundo da Mina sendo gerado...");

        WorldCreator creator = new WorldCreator("mina");

        creator.type(WorldType.CUSTOMIZED).generatorSettings("{\"coordinateScale\":684.412,\"heightScale\":684.412,\"lowerLimitScale\":512.0,\"upperLimitScale\":512.0,\"depthNoiseScaleX\":200.0,\"depthNoiseScaleZ\":200.0,\"depthNoiseScaleExponent\":0.5,\"mainNoiseScaleX\":80.0,\"mainNoiseScaleY\":160.0,\"mainNoiseScaleZ\":80.0,\"baseSize\":8.5,\"stretchY\":12.0,\"biomeDepthWeight\":1.0,\"biomeDepthOffset\":0.0,\"biomeScaleWeight\":1.0,\"biomeScaleOffset\":0.0,\"seaLevel\":63,\"useCaves\":true,\"useDungeons\":false,\"dungeonChance\":8,\"useStrongholds\":false,\"useVillages\":false,\"useMineShafts\":false,\"useTemples\":false,\"useMonuments\":false,\"useRavines\":true,\"useWaterLakes\":true,\"waterLakeChance\":4,\"useLavaLakes\":true,\"lavaLakeChance\":80,\"useLavaOceans\":false,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":4,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":256,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":256,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":1,\"coalCount\":0,\"coalMinHeight\":0,\"coalMaxHeight\":0,\"ironSize\":1,\"ironCount\":0,\"ironMinHeight\":0,\"ironMaxHeight\":0,\"goldSize\":1,\"goldCount\":0,\"goldMinHeight\":0,\"goldMaxHeight\":0,\"redstoneSize\":1,\"redstoneCount\":0,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":0,\"diamondSize\":1,\"diamondCount\":0,\"diamondMinHeight\":0,\"diamondMaxHeight\":0,\"lapisSize\":1,\"lapisCount\":0,\"lapisCenterHeight\":0,\"lapisSpread\":0,\"emeraldSize\":1,\"emeraldCount\":0,\"emeraldCenterHeight\":0,\"emeraldSpread\":0}");
        creator.generateStructures(false);

        World world = creator.createWorld();

        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("doDayLightCycle", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doMobLoot", "false");
        world.setTime(60000);
        world.setThunderDuration(0);
        world.setWeatherDuration(0);
        world.setAutoSave(false);

        getLogger().info("Mundo da Mina foi gerado com sucesso.");

        getLogger().info("Mundo da Mina Nether sendo gerado...");

        WorldCreator nether = new WorldCreator("mina_nether");
        nether.environment(World.Environment.NETHER);
        nether.generateStructures(false);
        nether.createWorld();

        getLogger().info("Mundo da Mina Nether foi gerado com sucesso.");

        registerListeners();
        registerCommands();

        getLogger().info("[MinePlugin] plugin inicializado.");
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new MineListener(), this);
        pm.registerEvents(new PlayerListener(), this);
    }
    private void registerCommands() {
        CommandMap map = ((CraftServer) Bukkit.getServer()).getCommandMap();

        map.register("mina", new MineCommand());
        map.register("minanether", new NetherMineCommand());
    }

    private void deleteFile(File file) {
        if (file.isDirectory()) {
            String[] list = file.list();
            assert list != null;
            for (String s : list) {
                deleteFile(new File(file, s));
            }
        }
        file.delete();
    }
}
