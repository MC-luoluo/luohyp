package moe.luoluo.apis;

import com.google.gson.JsonObject;

public record ApiResult(JsonObject json, long time) {
}
