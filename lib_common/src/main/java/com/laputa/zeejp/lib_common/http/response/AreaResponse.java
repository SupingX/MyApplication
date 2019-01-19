package com.laputa.zeejp.lib_common.http.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class AreaResponse {
    public List<Area> list;

    public static class Area{
        public String name;
        public String pId;
        public String id;

        public static List<Area> fromJsonList(String jsonList) {
            List<Area> bean;
            Gson g = new Gson();
            TypeToken<List<Area>> typeToken = new TypeToken<List<Area>>() {
            };
            bean = g.fromJson(jsonList, typeToken.getType());
            return bean;
        }
    }
}
