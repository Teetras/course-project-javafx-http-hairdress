package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hairdressing_services")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HairdressingServices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private String duration;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;


    public HairdressingServices(String name, String description, double price, String duration, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.category = category;
    }
    public HairdressingServices(int id,String name, String description, double price, String duration, Category category) {
       this.id=id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.category = category;
    }
}