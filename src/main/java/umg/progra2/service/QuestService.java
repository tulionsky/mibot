package umg.progra2.service;

import umg.progra2.dao.QuestDao;
import umg.progra2.db.DatabaseConnection;
import umg.progra2.db.TransactionManager;
import umg.progra2.model.Quest;

import java.sql.Connection;
import java.sql.SQLException;

public class QuestService {

    private QuestDao questDao = new QuestDao();

    public void crearUsuario(Quest quest) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            TransactionManager tm = new TransactionManager(connection);
            tm.beginTransaction();
            try {
                questDao.insertUser(quest);
                tm.commit();
            } catch (SQLException e) {
                tm.rollback();
                throw e;
            }
        }
    }
}
