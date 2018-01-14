/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.planning.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import org.springframework.core.env.Environment;

public final class DatabaseCreator {

    public static void createDatabaseIfNotExist(Environment env) {
        try {
            Class.forName(env.getProperty("db.planning.driverClass"));
            DriverManager.getConnection(env.getProperty("db.planning.url"), env.getProperty("db.planning.username"), env.getProperty("db.planning.password"));
        } catch (ClassNotFoundException | SQLException ex) {
            try {
                Connection connection = DriverManager.getConnection(env.getProperty("db.planning.connection"), env.getProperty("db.planning.username"), env.getProperty("db.planning.password"));
                Statement st = connection.createStatement();
                st.executeUpdate("CREATE DATABASE " + env.getProperty("db.planning.database"));
                Logger.getLogger(DatabaseCreator.class.getName()).warning(ex.getLocalizedMessage());
            } catch (SQLException ex1) {
                Logger.getLogger(DatabaseCreator.class.getName()).warning(ex1.getLocalizedMessage());
            }
        }
    }
}
