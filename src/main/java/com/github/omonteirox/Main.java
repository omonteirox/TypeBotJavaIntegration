package com.github.omonteirox;

import com.google.gson.*;


import java.util.Arrays;
import java.util.Collection;


public class Main {


    public static void main(String[] args) {
        Collection<String> produtosDoParque = Arrays.asList("Parque da joana, Parque da Telma, Parque da Mariazinha");
        BotClient botClient = new BotClient();
        JsonObject startChat = botClient.startChat(produtosDoParque);
        JsonObject obj1 =   botClient.continueChat("Atrações");
        JsonObject obj2 = botClient.continueChat("hahahahahah");
        JsonObject obj3 = botClient.continueChat("Sim");
        JsonObject obj4 = botClient.continueChat("10/10/2010");
        botClient.messagesFromBot(Arrays.asList(startChat,obj1,obj2,obj3,obj4));
        botClient.inputsFromBot(Arrays.asList(startChat, obj1, obj2,obj3,obj4));



    }


}