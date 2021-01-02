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

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_USER")
    private Integer idUser;
    @Column(name = "ID_POST")
    private Integer idPost;
    @Column(name = "DATA_APLICARII")
    @Temporal(TemporalType.DATE)
    private Date dataAplicarii;

    public Aplicanti() {
    }

    public Aplicanti(Integer id) {
        this.id = id;
    }
    public Aplicanti(int i, int i0, int i1, Date date1) {
        id=i;
        idUser=i0;
        idPost=i1;
        dataAplicarii=date1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
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
