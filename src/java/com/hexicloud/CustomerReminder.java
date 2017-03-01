/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hexicloud;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.logging.Logger;
import oracle.quartz.BaseJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
/**
 *
 * @author vaduri
 */
public class CustomerReminder extends BaseJob implements Job{
     private static final Logger LOGGER = Logger.getLogger(SendReminder.class.getName());
     HexiUtil hexiUtil = new HexiUtil();
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        LOGGER.info("Inside CustomerReminder execute ...........");
        
        Connection dbConnection = null;
        String mailContent = null;
        String emailSent = null;
        String subscriptionId = null;
        String customerFirstName = null;
        String customerLastName = null;
        String customerEmail = null;
        LOGGER.info("key Name" + jec.getJobDetail().getKey().getName());
        String jobName = jec.getJobDetail().getKey().getName();
     

        try {

            dbConnection = hexiUtil.getDBConnection();
            //check unresolved customer requests
            int jobId = getJobId(dbConnection, jobName);
            ArrayList customersList = geCustomersFromCLM(dbConnection);
            
            if(customersList != null) 
            {
            int customerCount = customersList.size();
            LOGGER.info("customerCount" + customerCount);
            HashMap rulesConfigMap = getRulesConfigured(dbConnection, jobId);
            String occurrence = null;
            String mailSubject = "Reminder : Please access provisioned cloud services";
            Iterator it = rulesConfigMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    LOGGER.info("Rule key ...." + pair.getKey());
                    LOGGER.info("Rule value ....." + pair.getValue());

                    if (pair.getKey().equals("CUSTOMER_EMAIL_TEMPLATE")) {

                        mailContent = pair.getValue().toString();
                    }                   

                }                             
                
                for (int i = 0; i < customerCount; i++) {

                    LOGGER.info("Subscription Id....." + customersList.get(i));
                    LOGGER.info("Line start date" + customersList.get(i + 1));
                    LOGGER.info("First name....." + customersList.get(i + 2));
                    LOGGER.info("last name...." + customersList.get(i + 3));
                    LOGGER.info("email...." + customersList.get(i + 4));
                    
                    if (customersList.get(i) != null) {

                        subscriptionId = customersList.get(i).toString();
                    }
                    if (customersList.get(i + 2) != null) {

                        customerFirstName = customersList.get(i + 2).toString();
                    }
                    if (customersList.get(i + 3) != null) {

                        customerLastName = customersList.get(i + 2).toString();
                    }
                    if (customersList.get(i + 4) != null) {

                        customerEmail = customersList.get(i + 2).toString();
                    }
                    if (mailContent != null){
                    
                        mailContent.replaceAll("<<FIRST_NAME>>", customerFirstName);
                        mailContent.replaceAll("<<LAST_NAME>>", customerFirstName);
                        mailContent.replaceAll("<<SUBSCRIPTION_ID>>", customerFirstName);
                    }
                    
                    
                    hexiUtil.sendMail(dbConnection, customerEmail, mailSubject, mailContent);
                    
                    i=i+4;
                }
              }
                
                
            
        }catch (SQLException ex) {
            Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
        }       

        finally {
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }    
     
    private int getJobId(Connection dbConnection, String jobName) throws SQLException {

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
    private ArrayList geCustomersFromCLM(Connection dbConnection) throws SQLException {
         ArrayList customersList = new ArrayList();

        if (dbConnection != null) {
            String query = "select clm.SUBSCRIPTION_ID, clm.line_start_date,u.FIRST_NAME,u.LAST_NAME,u.email from clm_data clm, users u where SUBSCRIPTION_LINE_STATUS = 'ACTIVE' and clm.REGISTRY_ID = u.REGISTRY_ID and u.ACTIVE= 1 and clm.LINE_START_DATE < sysdate - 7 and clm.SUBSCRIPTION_ID = '1444750'";
            PreparedStatement pStmt = dbConnection.prepareStatement(query);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {

                customersList.add(rs.getInt(1));
                customersList.add(rs.getString(2));
                customersList.add(rs.getString(3));
                customersList.add(rs.getString(4));
                customersList.add(rs.getString(5));
                

            }
        }

        return customersList;

    }
    
    private HashMap getRulesConfigured(Connection dbConnection, int jobId) throws SQLException {

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
