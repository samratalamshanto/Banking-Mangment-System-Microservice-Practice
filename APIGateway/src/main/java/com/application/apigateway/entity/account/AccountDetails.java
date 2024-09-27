package com.application.apigateway.entity.account;


import com.application.apigateway.entity.CommonAttributes;
import com.application.apigateway.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountDetails extends CommonAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    @OneToOne(cascade = CascadeType.ALL)
    private User accountHolder;
    private double balance;
}
