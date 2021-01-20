/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author DxGod
 */
@Entity
@Table(name = "POSTURI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Posturi.findAll", query = "SELECT p FROM Posturi p")
    , @NamedQuery(name = "Posturi.findById", query = "SELECT p FROM Posturi p WHERE p.id = :id")
    , @NamedQuery(name = "Posturi.findByCerinteMinime", query = "SELECT p FROM Posturi p WHERE p.cerinteMinime = :cerinteMinime")
    , @NamedQuery(name = "Posturi.findByCerinteOptionale", query = "SELECT p FROM Posturi p WHERE p.cerinteOptionale = :cerinteOptionale")
    , @NamedQuery(name = "Posturi.findByDataLimAplic", query = "SELECT p FROM Posturi p WHERE p.dataLimAplic = :dataLimAplic")
    , @NamedQuery(name = "Posturi.findByDeschisDe", query = "SELECT p FROM Posturi p WHERE p.deschisDe = :deschisDe")
    , @NamedQuery(name = "Posturi.findByDenumire", query = "SELECT p FROM Posturi p WHERE p.denumire = :denumire")})
public class Posturi implements Serializable {

    @OneToMany(mappedBy = "idPost")
    private Collection<Aplicanti> aplicantiCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "CERINTE_MINIME")
    private String cerinteMinime;
    @Size(max = 255)
    @Column(name = "CERINTE_OPTIONALE")
    private String cerinteOptionale;
    @Column(name = "DATA_LIM_APLIC")
    @Temporal(TemporalType.DATE)
    private Date dataLimAplic;
    @Size(max = 255)
    @Column(name = "DENUMIRE")
    private String denumire;
    @JoinColumn(name = "DESCHIS_DE", referencedColumnName = "ID")
    @ManyToOne
    private Users deschisDe;

    public Posturi() {
    }

    public Posturi(Integer id) {
        this.id = id;
    }

    public Posturi(int id, String denumire, String cerinteMinime, String cerinteOptionale, Date dataLimAplic, Users user) {
       this.id = id;
       this.denumire= denumire;
       this.cerinteMinime = cerinteMinime;
       this.cerinteOptionale = cerinteOptionale;
       this.dataLimAplic = dataLimAplic;
       this.deschisDe= user;
       
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCerinteMinime() {
        return cerinteMinime;
    }

    public void setCerinteMinime(String cerinteMinime) {
        this.cerinteMinime = cerinteMinime;
    }

    public String getCerinteOptionale() {
        return cerinteOptionale;
    }

    public void setCerinteOptionale(String cerinteOptionale) {
        this.cerinteOptionale = cerinteOptionale;
    }

    public Date getDataLimAplic() {
        return dataLimAplic;
    }

    public void setDataLimAplic(Date dataLimAplic) {
        this.dataLimAplic = dataLimAplic;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }


    public Users getDeschisDe() {
        return deschisDe;
    }

    public void setDeschisDe(Users deschisDe) {
        this.deschisDe = deschisDe;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Posturi)) {
            return false;
        }
        Posturi other = (Posturi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Posturi[ id=" + id + " ]";
    }

    /**
     *  Verifica daca data limita aplicarii din baza de date este dupa data 
     * de azi
     *
     * @return  <code>true</code> daca data limita aplicarii a trecut de ziua curenta;
     *          <code>false</code> daca nu a trecut.
     */
    public Boolean isValabil() {
        Date todayDate = new Date();
        return dataLimAplic.after(todayDate);
    }

    @XmlTransient
    public Collection<Aplicanti> getAplicantiCollection() {
        return aplicantiCollection;
    }

    public void setAplicantiCollection(Collection<Aplicanti> aplicantiCollection) {
        this.aplicantiCollection = aplicantiCollection;
    }
    
}
