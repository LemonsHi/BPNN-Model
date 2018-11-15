package com.tools;

import org.json.JSONObject;

import java.util.Map;

public class JSONTools {

    private String jsonStr = "";

    public JSONTools (String jsonStr) {
        this.jsonStr = jsonStr;
    }

    private JSONObject strToObject (String type) {
        JSONObject strJson = new JSONObject(jsonStr);
        switch (type) {
            case "BP":
                return strJson.getJSONObject("mainConfig").getJSONObject("BP");
        }
        return null;
    }

    public JSONObject getBPConfig () {
        return this.strToObject("BP");
    }
}
