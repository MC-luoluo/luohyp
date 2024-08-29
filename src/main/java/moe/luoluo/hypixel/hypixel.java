package moe.luoluo.hypixel;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;

public class hypixel {
    public static MessageChain hypixel(CommandSender sender, String player, String type) {
        MessageChain at = MiraiCode.deserializeMiraiCode("[mirai:at:" + sender + "]");
        MessageChainBuilder chain = new MessageChainBuilder().append(at);
        chain.append(new PlainText("test"));
        chain.append(new PlainText("\ntest2"));
        return chain.build();
    }
}
