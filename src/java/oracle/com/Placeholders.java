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
import oracle.com.QuartzJobConfiguration;

/**
 *
 * @author vaduri
 */
@Entity
@Table(name = "PLACEHOLDERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Placeholders.findAll", query = "SELECT p FROM Placeholders p")
    , @NamedQuery(name = "Placeholders.findByPlaceholderCode", query = "SELECT p FROM Placeholders p WHERE p.placeholderCode = :placeholderCode")
    , @NamedQuery(name = "Placeholders.findByPlaceholderDescription", query = "SELECT p FROM Placeholders p WHERE p.placeholderDescription = :placeholderDescription")})
@NamedNativeQueries({
    @NamedNativeQuery(name = "Placeholders.getPlaceholdersByJobId",
            query = "select * from PLACEHOLDERS where JOB_ID = ?jobId_bind order by placeholder_code asc",
            resultClass = Placeholders.class)})

public class Placeholders implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PLACEHOLDER_CODE")
    private String placeholderCode;
    @Size(max = 200)
    @Column(name = "PLACEHOLDER_DESCRIPTION")
    private String placeholderDescription;
    @JoinColumn(name = "JOB_ID", referencedColumnName = "JOB_ID")
    @ManyToOne
    private QuartzJobConfiguration jobId;

    public Placeholders() {
    }

    public Placeholders(String placeholderCode) {
        this.placeholderCode = placeholderCode;
    }

    public String getPlaceholderCode() {
        return placeholderCode;
    }

    public void setPlaceholderCode(String placeholderCode) {
        this.placeholderCode = placeholderCode;
    }

    public String getPlaceholderDescription() {
        return placeholderDescription;
    }

    public void setPlaceholderDescription(String placeholderDescription) {
        this.placeholderDescription = placeholderDescription;
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
        hash += (placeholderCode != null ? placeholderCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Placeholders)) {
            return false;
        }
        Placeholders other = (Placeholders) object;
        if ((this.placeholderCode == null && other.placeholderCode != null) || (this.placeholderCode != null && !this.placeholderCode.equals(other.placeholderCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "oracle.com.service.Placeholders[ placeholderCode=" + placeholderCode + " ]";
    }
    
}
