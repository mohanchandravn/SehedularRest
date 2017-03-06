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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
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
    
      public int getJobId(Connection dbConnection, String jobName) throws SQLException {

        int jobId = 0;

        if (dbConnection != null) {

            String query = "select job_id from Job_configuration where job_name ='" + jobName + "'";
            //String query = "select sr_id,sent_to,request_sent_on from user_emails";
            PreparedStatement pStmt = dbConnection.prepareStatement(query);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                jobId = rs.getInt(1);
            }

        }
        return jobId;
    }
      public HashMap getRulesConfigured(Connection dbConnection, int jobId) throws SQLException {

        //ArrayList configurdRules  = new ArrayList();
        HashMap<String, String> rulesConfMap = new HashMap<String, String>();
        if (dbConnection != null) {
            PreparedStatement pStmt = null;
            ResultSet rs = null;
            try {
                String query = "select rule_key,rule_value,rule_type from RULE_CONFIGURATION where job_id=" + jobId;
                pStmt = dbConnection.prepareStatement(query);
                rs = pStmt.executeQuery();

                while (rs.next()) {
                   rulesConfMap.put(rs.getString(1), rs.getString(2));
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (pStmt != null) {
                    try {
                        pStmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
      
        return rulesConfMap;
    }    


    
}
