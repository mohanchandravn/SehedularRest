package com.hexicloud;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import oracle.quartz.BaseJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vaduri
 */
public class CustomerWelcomeEmail extends BaseJob implements Job{
    private static final Logger LOGGER = Logger.getLogger(SendReminder.class.getName());
     HexiUtil hexiUtil = new HexiUtil();
    
      @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.print("Inside CustomerReminder execute ...........");
        
        Connection dbConnection = null;
        String emailSubject = null;
        String emailContent = null;
        String emailSent = null;       
        String customerFirstName = null;
        String customerLastName = null;
        String header = null;
        String subHeader = null;
        String sentTo = null;
      //  String sentTo = "venu.aduri@oracle.com";
        
        int jobId = 0;
        System.out.print("key Name" + jec.getJobDetail().getKey().getName());
        String jobName = jec.getJobDetail().getKey().getName();
     

        try {

            dbConnection = hexiUtil.getDBConnection();
            jobId = hexiUtil.getJobId(dbConnection, jobName);
            HashMap rulesConfigMap = hexiUtil.getRulesConfigured(dbConnection, jobId);
            
            ArrayList getUserDetails = getUserDetails(dbConnection);
            
              if (getUserDetails != null) {
                // get reminders frequency and find next remainder date. if it is on the same day send remainder
                int userCount = getUserDetails.size();
             
            
              
                    for (int i = 0; i < userCount; i++) {

                          LOGGER.info("sent_to....." + getUserDetails.get(i));
                          LOGGER.info("first_name....." + getUserDetails.get(i + 1));
                          LOGGER.info("last_name....." + getUserDetails.get(i + 2));


                          if (getUserDetails.get(i) != null) {
                              sentTo = getUserDetails.get(i).toString();
                          }
                          if (getUserDetails.get(i+1) != null) {
                              customerFirstName = getUserDetails.get(i+1).toString();
                          }
                          if (getUserDetails.get(i+2) != null) {
                              customerLastName = getUserDetails.get(i+2).toString();
                          }
                          
                          i = i +3;
                    }   
               }

            Iterator it = rulesConfigMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    System.out.print("Rule key ...." + pair.getKey());
                    System.out.print("Rule value ....." + pair.getValue());

                    if (pair.getKey().equals("CUSTOMER_WELCOME_EMAIL_SUBJECT")) {

                        emailSubject = pair.getValue().toString();
                    }          
                    if (pair.getKey().equals("CUSTOMER_WELCOME_EMAIL_TEMPLATE")) {

                        emailContent = pair.getValue().toString();
                    }                            
                    
                }               
                //trigger email                
                if(dbConnection != null && sentTo !=null && emailSubject != null && emailContent != null){
                        hexiUtil.sendMail(dbConnection, sentTo, emailSubject, emailContent);
                }
            }catch (SQLException ex) {
            Logger.getLogger(CustomerWelcomeEmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(CustomerWelcomeEmail.class.getName()).log(Level.SEVERE, null, ex);
        }       

        finally {
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerWelcomeEmail.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }    
    
      private static ArrayList<String> getUserDetails(Connection dbConnection) throws SQLException {
        ArrayList userDetails = new ArrayList();

        if (dbConnection != null) {
            String query = "select ue.sent_to as sent_to,u.first_name as first_name,u.last_name as last_name from user_emails ue ,users u where ue.is_resolved = 0 and ue.user_id = u.user_id and u.active =1  and ue.sr_id = 36 order by sr_id asc";
            PreparedStatement pStmt = dbConnection.prepareStatement(query);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {

                userDetails.add(rs.getString(1));
                userDetails.add(rs.getString(2));
                userDetails.add(rs.getString(3));
         

            }
        }

        return userDetails;

    }
    
}
