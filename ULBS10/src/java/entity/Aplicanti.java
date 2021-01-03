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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Razvan
 */
@Entity
@Table(name = "APLICANTI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aplicanti.findAll", query = "SELECT a FROM Aplicanti a")
    , @NamedQuery(name = "Aplicanti.findById", query = "SELECT a FROM Aplicanti a WHERE a.id = :id")
    , @NamedQuery(name = "Aplicanti.findByIdUser", query = "SELECT a FROM Aplicanti a WHERE a.idUser = :idUser")
    , @NamedQuery(name = "Aplicanti.findByIdPost", query = "SELECT a FROM Aplicanti a WHERE a.idPost = :idPost")
    , @NamedQuery(name = "Aplicanti.findByDataAplicarii", query = "SELECT a FROM Aplicanti a WHERE a.dataAplicarii = :dataAplicarii")})
public class Aplicanti implements Serializable {

    @JoinColumn(name = "ID_POST", referencedColumnName = "ID")
    @ManyToOne
    private Posturi idPost;
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID")
    @ManyToOne
    private Users idUser;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Column(name = "DATA_APLICARII")
    @Temporal(TemporalType.DATE)
    private Date dataAplicarii;

    public Aplicanti() {
    }

    public Aplicanti(Integer id) {
        this.id = id;
    }
    public Aplicanti(int i, Users u, Posturi p, Date date1) {
        id=i;
        idUser=u;
        idPost=p;
        dataAplicarii=date1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    public Posturi getIdPost() {
        return idPost;
    }

    public void setIdPost(Posturi idPost) {
        this.idPost = idPost;
    }

    public Date getDataAplicarii() {
        return dataAplicarii;
    }

    public void setDataAplicarii(Date dataAplicarii) {
        this.dataAplicarii = dataAplicarii;
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
        if (!(object instanceof Aplicanti)) {
            return false;
        }
        Aplicanti other = (Aplicanti) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "controller.Aplicanti[ id=" + id + " ]";
    }    
}
