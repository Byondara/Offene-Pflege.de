/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package entity.nursingprocess;

import entity.Users;
import op.OPDE;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tloehr
 */
@Entity
@Table(name = "PlanKontrolle")
@NamedQueries({
        @NamedQuery(name = "PlanKontrolle.findAll", query = "SELECT p FROM NPControl p"),
        @NamedQuery(name = "PlanKontrolle.findByPKonID", query = "SELECT p FROM NPControl p WHERE p.pKonID = :pKonID"),
        @NamedQuery(name = "PlanKontrolle.findByDatum", query = "SELECT p FROM NPControl p WHERE p.datum = :datum"),
        @NamedQuery(name = "PlanKontrolle.findByAbschluss", query = "SELECT p FROM NPControl p WHERE p.abschluss = :abschluss")})
public class NPControl implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PKonID")
    private Long pKonID;
    @Lob
    @Column(name = "Bemerkung")
    private String bemerkung;
    @Basic(optional = false)
    @Column(name = "Datum")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;
    @Column(name = "Abschluss")
    private Boolean abschluss;

    @JoinColumn(name = "PlanID", referencedColumnName = "PlanID")
    @ManyToOne
    private NursingProcess nursingProcess;

    @JoinColumn(name = "UKennung", referencedColumnName = "UKennung")
    @ManyToOne
    private Users user;

    public NPControl() {
    }

    public NPControl(String bemerkung, NursingProcess nursingProcess) {
        this.bemerkung = bemerkung;
        this.abschluss = true;
        this.nursingProcess = nursingProcess;
        this.user = OPDE.getLogin().getUser();
        this.datum = new Date();
    }

    public Long getPKonID() {
        return pKonID;
    }

    public void setPKonID(Long pKonID) {
        this.pKonID = pKonID;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Boolean isAbschluss() {
        return abschluss;
    }

    public void setAbschluss(Boolean abschluss) {
        this.abschluss = abschluss;
    }

    public NursingProcess getNursingProcess() {
        return nursingProcess;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pKonID != null ? pKonID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof NPControl)) {
            return false;
        }
        NPControl other = (NPControl) object;
        if ((this.pKonID == null && other.pKonID != null) || (this.pKonID != null && !this.pKonID.equals(other.pKonID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.rest.PlanKontrolle[pKonID=" + pKonID + "]";
    }

}