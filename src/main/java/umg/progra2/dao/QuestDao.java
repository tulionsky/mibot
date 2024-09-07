package umg.progra2.dao;

import umg.progra2.db.DatabaseConnection;
import umg.progra2.model.Quest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestDao {

    public void insertUser(Quest quest) throws SQLException {
        String query = "INSERT INTO tb_respuestas(seccion,telegram_id,respuesta_texto,pregunta_id,nombre_id) VALUES (?,?,?,?,?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, quest.getSeccion());
            statement.setLong(2, quest.getTelegramid());
            statement.setString(3, quest.getResponse());
            statement.setInt(4, quest.getPreguntaid());
            statement.setString(5, quest.getNombreid());

            statement.executeUpdate();
        }
    }
}
