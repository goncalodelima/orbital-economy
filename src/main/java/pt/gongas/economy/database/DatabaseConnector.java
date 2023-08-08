package pt.gongas.economy.database;

import java.sql.Connection;

public abstract class DatabaseConnector {

    protected static Connection connection;

    public abstract void connect();
    public abstract void close();
    public abstract void createTable();
    public static Connection getConnection() { return connection; }

}
