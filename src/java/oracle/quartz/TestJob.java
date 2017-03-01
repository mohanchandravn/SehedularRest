/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.quartz;

import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Logger;
import oracle.com.service.QuartzJobConfigurationFacadeREST;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author jyomohan
 */
public class TestJob extends BaseJob {

    private static final Logger LOGGER = Logger.getLogger(TestJob.class.getName());

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        super.execute(jec); //To change body of generated methods, choose Tools | Templates.
        LOGGER.info("Implementation code goes here");
//        String failedMsg="ERROR";
        String failedMsg = "SUCCESS";
        if ("ERROR".equals(failedMsg)) {
            updateJobHistory(jobHistoryId, failedMsg, "F", null);
        } else if ("SUCCESS".equals(failedMsg)) {
            updateJobHistory(jobHistoryId, failedMsg, "C", null);
        }

    }
}
