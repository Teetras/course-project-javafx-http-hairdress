package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")

@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

  /*  @Column(name = "name")
    private String name;
*/@OneToOne
  @JoinColumn(name = "person_id", referencedColumnName = "id")
  private Person person_id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
    @Column(name = "isWorker")
    private String isWorker;
    @Column(name = "isAdmin")
    private String isAdmin;

    public User( String login, String password, Person pers) {
        this.login = login;
        this.password = password;
        this.person_id=pers;
        this.isWorker = "0";
        this.isAdmin = "0";
    }


    public User( String login, String password, String isWorker) {
        this.login = login;
        this.password = password;
        this.isWorker = isWorker;
        this.isAdmin = "0";
    }

    ;

    public User( String login, String password, String isWorker, String isAdmin) {
        this.login = login;
        this.password = password;
        this.isWorker = isWorker;
        this.isAdmin = isAdmin;
    }
    public User(int id, String login, String password, String isWorker, String isAdmin) {
        this.id=id;
        this.login = login;
        this.password = password;
        this.isWorker = isWorker;
        this.isAdmin = isAdmin;
    }
}