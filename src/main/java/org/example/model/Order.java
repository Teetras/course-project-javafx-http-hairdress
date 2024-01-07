package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "comment")
    private String comment = null;

    @Column(name = "order_date")
    private LocalDate orderDate;


    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_hairdressing", referencedColumnName = "id")
    private HairdressingServices hairdressingService;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    public Order(User user, HairdressingServices hairdressingService, String comment, LocalDate orderDate) {
        this.user = user;
        this.hairdressingService = hairdressingService;
        this.comment = comment;
        this.orderDate = orderDate;
    }
}