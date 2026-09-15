package com.tyss.EMS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(unique = true, nullable = false)
    private String mobile;

    private Double salary;
    @Column(nullable = false)

    private String password;


    // Leave balances
    @Column(nullable = false,columnDefinition = "integer default 10")
    private Integer sickLeaveBal = 10;

    @Column(nullable = false,columnDefinition = "integer default 10")
    private Integer casualLeaveBal = 10;

    @Column(nullable = false,columnDefinition = "integer default 6")
    private Integer unpaidLeaveBal=6 ;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Address address;

    // Embedded Address
//    @Embedded
//    private Address address;

    @ManyToOne
    private Department department;

    @OneToMany(mappedBy = "employee")
    private List<Leave> leaves;


    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<EmployeeFile> files = new ArrayList<>();
}
