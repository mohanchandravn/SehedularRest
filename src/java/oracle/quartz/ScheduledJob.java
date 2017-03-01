/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.quartz;
import java.util.Date;



/**
 * This class represents a scheduled object in Quartz framework.
 * The object is used by the ScheduledJobsVO to encapsulate details of a
 * scheduled job during processing.
 */
public class ScheduledJob {

    /**
     * Variable delcaration for jobName.
     */
    private String jobName = null;

    /**
     * Variable delcaration for groupName.
     */
    private String groupName = null;

    /**
     * Variable delcaration for nextExecution.
     */
    private Date nextExecution = null;

    /**
     * Variable delcaration for jobClass.
     */
    private String jobClass = null;

    /**
     * Default constructor
     * @param jobName Name of the scheduled job
     * @param groupName Group to which the scheduled job belongs to
     * @param nextExecution The time of next execution for the job
     * @param jobClass Name of the class executed for the job
     */
    public ScheduledJob(String jobName, String groupName, Date nextExecution, String jobClass) {
        super();
        this.jobName = jobName;
        this.groupName = groupName;
        this.nextExecution = nextExecution;
        this.jobClass = jobClass;
    }

    /**
     * Setter for a variable.
     * @param jobName value for the variable
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Setter for a variable.
     * @param groupName value for the variable
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Setter for a variable.
     * @param nextExecution value for the variable
     */
    public void setNextExecution(Date nextExecution) {
        this.nextExecution = nextExecution;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public Date getNextExecution() {
        return nextExecution;
    }

    /**
     * Setter for a variable.
     * @param jobClass value for the variable
     */
    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    /**
     * Getter for a variable.
     * @return value of the variable
     */
    public String getJobClass() {
        return jobClass;
    }

    /**
     * toString method for the object.
     * @return
     */
    @Override
    public String toString() {
        String nextExecutionFormatted = (null == nextExecution ? "null" : String.format("%tc", nextExecution));
        return jobName + " " + groupName + " " + nextExecutionFormatted + " " + jobClass;
    }

}
