package moe.luoluo;

import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JCompositeCommand;

public final class commands extends JCompositeCommand {
    public commands() {
        super(luohyp.INSTANCE, "hypixel","hyp");
        // ...
    }

    // ...
    @SubCommand("bw")
    @Description("查询玩家的起床战争数据")
    public void bw(CommandContext context, String player, String type) {
        System.out.println(player+"的bw数据如下2arg");
    }
    @SubCommand("bw")
    @Description("查询玩家的起床战争数据")
    public void bw(CommandContext context, String player) {
        System.out.println(player+"的bw数据如下1arg");
    }
}
