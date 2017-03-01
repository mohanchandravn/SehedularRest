/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.com;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jyomohan
 */
@Entity
@Table(name = "JOB_CONFIGURATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "QuartzJobConfiguration.findAll", query = "SELECT q FROM QuartzJobConfiguration q"),
    @NamedQuery(name = "QuartzJobConfiguration.findByJobId", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.jobId = :jobId"),
    @NamedQuery(name = "QuartzJobConfiguration.findByJobName", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.jobName = :jobName"),
    @NamedQuery(name = "QuartzJobConfiguration.findByJobDescription", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.jobDescription = :jobDescription"),
    @NamedQuery(name = "QuartzJobConfiguration.findByJobFrequency", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.jobFrequency = :jobFrequency"),
    @NamedQuery(name = "QuartzJobConfiguration.findByJobFrequencyType", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.jobFrequencyType = :jobFrequencyType"),
    @NamedQuery(name = "QuartzJobConfiguration.findByClassName", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.className = :className"),
    @NamedQuery(name = "QuartzJobConfiguration.findByJobFrequencyHour", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.jobFrequencyHour = :jobFrequencyHour"),
    @NamedQuery(name = "QuartzJobConfiguration.findByJobFrequencyMinute", query = "SELECT q FROM QuartzJobConfiguration q WHERE q.jobFrequencyMinute = :jobFrequencyMinute")})
  @NamedNativeQueries({
    @NamedNativeQuery(name = "QuartzJobConfiguration.getjobconfigbyjobname",
            query = "select * from job_configuration where upper(job_name) like ?jobname_bind",
            resultClass = QuartzJobConfiguration.class),
    @NamedNativeQuery(name = "QuartzJobConfiguration.getjobconfigbyjobstatus",
            query = "select * from job_configuration where upper(job_status) like ?jobstatus_bind",
            resultClass = QuartzJobConfiguration.class),
     @NamedNativeQuery(name = "QuartzJobConfiguration.getjobconfigbyname&status",
            query = "select * from job_configuration where upper(job_name) like ?jobname_bind and upper(job_status) like ?jobstatus_bind",
            resultClass = QuartzJobConfiguration.class)     
})


public class QuartzJobConfiguration implements Serializable {

    @OneToMany(mappedBy = "jobId")
    private Collection<Placeholders> placeholdersCollection;

    @OneToMany(mappedBy = "jobId")
    private Collection<RuleConfiguration> ruleConfigurationCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="SEQ_GEN", sequenceName="JOB_CONFIGURATION_ID_S", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_GEN")
    @Basic(optional = true)
    @Column(name = "JOB_ID")
    private Integer jobId;
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "JOB_NAME")
    private String jobName;
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "JOB_DESCRIPTION")
    private String jobDescription;
    @Basic(optional = true)
    @NotNull
    @Column(name = "JOB_FREQUENCY")
    private BigInteger jobFrequency;
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "JOB_FREQUENCY_TYPE")
    private String jobFrequencyType;
    @Basic(optional = true)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "CLASS_NAME")
    private String className;
    @Column(name = "JOB_FREQUENCY_HOUR")
    private Short jobFrequencyHour;
    @Column(name = "JOB_FREQUENCY_MINUTE")
    private Short jobFrequencyMinute;
      @Basic(optional = true)
    @Column(name = "JOB_STATUS")
    private String jobStatus;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "jobId")
//    private Collection<QuartzJobHistory> quartzJobHistoryCollection;

    public QuartzJobConfiguration() {
    }

    public QuartzJobConfiguration(Integer jobId) {
        this.jobId = jobId;
    }

    public QuartzJobConfiguration(Integer jobId, String jobName, String jobDescription, BigInteger jobFrequency, String jobFrequencyType, String className,String jobStatus) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobDescription = jobDescription;
        this.jobFrequency = jobFrequency;
        this.jobFrequencyType = jobFrequencyType;
        this.className = className;
        this.jobStatus=jobStatus;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public BigInteger getJobFrequency() {
        return jobFrequency;
    }

    public void setJobFrequency(BigInteger jobFrequency) {
        this.jobFrequency = jobFrequency;
    }

    public String getJobFrequencyType() {
        return jobFrequencyType;
    }

    public void setJobFrequencyType(String jobFrequencyType) {
        this.jobFrequencyType = jobFrequencyType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Short getJobFrequencyHour() {
        return jobFrequencyHour;
    }

    public void setJobFrequencyHour(Short jobFrequencyHour) {
        this.jobFrequencyHour = jobFrequencyHour;
    }

    public Short getJobFrequencyMinute() {
        return jobFrequencyMinute;
    }

    public void setJobFrequencyMinute(Short jobFrequencyMinute) {
        this.jobFrequencyMinute = jobFrequencyMinute;
    }
    
    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

//    @XmlTransient
//    public Collection<QuartzJobHistory> getQuartzJobHistoryCollection() {
//        return quartzJobHistoryCollection;
//    }
//
//    public void setQuartzJobHistoryCollection(Collection<QuartzJobHistory> quartzJobHistoryCollection) {
//        this.quartzJobHistoryCollection = quartzJobHistoryCollection;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (jobId != null ? jobId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuartzJobConfiguration)) {
            return false;
        }
        QuartzJobConfiguration other = (QuartzJobConfiguration) object;
        if ((this.jobId == null && other.jobId != null) || (this.jobId != null && !this.jobId.equals(other.jobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracle.com.QuartzJobConfiguration[ jobId=" + jobId + " ]";
    } 

    @XmlTransient
    public Collection<RuleConfiguration> getRuleConfigurationCollection() {
        return ruleConfigurationCollection;
    }

    public void setRuleConfigurationCollection(Collection<RuleConfiguration> ruleConfigurationCollection) {
        this.ruleConfigurationCollection = ruleConfigurationCollection;
    }

    @XmlTransient
    public Collection<Placeholders> getPlaceholdersCollection() {
        return placeholdersCollection;
    }

    public void setPlaceholdersCollection(Collection<Placeholders> placeholdersCollection) {
        this.placeholdersCollection = placeholdersCollection;
    }
    
}
