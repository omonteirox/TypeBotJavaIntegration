package com.github.omonteirox;

import com.google.gson.*;


import java.util.ArrayList;
import java.util.Collection;

public class BotClient {

    private  final String token = SecretInfo.token;
    private final String url = "https://typebot.io/api/v1/";
    private String sessionId;
    private final Collection<String> messagesFromBot = new ArrayList<>();
    private final Collection<String> inputsFromBot = new ArrayList<>();


    public void messagesFromBot(Collection<JsonObject> objects) {
        for(JsonObject obj : objects){
            JsonArray messages = obj.get("messages").getAsJsonArray();
            for (JsonElement e : messages) {
                JsonObject message = e.getAsJsonObject();
                JsonObject content = message.get("content").getAsJsonObject();
                JsonArray richText = content.get("richText").getAsJsonArray();
                for (JsonElement r : richText) {
                    JsonObject rich = r.getAsJsonObject();
                    JsonArray children = rich.get("children").getAsJsonArray();
                    for (JsonElement c : children) {
                        JsonObject child = c.getAsJsonObject();
                        if(child.has("children")){
                            JsonArray children2 = child.get("children").getAsJsonArray();
                            for (JsonElement c2 : children2) {
                                JsonObject child2 = c2.getAsJsonObject();
                                String text = child2.get("text").getAsString();
                                messagesFromBot.add(text);
                            }
                        }else {
                            String text = child.get("text").getAsString();
                            messagesFromBot.add(text);
                        }

                    }
                }
            }
        }
        System.out.println("Mensagens que o BOT ENVIOU -> " + messagesFromBot.toString());
    }

    public void inputsFromBot(Collection<JsonObject> objects) {
        for(JsonObject obj : objects){
            if (obj.has("input")) {
                JsonObject inputs = obj.get("input").getAsJsonObject();
                if(inputs.has("items")){
                    JsonArray items = inputs.get("items").getAsJsonArray();
                    for (JsonElement i : items) {
                        JsonObject item = i.getAsJsonObject();
                        String content = item.get("content").getAsString();
                        inputsFromBot.add(content);
                    }
                }
                inputsFromBot.add("Não tem Texto, o tipo do input é " + inputs.get("type").getAsString());


            }
        }
        System.out.println("Inputs que o Bot Enviou -> " + inputsFromBot);
    }

    public JsonObject continueChat(String clientMessage) {
        String urlContinue = url + "sessions/" + sessionId + "/continueChat";
        Gson gson = new Gson();
        JsonObject body = new JsonObject();
        body.addProperty("message", clientMessage);

        String bodyJson = gson.toJson(body);

        String result = HttpUtil.connect(urlContinue, "POST", bodyJson, token);


        return JsonParser.parseString(result).getAsJsonObject();

    }

    public JsonObject startChat(Collection prefilled) {
        String idChat = SecretInfo.idChat;
        String urlStart = url + "typebots/" + idChat + "/preview/startChat";
        Gson gson = new Gson();
        JsonObject object = new JsonObject();
        String atracoesJson = gson.toJson(prefilled);
        JsonObject prefilledVariables = new JsonObject();
        prefilledVariables.addProperty("atracoes-travel", atracoesJson);

        JsonObject atracoesTravel = new JsonObject();
        atracoesTravel.addProperty("atracoes-travel", atracoesJson);

        object.add("prefilledVariables", atracoesTravel);
        String bodyJson = gson.toJson(object);


        String result = HttpUtil.connect(urlStart, "POST", bodyJson, token);

        JsonObject obj = JsonParser.parseString(result).getAsJsonObject();

        sessionId = getSessionId(obj);
      return obj;


    }
    public String getSessionId(JsonObject obj) {
        return obj.get("sessionId").getAsString();
    }


}
