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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.naming.NamingException;

/**
 *
 * @author vaduri
 */
public class SendReminder extends BaseJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(SendReminder.class.getName());
    
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        LOGGER.info("Inside SendReminder execute ...........");
        Connection dbConnection = null;
        String emailSent = null;
        LOGGER.info("key Name" + jec.getJobDetail().getKey().getName());
        String jobName = jec.getJobDetail().getKey().getName();
        HexiUtil hexiUtil = new HexiUtil();   

        try {

            dbConnection = hexiUtil.getDBConnection();

            //check unresolved customer requests
            ArrayList unResolvedRequests = getUnResolvedRequests(dbConnection);

            if (unResolvedRequests != null) {
                // get reminders frequency and find next remainder date. if it is on the same day send remainder
                int requestsCount = unResolvedRequests.size();
                //get jobId from jobname
                int jobId = getJobId(dbConnection, jobName);
                int noOfReminders = 0;
                int daysBetweenReminders = 0;
                int csmMailCount = 0;
                Date requestSentOn = null;
                String Sent_to = null;
                String csmFirstName = null;
                String csmLastName = null;
                int newCsmMailCount = 0;
                int srId = 0;
                String emailContent = null; 
                String emailSubject = null;
                
                //get rules configured for this job                
                HashMap rulesConfigMap = getRulesConfigured(dbConnection, jobId);                
                Calendar current = Calendar.getInstance();
                Date currentDate = current.getTime();
                SimpleDateFormat cFormatter = new SimpleDateFormat("yyyy-MM-dd");
                currentDate = cFormatter.parse(cFormatter.format(currentDate));
                LOGGER.info("Current Date .." + cFormatter.format(currentDate));
                
                        
           
                // get key-value pairs from rules configuration         
                Iterator it = rulesConfigMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    LOGGER.info("Rule key ...." + pair.getKey());
                    LOGGER.info("Rule value ....." + pair.getValue());

                    if (pair.getKey().equals("NO_OF_REMINDERS")) {

                        noOfReminders = Integer.parseInt(pair.getValue().toString());
                    }
                    if (pair.getKey().equals("DAYS_BETWEEN_REMINDERS")) {

                        daysBetweenReminders = Integer.parseInt(pair.getValue().toString());
                    }
                    if (pair.getKey().equals("CSM_EMAIL_TEMPLATE")) {

                        emailContent = pair.getValue().toString();
                    }
                    if (pair.getKey().equals("CSM_EMAIL_SUBJECT")) {

                        emailSubject = pair.getValue().toString();
                    }
                }

                for (int i = 0; i < requestsCount; i++) {

                    LOGGER.info("Sr Id....." + unResolvedRequests.get(i));
                    LOGGER.info("sent_to....." + unResolvedRequests.get(i + 1));
                    LOGGER.info("request_sent_on....." + unResolvedRequests.get(i + 2));
                    LOGGER.info("csm_mail_count...." + unResolvedRequests.get(i + 3));
                    LOGGER.info("first name....." + unResolvedRequests.get(i + 4));
                    LOGGER.info("last name....." + unResolvedRequests.get(i + 5));

                    if (unResolvedRequests.get(i) != null) {
                        srId = Integer.parseInt(unResolvedRequests.get(i).toString());
                    }

                    if (unResolvedRequests.get(i + 3) != null) {
                        csmMailCount = Integer.parseInt(unResolvedRequests.get(i + 3).toString());
                    }
                    if (unResolvedRequests.get(i + 2) != null) {
                        
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        requestSentOn = formatter.parse(unResolvedRequests.get(i + 2).toString());
                    }
                    if (unResolvedRequests.get(i + 1) != null) {

                        Sent_to = unResolvedRequests.get(i + 1).toString();
                    }
                    if (unResolvedRequests.get(i + 4) != null) {

                        csmFirstName = unResolvedRequests.get(i + 4).toString() + " " + unResolvedRequests.get(i + 5);
                    }
                    if (unResolvedRequests.get(i + 5) != null) {

                        csmLastName = unResolvedRequests.get(i + 4).toString() + " " + unResolvedRequests.get(i + 5);
                    }

                    newCsmMailCount = csmMailCount + 1;
                   //String subject = "Reminder: " + newCsmMailCount + " for Email request " + unResolvedRequests.get(i);
                   // mailContent = "Hi " + csmName + " , \n" + mailContent1 +  srId  + mailContent2;
                   
                    emailSubject = emailSubject.replaceAll("<<REMINDER_COUNT>>", Integer.toString(csmMailCount));
                    emailSubject = emailSubject.replaceAll("<<SR_ID>>", Integer.toString(srId));
                    
                    emailContent =  emailContent.replaceAll("<<FIRST_NAME>>", csmFirstName);
                    emailContent =  emailContent.replaceAll("<<LAST_NAME>>", csmLastName);
                    emailContent =  emailContent.replaceAll("<<SR_ID>>",Integer.toString(srId));
                    emailContent =  emailContent.replaceAll("<>"," ");
                      
                    i = i + 5;
                    //check remainder counts and send reminders
                 
                    
                    if (noOfReminders > csmMailCount) {
                        if (requestSentOn != null) {
                               requestSentOn = cFormatter.parse(cFormatter.format(requestSentOn));
                               LOGGER.info("Request sent on date "+ requestSentOn);
                            
                            if (requestSentOn.compareTo(currentDate) < 0) {

                                Calendar cal = Calendar.getInstance();
                                cal.setTime(requestSentOn);
                                cal.add(Calendar.DATE, daysBetweenReminders); // add frequency days
                                Date calculateDate = cal.getTime();
                                calculateDate = cFormatter.parse(cFormatter.format(calculateDate));
                                LOGGER.info("Calculated Date " + calculateDate);
                                // computed date is today
                                
                                if (calculateDate.compareTo(currentDate) == 0) {

                                    //send email
                                    hexiUtil.sendMail(dbConnection, Sent_to, emailSubject, emailContent);
                                    //update user_email tablecurrentDate,srId);
                                    updateUserMails(dbConnection,newCsmMailCount, currentDate, srId);
                                }

                            }

                        } else {
                            //send email
                            hexiUtil.sendMail(dbConnection, Sent_to, emailSubject, emailContent);
                            //update user_email tablecurrentDate,srId);
                            updateUserMails(dbConnection,newCsmMailCount, currentDate, srId);

                        }

                    }

                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(SendReminder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        LOGGER.info("result ..." + emailSent);
    }  

    private static ArrayList<String> getUnResolvedRequests(Connection dbConnection) throws SQLException {
        ArrayList unResolvedRequests = new ArrayList();

        if (dbConnection != null) {
            String query = "select ue.sr_id as sr_id,ue.sent_to as sent_to,ue.request_sent_on as request_sent_on,ue.csm_mail_count as mailcount,u.first_name as first_name,u.last_name as last_name from user_emails ue ,users u where ue.is_resolved = 0 and ue.user_id = u.user_id and u.active =1 order by sr_id asc";
            PreparedStatement pStmt = dbConnection.prepareStatement(query);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {

                unResolvedRequests.add(rs.getInt(1));
                unResolvedRequests.add(rs.getString(2));
                unResolvedRequests.add(rs.getString(3));
                unResolvedRequests.add(rs.getString(4));
                unResolvedRequests.add(rs.getString(5));
                unResolvedRequests.add(rs.getString(6));

            }
        }

        return unResolvedRequests;

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

        // return configurdRules;
        return rulesConfMap;
    }

    private void updateUserMails(Connection dbConnection,int newCsmMailCount, Date currentDate, int srId) throws NamingException, SQLException {

        if (dbConnection != null) {
            PreparedStatement updStmt = null;

            java.sql.Date sqlDate = new java.sql.Date(currentDate.getTime());
            try {
                String query = "update USER_EMAILS set CSM_MAIL_COUNT = ? , REQUEST_SENT_ON = ? where SR_ID=" + srId + "";
                updStmt = dbConnection.prepareStatement(query);
                updStmt.setInt(1, newCsmMailCount);
                updStmt.setDate(2, sqlDate);
                updStmt.executeUpdate();
                LOGGER.info("Record is updated to USER_EMAILS table!");

            } catch (SQLException se) {
                se.printStackTrace();
            } finally {
                if (updStmt != null) {
                            updStmt.close();
                }                
            }

        }
    }
}
