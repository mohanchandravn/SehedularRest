/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.com;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jyomohan
 */
@Entity
@Table(name = "JOB_HISTORY")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuartzJobHistory.findAll", query = "SELECT q FROM QuartzJobHistory q where q.jobId=12322"),
    @NamedQuery(name = "QuartzJobHistory.findByJobHistoryId", query = "SELECT q FROM QuartzJobHistory q WHERE q.jobHistoryId = :jobHistoryId"),
    @NamedQuery(name = "QuartzJobHistory.findByStartDate", query = "SELECT q FROM QuartzJobHistory q WHERE q.startDate = :startDate"),
    @NamedQuery(name = "QuartzJobHistory.findByEndDate", query = "SELECT q FROM QuartzJobHistory q WHERE q.endDate = :endDate"),
    @NamedQuery(name = "QuartzJobHistory.findByJobStatus", query = "SELECT q FROM QuartzJobHistory q WHERE q.jobStatus = :jobStatus"),
    @NamedQuery(name = "QuartzJobHistory.findByJobFailedReason", query = "SELECT q FROM QuartzJobHistory q WHERE q.jobFailedReason = :jobFailedReason"),
    //@NamedQuery(name = "QuartzJobHistory.findBySuccessfulRunReportDate", query = "SELECT q FROM QuartzJobHistory q WHERE q.successfulRunReportDate = :successfulRunReportDate")
})
    @NamedNativeQueries({
    @NamedNativeQuery(name = "QuartzJobHistory.gethistorybyjobid",
            query = "select * from JOB_HISTORY where job_id=?jobid_bind",
            resultClass = QuartzJobHistory.class)      
})

//@SqlResultSetMappings({
//    @SqlResultSetMapping(name="ResultSetMapping.gethistorybyjobid", 
//                         classes = {@ConstructorResult(targetClass = QuartzJobHistoryInfo.class, 
//                         columns = {@ColumnResult(name="JOB_HISTORY_ID"), @ColumnResult(name="START_DATE"), @ColumnResult(name="END_DATE"), @ColumnResult(name="JOB_STATUS"), @ColumnResult(name="JOB_FAILED_REASON"), @ColumnResult(name="JOB_ID")})
//    })
//})
public class QuartzJobHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name="HISTORYSEQ_GEN", sequenceName="JOB_HISTORY_ID_S", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HISTORYSEQ_GEN")
    @Column(name = "JOB_HISTORY_ID")
    private Integer jobHistoryId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @Size(min = 1, max = 1)
    @Column(name = "JOB_STATUS")
    private String jobStatus;
    @Size(max = 4000)
    @Column(name = "JOB_FAILED_REASON")
    private String jobFailedReason;
    @Column(name = "SUCCESSFUL_RUN_REPORT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date successfulRunReportDate;
    

    @Column(name = "JOB_ID")
    private Integer jobId;
    
    public QuartzJobHistory() {
    }

    public QuartzJobHistory(Integer jobHistoryId) {
        this.jobHistoryId = jobHistoryId;
    }

    public QuartzJobHistory(Integer jobHistoryId, Date startDate, String jobStatus,Integer jobId) {
        this.jobHistoryId = jobHistoryId;
        this.startDate = startDate;
        this.jobStatus = jobStatus;
        this.jobId=jobId;
    }

    public Integer getJobHistoryId() {
        return jobHistoryId;
    }

    public void setJobHistoryId(Integer jobHistoryId) {
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

    public Date getSuccessfulRunReportDate() {
        return successfulRunReportDate;
    }

    public void setSuccessfulRunReportDate(Date successfulRunReportDate) {
        this.successfulRunReportDate = successfulRunReportDate;
    }

//    public QuartzJobConfiguration getJobId() {
//        return jobId;
//    }
//
//    public void setJobId(QuartzJobConfiguration jobId) {
//        this.jobId = jobId;
//    }
        public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobHistoryId != null ? jobHistoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuartzJobHistory)) {
            return false;
        }
        QuartzJobHistory other = (QuartzJobHistory) object;
        if ((this.jobHistoryId == null && other.jobHistoryId != null) || (this.jobHistoryId != null && !this.jobHistoryId.equals(other.jobHistoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracle.com.QuartzJobHistory[ jobHistoryId=" + jobHistoryId + " ]";
    }
    
}
