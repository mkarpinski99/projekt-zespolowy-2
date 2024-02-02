package com.blynder.blynder.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class TimeoutInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private User timeoutedUser;
    @ManyToOne
    private Stream stream;

    private Date timeoutedUntil;

}
