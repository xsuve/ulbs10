/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Razvan
 */
@Entity
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id")
    , @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")
    , @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password")
    , @NamedQuery(name = "Users.findByFirstname", query = "SELECT u FROM Users u WHERE u.firstname = :firstname")
    , @NamedQuery(name = "Users.findByLastname", query = "SELECT u FROM Users u WHERE u.lastname = :lastname")
    , @NamedQuery(name = "Users.findByStatut", query = "SELECT u FROM Users u WHERE u.statut = :statut")})
public class Users implements Serializable {

    @OneToMany(mappedBy = "idUser")
    private Collection<Aplicanti> aplicantiCollection;
    @OneToMany(mappedBy = "deschisDe")
    private Collection<Posturi> posturiCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 255)
    @Column(name = "PASSWORD")
    private String password;
    @Size(max = 50)
    @Column(name = "FIRSTNAME")
    private String firstname;
    @Size(max = 50)
    @Column(name = "LASTNAME")
    private String lastname;
    @Size(max = 30)
    @Column(name = "STATUT")
    private String statut;

    public Users() {
    }

    public Users(String stEmail, String stPassword, String stFirstName, String stLastName, String stStatut) {
        email = stEmail;
        password = stPassword;
        firstname = stFirstName;
        lastname = stLastName;
        statut = stStatut;
    }

    public Users(int inID, String stEmail, String stPassword, String stFirstName, String stLastName, String stStatut) {
        id = inID;
        email = stEmail;
        password = stPassword;
        firstname = stFirstName;
        lastname = stLastName;
        statut = stStatut;
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Users[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Aplicanti> getAplicantiCollection() {
        return aplicantiCollection;
    }

    public void setAplicantiCollection(Collection<Aplicanti> aplicantiCollection) {
        this.aplicantiCollection = aplicantiCollection;
    }

    @XmlTransient
    public Collection<Posturi> getPosturiCollection() {
        return posturiCollection;
    }

    public void setPosturiCollection(Collection<Posturi> posturiCollection) {
        this.posturiCollection = posturiCollection;
    }

    /**
     *  Returneaza un URL cu CV-ul unui utilizator, daca acesta exista sau # daca nu
     *
     * @return
     */
    public String getCV() {
        URL s = this.getClass().getProtectionDomain().getCodeSource().getLocation();
        String p = s.getPath();
        String returnare = "";
        if (p.contains("/")) {
            p = p.replace("WEB-INF/classes/entity/Users.class", "cv/");
            p = p.replace("WEB-INF/classes/entity/Users.class", "cv/");
            returnare+="cv/";
        }
        if (p.contains("\\")) {
            p = p.replace("WEB-INF\\classes\\entity\\Users.class", "cv\\");
            returnare+="cv\\";
        }
        p += id.toString() + ".pdf";
        returnare+=id.toString()+".pdf";
            
        try {
            p = java.net.URLDecoder.decode(p, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
        }
            
        File file = new File(p);
        if (file.exists()) {
            return returnare;
        } else {
            return "#";
        }
    }
}
