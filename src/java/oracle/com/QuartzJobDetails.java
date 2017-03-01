/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.com;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jyomohan
 */
@Entity
@Table(name = "QRTZ_JOB_DETAILS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuartzJobDetails.findAll", query = "SELECT q FROM QuartzJobDetails q"),
    @NamedQuery(name = "QuartzJobDetails.findBySchedName", query = "SELECT q FROM QuartzJobDetails q WHERE q.quartzJobDetailsPK.schedName = :schedName"),
    @NamedQuery(name = "QuartzJobDetails.findByJobName", query = "SELECT q FROM QuartzJobDetails q WHERE q.quartzJobDetailsPK.jobName = :jobName"),
    @NamedQuery(name = "QuartzJobDetails.findByJobGroup", query = "SELECT q FROM QuartzJobDetails q WHERE q.quartzJobDetailsPK.jobGroup = :jobGroup"),
    @NamedQuery(name = "QuartzJobDetails.findByDescription", query = "SELECT q FROM QuartzJobDetails q WHERE q.description = :description"),
    @NamedQuery(name = "QuartzJobDetails.findByJobClassName", query = "SELECT q FROM QuartzJobDetails q WHERE q.jobClassName = :jobClassName"),
    @NamedQuery(name = "QuartzJobDetails.findByIsDurable", query = "SELECT q FROM QuartzJobDetails q WHERE q.isDurable = :isDurable"),
    @NamedQuery(name = "QuartzJobDetails.findByIsNonconcurrent", query = "SELECT q FROM QuartzJobDetails q WHERE q.isNonconcurrent = :isNonconcurrent"),
    @NamedQuery(name = "QuartzJobDetails.findByIsUpdateData", query = "SELECT q FROM QuartzJobDetails q WHERE q.isUpdateData = :isUpdateData"),
    @NamedQuery(name = "QuartzJobDetails.findByRequestsRecovery", query = "SELECT q FROM QuartzJobDetails q WHERE q.requestsRecovery = :requestsRecovery")})
public class QuartzJobDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected QuartzJobDetailsPK quartzJobDetailsPK;
    @Size(max = 250)
    @Column(name = "DESCRIPTION")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "JOB_CLASS_NAME")
    private String jobClassName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "IS_DURABLE")
    private String isDurable;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "IS_NONCONCURRENT")
    private String isNonconcurrent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "IS_UPDATE_DATA")
    private String isUpdateData;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "REQUESTS_RECOVERY")
    private String requestsRecovery;
    @Lob
    @Column(name = "JOB_DATA")
    private Serializable jobData;

    public QuartzJobDetails() {
    }

    public QuartzJobDetails(QuartzJobDetailsPK quartzJobDetailsPK) {
        this.quartzJobDetailsPK = quartzJobDetailsPK;
    }

    public QuartzJobDetails(QuartzJobDetailsPK quartzJobDetailsPK, String jobClassName, String isDurable, String isNonconcurrent, String isUpdateData, String requestsRecovery) {
        this.quartzJobDetailsPK = quartzJobDetailsPK;
        this.jobClassName = jobClassName;
        this.isDurable = isDurable;
        this.isNonconcurrent = isNonconcurrent;
        this.isUpdateData = isUpdateData;
        this.requestsRecovery = requestsRecovery;
    }

    public QuartzJobDetails(String schedName, String jobName, String jobGroup) {
        this.quartzJobDetailsPK = new QuartzJobDetailsPK(schedName, jobName, jobGroup);
    }

    public QuartzJobDetailsPK getQuartzJobDetailsPK() {
        return quartzJobDetailsPK;
    }

    public void setQuartzJobDetailsPK(QuartzJobDetailsPK quartzJobDetailsPK) {
        this.quartzJobDetailsPK = quartzJobDetailsPK;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getIsDurable() {
        return isDurable;
    }

    public void setIsDurable(String isDurable) {
        this.isDurable = isDurable;
    }

    public String getIsNonconcurrent() {
        return isNonconcurrent;
    }

    public void setIsNonconcurrent(String isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent;
    }

    public String getIsUpdateData() {
        return isUpdateData;
    }

    public void setIsUpdateData(String isUpdateData) {
        this.isUpdateData = isUpdateData;
    }

    public String getRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(String requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }

    public Serializable getJobData() {
        return jobData;
    }

    public void setJobData(Serializable jobData) {
        this.jobData = jobData;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (quartzJobDetailsPK != null ? quartzJobDetailsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuartzJobDetails)) {
            return false;
        }
        QuartzJobDetails other = (QuartzJobDetails) object;
        if ((this.quartzJobDetailsPK == null && other.quartzJobDetailsPK != null) || (this.quartzJobDetailsPK != null && !this.quartzJobDetailsPK.equals(other.quartzJobDetailsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracle.com.QuartzJobDetails[ quartzJobDetailsPK=" + quartzJobDetailsPK + " ]";
    }
    
}
