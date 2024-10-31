import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

class Member {
    String uuid;
    String rank;
    long joined;

    // Getters 和 Setters 可以省略，取决于你的需求
}

public class test {
    public static void main(String[] args) {
        String jsonArray = "[{\"uuid\":\"aa197cbac6884bc7913b02b4794eeffd\",\"rank\":\"GuildMaster\",\"joined\":1667151336276},{\"uuid\":\"88e5c5d3c8ac4f7ca84c3cbb38822f82\",\"rank\":\"成员\",\"joined\":1669729320465},{\"uuid\":\"cb012f337a9d43a0a462ef006975126d\",\"rank\":\"成员\",\"joined\":1675850376000}]";

        Gson gson = new Gson();
        Type memberListType = new TypeToken<List<Member>>() {}.getType();
        List<Member> members = gson.fromJson(jsonArray, memberListType);

        String targetUuid = "88e5c5d3c8ac4f7ca84c3cbb38822f82";
        Optional<Member> member = members.stream()
                .filter(m -> m.uuid.equals(targetUuid))
                .findFirst();

        member.ifPresent(m -> {
            System.out.println("找到成员: " + m.rank + ", UUID: " + m.uuid);
        });
    }
}
