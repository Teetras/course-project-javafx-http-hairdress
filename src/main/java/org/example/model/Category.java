package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "category")
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_id;

    @Column(name = "category_name")
    private String category_name;

    public Category(String category_name){
        this.category_name=category_name;
    }
    public Category(int id, String category_name){
        this.category_id = id;
        this.category_name = category_name;
    }
    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
    public int getCategory_id() {
       return category_id;
    };

    public void setId(String id) {
        this.category_id= Integer.parseInt(id);
    }

}
