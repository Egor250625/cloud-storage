package by.egorivanov.cloudstorage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Entity
    @Table(name = "Users",schema = "cloud_file_storage")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id",nullable = false)
        private Integer id;


        @Column(name = "username",nullable = false)
        @Size(min = 5,max = 20,message ="Username must be between 5 and 15 characters long." )
        private String username;


        @Column(name = "password",nullable = false)
        @Size(min = 5,message = "Password must be no less than 5 characters.")
        private String password;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

