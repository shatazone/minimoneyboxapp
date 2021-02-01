package com.example.minimoneybox.misc;

import com.google.gson.Gson;

import javax.inject.Inject;

public class JsonSerializer {
        private final Gson gson;
        
        @Inject
        public JsonSerializer(Gson gson) {
            this.gson = gson;
        }
        
        public String toJson(Object object) {
            if(object == null) {
                return "null:null";
            } else {
                return object.getClass().getName() + ":" + gson.toJson(object);
            }
        }

        public <T> T fromJson(String json) {
            int index = json.indexOf(':');
            String classValue = json.substring(0, index);
            String objectValue = json.substring(index+1);

            if(classValue.equals("null")) {
                return null;
            } else {
                try {
                    return (T) gson.fromJson(objectValue, Class.forName(classValue));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);  // better to throw a dedicated exception
                }
            }
        }
    }