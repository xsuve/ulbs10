/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Razvan
 */
public class User {
    private final int id;
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String statut;
    
    public User(){
       id = -1;
       email="";
       password="";
       firstName="";
       lastName="";
       statut="";
    }
    public User(int i_id,String s_email, String s_pass, String s_firstName, String s_lastName, String s_statut) {
        id=i_id;
        email = s_email;
        password = s_pass;
        firstName = s_firstName;
        lastName = s_lastName;
        statut = s_statut;     
    }
    public int getId(){
        return id;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public String getStatut(){
        return statut;
    }
    
}
