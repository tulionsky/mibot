package umg.progra2.botTelegram;

import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TareaBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "@Tulionsky_bot";
    }

    @Override
    public String getBotToken() {
        return "7348836758:AAGNaK4IkW86rBWRRIhTOjNqQzI8vCGvRpI";
    }

    private static final String API_KEY = "8c8b4151b661fc1025411ad2";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/EUR";

    public static double obtenerTipoCambio(String monedaDestino) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();

            JSONObject json = new JSONObject(content.toString());
            return json.getJSONObject("conversion_rates").getDouble(monedaDestino);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void Mensajegrupal(String messageText) {
        String mensaje = messageText.substring(8).trim();
        List<Long> listaChats = List.of(6597569075L, 6688363556L,  1533824724L); // Reemplaza con los IDs de tus compañeros
        for (Long id : listaChats) {
            sendText(id, mensaje);
        }
    }

    // Método para enviar una imagen
    public void sendPhoto(long chatId, String caption) {
        SendPhoto photo = new SendPhoto();
        photo.setChatId(chatId);
        File file = new File("C:\\Users\\tulio\\Downloads\\7XC1KUjAQAWqbRfb8yZCMD0X_GpXdlWmmSiZXiecjXA.webp");
        photo.setPhoto(new InputFile(file)); // Puede ser una URL o un archivo local
        photo.setCaption(caption); // Texto opcional que acompaña la imagen

        try {
            execute(photo); // Envía la imagen
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void Menuprincipal(long chatId) {
        sendText(chatId, "Bienvenidoo, ¿Es tu primera vez por aquí verdad?\n¿Porque no intentas saludar?\n" +
                "O si deseas, aqui están los comandos disponibles:\n"
                + "/info - Información personal de mi creador\n"
                + "/progra - Información sobre la clase de programación\n"
                + "/hola - Saludo con la fecha actual\n"
                + "/cambio [una cantidad] - Conversión de Euros a Quetzales\n"
                + "/grupal [mensaje] - Enviar un mensaje grupal a mi creador y sus amigos\n" +
                "\nO si deseas tambien, puedes colocar tu nombre y esperar una sorpresa :)");
    }


    @Override
    public void onUpdateReceived(Update update) {

        //obtener informacion de lapersona que manda mensajes
        String nombre = update.getMessage().getFrom().getFirstName();
        String apellido = update.getMessage().getFrom().getLastName();
        String username = update.getMessage().getFrom().getUserName();
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy, HH:mm:ss");
        String fechaFormateada = ahora.format(formatoFecha);



        //Se verifica si la actualización contiene un mensaje y si ese mensaje tiene texto.
        //Luego se procesa el contenido del mensaje y se responde según el caso.
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            //manejo de mensajes
            if (message_text.toLowerCase().equals("/start")) {
                Menuprincipal(chat_id);
            }
            if (message_text.toLowerCase().equals("hola")) {
                sendText(chat_id, "Hola " + nombre + ", hoy es " + fechaFormateada);
            }
            if (message_text.toLowerCase().equals("/info")) {
                sendText(chat_id,"Hola! Soy un bot creado por: Tulio Quintana\nNo.Carnet: 0905-23-5024\nY cursa el semestre 4 de la carrera de ingenieria en sistemas :)");
            }
            if (message_text.toLowerCase().equals("/progra")) {
                sendText(chat_id,"¡La clase de progra está genial! Cada día aprendes algo nuevo y útil que te acerca más al mundo de la tecnología.\n(Este mensaje no esta claramente pensado para caerle mejor al Ing. Ruldin)");
            }
            if (message_text.startsWith("/grupal")) {
                Mensajegrupal(message_text);
            }
            if (message_text.toLowerCase().equals("/foto")) {
                sendPhoto(chat_id, "Que bonita foto no?.");
            }
            if (message_text.toLowerCase().equals("/teffy")){

            }
            if (message_text.toLowerCase().startsWith("/cambio")) {
                try {
                    String[] partes = message_text.split(" ");
                    double euros = Double.parseDouble(partes[1]);

                    // Obtiene el tipo de cambio actual de EUR a GTQ
                    double tipoCambio = obtenerTipoCambio("GTQ");
                    if (tipoCambio == -1) {
                        sendText(chat_id, "Error al obtener el tipo de cambio.");
                    } else {
                        double conversion = euros * tipoCambio;
                        String quetzales = String.format("%.2f",conversion);
                        sendText(chat_id, euros + " Euros son " + quetzales + " Quetzales.");
                    }
                } catch (Exception e) {
                    sendText(chat_id, "Por favor ingresa un valor válido para el cambio.");
                }


            }

            System.out.println("User id: " + chat_id + " Message: " + message_text);

        }
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }





}
