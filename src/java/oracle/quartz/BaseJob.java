/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.quartz;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.naming.CommunicationException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import oracle.com.QuartzJobConfiguration;
import oracle.com.QuartzJobHistory;
import oracle.com.service.QuartzJobConfigurationFacadeREST;
import oracle.com.service.QuartzJobHistoryFacadeREST;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author jyomohan
 */
public class BaseJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(BaseJob.class.getName());
    protected Integer jobHistoryId = 0;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        LOGGER.info("************" + jec.getJobDetail().getKey().getName() + "  Job Executed successfully****************");
        jobHistoryId = this.createJobHistory(jec.getJobDetail().getKey().getName());
    }

    /**
     * Methods creates job history record and returns Job History Id
     *
     * @param jobName
     * @return Job History Id
     */
    public Integer createJobHistory(String jobName) {
        LOGGER.info("Entering the method createJobHistory");
        Integer jobId = null;
        Integer historyId = 0;
        try {
            InitialContext ic = new InitialContext();
            QuartzJobConfigurationFacadeREST configObj = (QuartzJobConfigurationFacadeREST) ic.lookup("java:comp/env/Scheduler/QuartzJobConfigurationFacadeREST");
            QuartzJobHistoryFacadeREST historyObj = (QuartzJobHistoryFacadeREST) ic.lookup("java:comp/env/Scheduler/QuartzJobHistoryFacadeREST");
            QuartzJobConfiguration entity = null;
            LOGGER.info("rows size:::" + configObj.getjobConfigByJobname(jobName).size());
            if (configObj.getjobConfigByJobname(jobName).size() == 1) {
                LOGGER.info("Inside if" + configObj.getjobConfigByJobname(jobName));
                entity = configObj.getjobConfigByJobname(jobName).get(0);
            }
            if (entity != null) {
                jobId = entity.getJobId();
            }
            LOGGER.info("job Id:::" + jobId);
            java.util.Date date = new java.util.Date();
            Date START_DATE = new Timestamp(date.getTime());
            String jobStatus = "P"; //SchedulerConstants.JOB_STATUS_IN_PROGRESS;
            QuartzJobHistory row = new QuartzJobHistory();
            row.setStartDate(START_DATE);
            row.setJobStatus(jobStatus);
            row.setJobId(jobId);
            LOGGER.info("history obj:" + row);
            historyObj.create(row);
            List<QuartzJobHistory> historybyjobid = historyObj.gethistorybyjobid(jobId.toString());

            for (QuartzJobHistory historyRow : historybyjobid) {
                if (historyRow.getJobHistoryId() > historyId) {
                    historyId = historyRow.getJobHistoryId();
                }
            }
            LOGGER.info("historyId Id =====>" + historyId + START_DATE + jobStatus + jobId);
        } catch (CommunicationException ex) {
            System.out.println(ex.getClass().getName());
            System.out.println(ex.getRootCause().getLocalizedMessage());
            System.out.println("\n*** A CommunicationException was raised.  This typically\n*** occurs when the target WebLogic server is not running.\n");
        } catch (Exception jobHistoryExp) {
            LOGGER.info(jobHistoryExp.getMessage());
            throw new RuntimeException(jobHistoryExp);
        }
        LOGGER.info("Exiting the method createJobHistory");
        return historyId;
    }

    /**
     * Method updates status, failed messasge, end date and last successfull
     * report run date
     *
     * @param historyId
     * @param failedMsg
     * @param status
     * @param reportSuccessFullRunDate
     */
    public void updateJobHistory(Integer historyId, String failedMsg, String status, Date reportSuccessFullRunDate) {
        LOGGER.info("Entering the method updateJobHistory");
        try {
            LOGGER.info("updateJobHistory() ====>historyId Id ==>" + historyId + " failedMsg :" + failedMsg
                    + " status:" + status);
            InitialContext ic = new InitialContext();
            QuartzJobHistoryFacadeREST historyObj = (QuartzJobHistoryFacadeREST) ic.lookup("java:comp/env/Scheduler/QuartzJobHistoryFacadeREST");
            QuartzJobHistory entity = null;
            if (historyId != null) {
                entity = historyObj.find(historyId);
            }
            if (entity != null) {
                java.util.Date date = new java.util.Date();
                Date endDate = new Timestamp(date.getTime());
                entity.setJobStatus(status);
                entity.setJobFailedReason(failedMsg);
                entity.setEndDate(endDate);
                if (reportSuccessFullRunDate != null) {
                    entity.setSuccessfulRunReportDate(reportSuccessFullRunDate);
                }
                historyObj.edit(entity);

            }
        } catch (Exception jobHistoryExp) {
            LOGGER.info(jobHistoryExp.getMessage());
            throw new RuntimeException(jobHistoryExp);
        }
        LOGGER.info("Exiting the method updateJobHistory");
    }

}
