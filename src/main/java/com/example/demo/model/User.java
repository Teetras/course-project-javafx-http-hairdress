package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
    public class User {

        private String id;
        private String login;
        private String password;
        private int isWorker;
        private int isAdmin;
private Person person_id;
        public User() {
        }

        public User(String id, String login, String password, int isWorker, int isAdmin) {
            this.id=id;
            this.login = login;
            this.password = password;
            this.isWorker = isWorker;
            this.isAdmin = isAdmin;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getIsWorker() {
            return isWorker;
        }

        public void setIsWorker(int isWorker) {
            this.isWorker = isWorker;
        }

        public int getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(int isAdmin) {
            this.isAdmin = isAdmin;
        }
    }
