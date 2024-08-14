package umg.progra2;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import umg.progra2.botTelegram.Bot;
import umg.progra2.botTelegram.PokemonBot;
import umg.progra2.botTelegram.TareaBot;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args){
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TareaBot bot1 = new TareaBot();
            //PokemonBot poke = new PokemonBot();
            botsApi.registerBot(bot1);
            System.out.println("El bot1 funciona bien pa");
        }
        catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }

    }
}