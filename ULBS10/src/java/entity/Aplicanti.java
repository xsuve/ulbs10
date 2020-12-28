/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elena
 */
@Entity
@Table(name = "APLICANTI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aplicanti.findAll", query = "SELECT a FROM Aplicanti a")
    , @NamedQuery(name = "Aplicanti.findByIdUser", query = "SELECT a FROM Aplicanti a WHERE a.idUser = :idUser")
    , @NamedQuery(name = "Aplicanti.findByDataAplicarii", query = "SELECT a FROM Aplicanti a WHERE a.dataAplicarii = :dataAplicarii")
    , @NamedQuery(name = "Aplicanti.findByObservatii", query = "SELECT a FROM Aplicanti a WHERE a.observatii = :observatii")
    , @NamedQuery(name = "Aplicanti.findByIdObservator", query = "SELECT a FROM Aplicanti a WHERE a.idObservator = :idObservator")})
public class Aplicanti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_USER")
    private Integer idUser;
    @Lob
    @Column(name = "CV")
    private Serializable cv;
    @Column(name = "DATA_APLICARII")
    @Temporal(TemporalType.DATE)
    private Date dataAplicarii;
    @Size(max = 200)
    @Column(name = "OBSERVATII")
    private String observatii;
    @Column(name = "ID_OBSERVATOR")
    private Integer idObservator;
    @JoinColumn(name = "ID_POST", referencedColumnName = "ID")
    @ManyToOne
    private Posturi idPost;
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Users users;

    public Aplicanti() {
    }

    public Aplicanti(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Serializable getCv() {
        return cv;
    }

    public void setCv(Serializable cv) {
        this.cv = cv;
    }

    public Date getDataAplicarii() {
        return dataAplicarii;
    }

    public void setDataAplicarii(Date dataAplicarii) {
        this.dataAplicarii = dataAplicarii;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public Integer getIdObservator() {
        return idObservator;
    }

    public void setIdObservator(Integer idObservator) {
        this.idObservator = idObservator;
    }

    public Posturi getIdPost() {
        return idPost;
    }

    public void setIdPost(Posturi idPost) {
        this.idPost = idPost;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aplicanti)) {
            return false;
        }
        Aplicanti other = (Aplicanti) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Aplicanti[ idUser=" + idUser + " ]";
    }
    
}
