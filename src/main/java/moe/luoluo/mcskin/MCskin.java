package moe.luoluo.mcskin;

import com.google.gson.JsonObject;
import moe.luoluo.apis.Mojang;
import moe.luoluo.apis.Skin;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class MCskin {
    private static final Logger log = LoggerFactory.getLogger(MCskin.class);

    public static void mcskin(CommandSender context, String player) throws IOException, URISyntaxException {
        MessageChainBuilder chain = new MessageChainBuilder();

        String uuid = Mojang.mojang(player, "uuid");
        if (Objects.equals(uuid, "NotFound")) {
            context.sendMessage("玩家不存在");
            return;
        }

        JsonObject profile = Skin.getProfile(uuid);

        chain.append("玩家: ").append(profile.get("profileName").getAsString());
        chain.append("\nuuid: ").append(profile.get("profileId").getAsString());

        try {
            byte[] data = Skin.render(uuid);
            if (context.getSubject() != null)
                chain.append(context.getSubject().uploadImage(ExternalResource.create(data)));

        } catch (IOException e) {
            chain.append(new PlainText("\n皮肤渲染图加载失败"));
            log.error(String.valueOf(new RuntimeException(e)));
        }

        URI skinUri = Skin.getSkinUri(profile);
        try {
            byte[] data = Skin.skin(skinUri);
            if (context.getSubject() != null)
                chain.append(context.getSubject().uploadImage(ExternalResource.create(data)));

        } catch (IOException e) {
            chain.append(new PlainText("\n皮肤纹理加载失败"));
            log.error(String.valueOf(new RuntimeException(e)));
        }
        chain.append("\n皮肤链接: ").append(String.valueOf(skinUri));

        URI capeUri = Skin.getCapeUri(profile);
        if (capeUri != null) {
            try {
                byte[] data = Skin.skin(capeUri);
                if (context.getSubject() != null)
                    chain.append(context.getSubject().uploadImage(ExternalResource.create(data)));

            } catch (IOException e) {
                chain.append(new PlainText("\n披风图片加载失败"));
                log.error(String.valueOf(new RuntimeException(e)));
            }
            chain.append("\n披风链接: ").append(String.valueOf(capeUri));
        }

        URI ofCape = new URI("http://s.optifine.net/capes/" + profile.get("profileName").getAsString() + ".png");
        byte[] ofData;
        try {
            ofData = Skin.skin(ofCape);
            if (context.getSubject() != null)
                chain.append(context.getSubject().uploadImage(ExternalResource.create(ofData)));

            chain.append("\noptifine披风链接: ").append(String.valueOf(ofCape));
        } catch (IOException ignored) {
        }

        context.sendMessage(chain.build());
    }
}
