package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Review {
    private int reviewId;
    private Order order;
    private User customer;
    private int rating;
    private String comment;
    private Date reviewDate;
}
