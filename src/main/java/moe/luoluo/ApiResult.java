package moe.luoluo;

import com.google.gson.JsonObject;

public class ApiResult {
    private JsonObject json;
    private long time;

    public ApiResult(JsonObject json, long time) {
        this.json = json;
        this.time = time;
    }

    public JsonObject getJson() {
        return json;
    }

    public long getTime() {
        return time;
    }
}
