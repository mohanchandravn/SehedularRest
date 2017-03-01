/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.com;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vaduri
 */
@Entity
@Table(name = "RULE_CONFIGURATION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RuleConfiguration.findAll", query = "SELECT r FROM RuleConfiguration r")
    , @NamedQuery(name = "RuleConfiguration.findByRuleKey", query = "SELECT r FROM RuleConfiguration r WHERE r.ruleKey = :ruleKey")
    , @NamedQuery(name = "RuleConfiguration.findByRuleType", query = "SELECT r FROM RuleConfiguration r WHERE r.ruleType = :ruleType")
    , @NamedQuery(name = "RuleConfiguration.findByRuleValue", query = "SELECT r FROM RuleConfiguration r WHERE r.ruleValue = :ruleValue")
    , @NamedQuery(name = "RuleConfiguration.findByIsUpdatable", query = "SELECT r FROM RuleConfiguration r WHERE r.isUpdatable = :isUpdatable")
    , @NamedQuery(name = "RuleConfiguration.findByUiLabel", query = "SELECT r FROM RuleConfiguration r WHERE r.uiLabel = :uiLabel")
    })

 @NamedNativeQueries({
    @NamedNativeQuery(name = "RuleConfiguration.getRulesConfigInfoByJobId",
            query = "select * from RULE_CONFIGURATION where JOB_ID = ?jobId_bind order by rule_key asc",
            resultClass = RuleConfiguration.class),
    @NamedNativeQuery(name = "RuleConfiguration.updateRuleConfigByJobId",
            query = "update RULE_CONFIGURATION set rule_value = ?rule_value_bind where ui_label = ?ui_label_bind and job_id = ?jobId_bind") 
 })
     
public class RuleConfiguration implements Serializable {

 

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "RULE_KEY")
    private String ruleKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "RULE_TYPE")
    private String ruleType;
    @Size(max = 4000)
    @Column(name = "RULE_VALUE")
    private String ruleValue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "IS_UPDATABLE")
    private String isUpdatable;
    @Size(max = 50)
    @Column(name = "UI_LABEL")
    private String uiLabel;
    @Size(max = 20)
    @Column(name = "INPUT_FIELD_TYPE")
    private String inputFieldType;
    @JoinColumn(name = "JOB_ID", referencedColumnName = "JOB_ID")
    @ManyToOne
    private QuartzJobConfiguration jobId;

    public RuleConfiguration() {
    }

    public RuleConfiguration(String ruleKey) {
        this.ruleKey = ruleKey;
    }

    public RuleConfiguration(String ruleKey, String ruleType, String isUpdatable) {
        this.ruleKey = ruleKey;
        this.ruleType = ruleType;
        this.isUpdatable = isUpdatable;
    }

    public String getRuleKey() {
        return ruleKey;
    }

    public void setRuleKey(String ruleKey) {
        this.ruleKey = ruleKey;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getIsUpdatable() {
        return isUpdatable;
    }

    public void setIsUpdatable(String isUpdatable) {
        this.isUpdatable = isUpdatable;
    }

    public String getUiLabel() {
        return uiLabel;
    }

    public void setUiLabel(String uiLabel) {
        this.uiLabel = uiLabel;
    }

    public QuartzJobConfiguration getJobId() {
        return jobId;
    }

    public void setJobId(QuartzJobConfiguration jobId) {
        this.jobId = jobId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ruleKey != null ? ruleKey.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RuleConfiguration)) {
            return false;
        }
        RuleConfiguration other = (RuleConfiguration) object;
        if ((this.ruleKey == null && other.ruleKey != null) || (this.ruleKey != null && !this.ruleKey.equals(other.ruleKey))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracle.com.RuleConfiguration[ ruleKey=" + ruleKey + " ]";
    }

    public String getInputFieldType() {
        return inputFieldType;
    }

    public void setInputFieldType(String inputFieldType) {
        this.inputFieldType = inputFieldType;
    }
    
}
