/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hexicloud;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author vaduri
 */
public class HexiUtil {

   private static final Logger LOGGER = Logger.getLogger(HexiUtil.class.getName());
    public static final String PROPERTIES_FILE = "configurations.properties";
    public static Properties properties = new Properties();
    
    public void HexiUtil(){
    readProperties();
    }
    private Properties readProperties() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }     
    public String sendMail(Connection dbConnection, String send_to, String subject, String mailContent) throws SQLException, NamingException {

        CallableStatement callableStatement = null;
        String result = null;

        String sendEmailProc = "{call send_email(?,?,?,?,?)}";

        try {
            
            callableStatement = dbConnection.prepareCall(sendEmailProc);

            callableStatement.setString(1, "metcs-cloud.admin@oracleads.com");
            callableStatement.setString(2, send_to);
            callableStatement.setString(3, subject);
            callableStatement.setString(4, mailContent);

            callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);

            // execute procedure
            callableStatement.executeUpdate();

            result = callableStatement.getString(5);
            LOGGER.info("Mail sent successfully "+ result);
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (callableStatement != null) {
                callableStatement.close();
            }

        }
        return result;

    }

    public  Connection getDBConnection() throws NamingException {

        Connection dbConnection = null;
        Properties dbProperties = readProperties();
        
        String dbCon = null;
        String dbUser = null;
        String dbPassword = null;
    
        
        

        try {
            
            dbCon = dbProperties.getProperty("dbCon");
            dbUser = dbProperties.getProperty("dbUser");
            dbPassword = dbProperties.getProperty("dbPassword");
            
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            dbConnection = DriverManager.getConnection(dbCon, dbUser, dbPassword);
            LOGGER.info("dbConnection created successfully");
            return dbConnection;

        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return dbConnection;

    }

    
}
