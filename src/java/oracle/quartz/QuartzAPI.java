    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.quartz;



import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.com.QuartzJobConfiguration;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;


import oracle.quartz.ScheduledJob;

/**
 *
 * @author jyomohan
 */
public class QuartzAPI {
    private static final Logger LOGGER = Logger.getLogger(QuartzAPI.class.getName()); 

    /**
     * Method runs the job immediately onetime only.
     */
    public void runJobNow(String jobName,String className) {
        LOGGER.info("Entering the method runJobNow");
            QuartzSchedulerSingleton quartzSchedulerSingleton = QuartzSchedulerSingleton.getInstance();
            try {
                LOGGER.info("triggering the job now");
                boolean isExist = false;
                List<ScheduledJob> scheduledJobs = quartzSchedulerSingleton.getScheduledJobs();
                if (!scheduledJobs.isEmpty()) {
                    for (ScheduledJob scheduledJob : scheduledJobs) {
                        if (scheduledJob != null) {
                            String scheduledJobName = scheduledJob.getJobName();
                            if (jobName.equals(scheduledJobName)) {
                                LOGGER.info("Job found in the quartz");
                                isExist = true;
                                break;
                            }
                        }
                    }
                }
                if (isExist) {
                    LOGGER.info("Triggering the job");
                    JobKey jobKey = new JobKey(jobName, Scheduler.DEFAULT_GROUP);
                    quartzSchedulerSingleton.getScheduler().triggerJob(jobKey);
                } else {
                    LOGGER.info("Triggering the job");
                    Class jobClass = Class.forName(className);
                    JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName).build();
                    Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName).startNow().forJob(job).build();
                    quartzSchedulerSingleton.getScheduler().scheduleJob(job, trigger);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, null, e);
               e.printStackTrace();
            }
     
        LOGGER.info("Exiting the method runJobNow");
    }

    /**
     * Method stops the job immediately.It removes the job from quartz scheduler.
     * It does not remove the job from the Job Configuration table.
     */
    public void stopJobNow(String jobName) {
        //jobName="TestJob";
        LOGGER.info("Entering the method stopJobNow");
            QuartzSchedulerSingleton quartzSchedulerSingleton = QuartzSchedulerSingleton.getInstance();
            List<ScheduledJob> scheduledJobs = quartzSchedulerSingleton.getScheduledJobs();
            if (!scheduledJobs.isEmpty()) {
                for (ScheduledJob scheduledJob : scheduledJobs) {
                    if (scheduledJob != null) {
                        String scheduledJobName = scheduledJob.getJobName();
                        if (jobName.equals(scheduledJobName)) {
                            LOGGER.info("Deleting the job.");
                            quartzSchedulerSingleton.deleteScheduledJob(jobName, scheduledJob.getGroupName());
                            break;
                        }
                    }
                }
            }
       
        LOGGER.info("Entering the method stopJobNow");
    }

    /**
     * Method deletes the job and job history both from quartz and job configuration
     * table when the user confirms the delete.
     */
    public void deleteJob(String jobName) {
        LOGGER.info("Entering the method deleteJob");
            QuartzSchedulerSingleton quartzSchedulerSingleton = QuartzSchedulerSingleton.getInstance();
            List<ScheduledJob> scheduledJobs = quartzSchedulerSingleton.getScheduledJobs();
            if (!scheduledJobs.isEmpty()) {
                for (ScheduledJob scheduledJob : scheduledJobs) {
                    if (scheduledJob != null) {
                        String scheduledJobName = scheduledJob.getJobName();
                        if (jobName.equals(scheduledJobName)) {
                            LOGGER.info("Deleting the job.");
                            quartzSchedulerSingleton.deleteScheduledJob(jobName, scheduledJob.getGroupName());
                            break;
                        }
                    }
                }
            }
        LOGGER.info("Entering the method deleteJob");
    }

    /**
     * Method saves the job and schedules the job with the quartz scheduler.
     */
    public void saveAndRunJob(QuartzJobConfiguration entity) {
        LOGGER.info("Entering the method saveAndRunJob");
        try
        {  
            if (entity != null) {
                String jobName = null;
                BigInteger jobFrequency = null;
                String jobFrequencyType = null;
                Integer jobFrequencyHour=null;
                Integer jobFrequencyMinute=null; 
                String jobClass=null;
                if (entity.getJobName() != null) {
                    jobName = entity.getJobName();
                }
                if (entity.getJobFrequency() != null) //BigDecimal
                {
                    jobFrequency = entity.getJobFrequency();
                }
                if (entity.getJobFrequencyType() != null) {
                    jobFrequencyType = entity.getJobFrequencyType();
                }
                if(entity.getJobFrequencyHour()!=null)
                {
                  jobFrequencyHour=entity.getJobFrequencyHour().intValue(); 
                }
                  if(entity.getJobFrequencyMinute()!=null)
                {
                  jobFrequencyMinute=entity.getJobFrequencyMinute().intValue(); 
                }  
                  if(entity.getClassName()!=null)
                {
                  jobClass=entity.getClassName();
                }  
                LOGGER.info("jobName :"+jobName+" jobFrequency :"+jobFrequency+" jobFrequencyType:"+jobFrequencyType+" jobFrequencyHour:"+jobFrequencyHour+" jobFrequencyMinute:"+jobFrequencyMinute+" jobClass:"+jobClass);
                String _cronScheduler = null;
                if ("H".equals(jobFrequencyType)) {
                    int frequency = jobFrequency.intValue();
                    if (frequency != 0) {
                        _cronScheduler = "0 0 0/" + frequency + " * * ?";
                    } else {
                        _cronScheduler = null;
                    }
                } else if ("D".equals(jobFrequencyType)) {
                    int minute = jobFrequencyMinute.intValue();
                    int hour = jobFrequencyHour.intValue();
                    _cronScheduler = "0 " + minute + " " + hour + " * * ?";
                } 
//                else if("M".equals(jobFrequencyType)){
//                     _cronScheduler = "0 0,5,10,15,20,25,30,35,40,45,50,55 * * * ?";
//                
//                }
               // byte entityState = currentRow.getEntity(0).getEntityState();
                QuartzSchedulerSingleton quartzSchedulerSingleton = QuartzSchedulerSingleton.getInstance();
               // if (Entity.STATUS_NEW == entityState) {
                    LOGGER.info("Scheduling the new job.");
                    if (_cronScheduler != null) {
                        _cronScheduler="0/30 * * * * ?";
                        quartzSchedulerSingleton.scheduleJob(jobName, jobClass, _cronScheduler);
                    }
//                } else if (Entity.STATUS_MODIFIED == entityState) {
//                    List<ScheduledJob> scheduledJobs = quartzSchedulerSingleton.getScheduledJobs();
//                    if (!scheduledJobs.isEmpty()) {
//                        for (ScheduledJob scheduledJob : scheduledJobs) {
//                            if (scheduledJob != null) {
//                                String scheduledJobName = scheduledJob.getJobName();
//                                if (jobName.equals(scheduledJobName)) {
//                                    LOGGER.info("Deleting the job.");
//                                    quartzSchedulerSingleton.deleteScheduledJob(jobName, scheduledJob.getGroupName());
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                    if (_cronScheduler != null) {
//                        LOGGER.info("Scheduling the job.");
//                        quartzSchedulerSingleton.scheduleJob(jobName, jobClass, _cronScheduler);
//                    
//                }
//                JobKey jobKey = new JobKey(jobName, Scheduler.DEFAULT_GROUP);
//                quartzSchedulerSingleton.getScheduler().triggerJob(jobKey);
//            }
        }
        }catch(Exception ex) {
                 LOGGER.log(Level.SEVERE, null, ex);
                 ex.printStackTrace();
                 throw new RuntimeException(ex);
                
            }
        
        LOGGER.info("Exiting the method saveAndRunJob");
    }

}