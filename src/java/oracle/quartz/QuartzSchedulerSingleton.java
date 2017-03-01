/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;


/**
 * Logic to interact with the Quartz library.
 * Implemented using the singleton pattern. Logic is accessed from both model
 * and view layers
 */
public class QuartzSchedulerSingleton {
    private static final Logger LOGGER = Logger.getLogger(QuartzSchedulerSingleton.class.getName());

    /**
     * Singleton static instance for the class.
     */
    private static QuartzSchedulerSingleton _instance = null;

    /**
     * Static instance of the scheduler factory initialized based on configuration
     * file uploaded to JCS. Instance is shared for all code in the sample.
     */
    private static StdSchedulerFactory _schedulerFactory = null;

    /**
     * Static instance for the shared scheduler. Created and started when singleton is created.
     */
    private static Scheduler _scheduler = null;

    /**
     * Default constructor
     * Defined as protected to prevent instantiation
     */
    protected QuartzSchedulerSingleton() {
        LOGGER.info("QuartzSchedulerSingleton.constructor Start ");
        try {
            /*
             * Obtain instance of the Scheduler Factory and initialize it based
             * on the configuration file uploaded to JCS. In this sample the
             * file location is hard coded as the location accessible in JCS
             * is fixed, however this could be based on for example a system
             * property. For this to work the file must exist in the given
             * location. See the configuration steps in the blog post for steps
             * to upload it.
             * Alternatively the properties object could be populated RT, however
             * that would mean that if something changes the application would
             * need to be re-deployed.
             */
            _schedulerFactory = new StdSchedulerFactory();
            // _schedulerFactory.initialize("/customer/scratch/quartz.properties");
            _schedulerFactory.initialize("quartz.properties"); //without properties it doesn't work
            _scheduler = _schedulerFactory.getScheduler();
            // Scheduler needs to be started for it to execute jobs
            _scheduler.start();
        } catch (Exception e) {
             LOGGER.log(Level.SEVERE, null, e);
             e.printStackTrace();
             throw new RuntimeException(e);
        }
        LOGGER.info("QuartzSchedulerSingleton.constructor End ");
    }

    /**
     * Get the instance of the singleton
     * @return instance of the singleton
     */
    public static QuartzSchedulerSingleton getInstance() {
        LOGGER.info("Entering the method QuartzSchedulerSingleton");
        if (_instance == null) {
            LOGGER.info("QuartzSchedulerSingleton.getInstance create new ");
            _instance = new QuartzSchedulerSingleton();
        }
        LOGGER.info("Exiting the method QuartzSchedulerSingleton");
        return _instance;
    }

    /**
     * Get the scheduler initialized based on the properties uploaded to JCS.
     * @return initialized scheduler
     */
    public Scheduler getScheduler() {
        return _scheduler;
    }

    /**
     * Gets a list of scheduled jobs
     * @return list of scheduler jobs
     */
    public List<ScheduledJob> getScheduledJobs() {
        LOGGER.info("QuartzSchedulerSingleton.getScheduledJobs Start ");
        List<ScheduledJob> result = new ArrayList<ScheduledJob>();
        try {
            // Loop through the groups and all jobs for each group
            for (String groupName : _scheduler.getJobGroupNames()) {
                for (JobKey jobKey : _scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();

                    // Get the trigger for the job to determine next execution
                    List<Trigger> triggers = (List<Trigger>) _scheduler.getTriggersOfJob(jobKey);
                    Date nextFireTime = triggers.get(0).getNextFireTime();

                    // Get the JobDetail to obtain the name of the class for the job
                    JobDetail jobDetail = _scheduler.getJobDetail(jobKey);

                    // Add a POJO object to the list containing the details of the scheduled jobs
                    ScheduledJob temp =
                        new ScheduledJob(jobName, jobGroup, nextFireTime, jobDetail.getJobClass().getName());
                    LOGGER.info("QuartzSchedulerSingleton.getScheduledJobs job : " + temp);
                    result.add(temp);
                }
            }
        } catch (Exception e) {
           LOGGER.log(Level.SEVERE, null, e);
             e.printStackTrace();
             throw new RuntimeException(e);
        }
        LOGGER.info("QuartzSchedulerSingleton.getScheduledJobs result " + result);
        return result;
    }

    /**
     * Delete scheduled job
     * @param jobName name of the job to be deleted
     * @param groupName group name of the job to be deleted
     */
    public void deleteScheduledJob(String jobName, String groupName) {
        LOGGER.info("QuartzSchedulerSingleton.deleteScheduledJob Start; " + jobName + " " + groupName);
        try {
            // Construct the key for the job to be deleted
            JobKey jobKey = new JobKey(jobName, groupName);

            // Delete the job
            _scheduler.deleteJob(jobKey);
        } catch (Exception e) {
           LOGGER.log(Level.SEVERE, null, e);
             e.printStackTrace();
             throw new RuntimeException(e);
        }
        LOGGER.info("QuartzSchedulerSingleton.deleteScheduledJob End ");
    }

    /**
     * Schedule a job
     * @param jobName name of the job to be scheduled
     * @param jobClassName class of the job to be schedule
     * @param cronSchedule the cron based schedule for the job
     */
    public void scheduleJob(String jobName, String jobClassName, String cronSchedule) {
        LOGGER.info("QuartzSchedulerSingleton.scheduleJob Start; " + jobName + " " + jobClassName + " " + cronSchedule);
        try {
            Class jobClass = Class.forName(jobClassName);

            // Create the job
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName).build();
            
            // Create a new cron based schedule
            Trigger trigger =
                TriggerBuilder.newTrigger().withIdentity(jobName).withSchedule(CronScheduleBuilder.cronSchedule(cronSchedule)).startNow().build();

            _scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
             e.printStackTrace();
             throw new RuntimeException(e);
        }
        LOGGER.info("QuartzSchedulerSingleton.scheduleJob End ");
    }
}
