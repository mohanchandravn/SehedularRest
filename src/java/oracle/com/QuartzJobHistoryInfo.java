package oracle.com;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author jyomohan
 */
public class QuartzJobHistoryInfo {
    @Column(name = "JOB_HISTORY_ID")
    private BigDecimal jobHistoryId;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @Column(name = "JOB_STATUS")
    private String jobStatus;
    @Column(name = "JOB_FAILED_REASON")
    private String jobFailedReason;
    @Column(name = "SUCCESSFUL_RUN_REPORT_DATE")
    private String successfulRunReportDate;
    @Column(name = "JOB_ID")
    private BigDecimal jobId;
    public QuartzJobHistoryInfo()
    {
        
    }
     public QuartzJobHistoryInfo(BigDecimal jobHistoryId, Date startDate,Date endDate,String jobStatus,String jobFailedReason,BigDecimal jobId) {
        this.jobHistoryId = jobHistoryId;
        this.startDate = startDate;
        this.endDate=endDate;
        this.jobStatus=jobStatus;
        this.jobFailedReason=jobFailedReason;
       // this.successfulRunReportDate=successfulRunReportDate;
        this.jobId=jobId;
        this.jobStatus = jobStatus;
    }

    public BigDecimal getJobHistoryId() {
        return jobHistoryId;
    }

    public void setJobHistoryId(BigDecimal jobHistoryId) {
        this.jobHistoryId = jobHistoryId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobFailedReason() {
        return jobFailedReason;
    }

    public void setJobFailedReason(String jobFailedReason) {
        this.jobFailedReason = jobFailedReason;
    }

    public String getSuccessfulRunReportDate() {
        return successfulRunReportDate;
    }

    public void setSuccessfulRunReportDate(String successfulRunReportDate) {
        this.successfulRunReportDate = successfulRunReportDate;
    }

    public BigDecimal getJobId() {
        return jobId;
    }

    public void setJobId(BigDecimal jobId) {
        this.jobId = jobId;
    }

    
}
