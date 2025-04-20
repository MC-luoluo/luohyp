package moe.luoluo;

import moe.luoluo.util.Task;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class luohyp extends JavaPlugin {
    public static final luohyp INSTANCE = new luohyp();

    private luohyp() {
        super(new JvmPluginDescriptionBuilder("moe.luoluo.luohyp", "1.1.3")
                .name("luphyp")
                .author("MCluoluo")
                .build());
    }

    @Override
    public void onEnable() {
        reloadPluginConfig(Config.INSTANCE);

        getLogger().info("luohyp enabled");

        Task.start();

        CommandManager.INSTANCE.registerCommand(new Hypixel(), false);
        CommandManager.INSTANCE.registerCommand(new MCSkin(), false);
    }
}