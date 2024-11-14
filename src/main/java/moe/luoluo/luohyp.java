package moe.luoluo;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

public final class luohyp extends JavaPlugin {
    public static final luohyp INSTANCE = new luohyp();

    private luohyp() {
        super(new JvmPluginDescriptionBuilder("moe.luoluo.luohyp", "1.0.8")
                .name("luphyp")
                .author("MCluoluo")
                .build());
    }

    @Override
    public void onEnable() {
        reloadPluginConfig(config.INSTANCE);
        reloadPluginData(Data.INSTANCE);

        getLogger().info("luohyp enabled");

        CommandManager.INSTANCE.registerCommand(new Hypixel(), false);
        CommandManager.INSTANCE.registerCommand(new MCSkin(), false);
    }
}