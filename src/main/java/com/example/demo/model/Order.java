package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {

    private int id;
    private String comment = null;
    private HairdressingServiceInfo hairdressingService;
    private User user;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;


    public Order(User user, HairdressingServiceInfo hairdressingService, String comment,LocalDate orderDate) {
        this.user = user;
        this.hairdressingService = hairdressingService;
        this.comment = comment;
        this.orderDate=orderDate;
    }
}