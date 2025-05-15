package com.example.userBalanceApp.model;

import com.example.userBalanceApp.constant.TableName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.ACCOUNT)
public class Account {

    @Id
    private Long id;

    private BigDecimal balance;

    private BigDecimal initialBalance;

    private BigDecimal maxIncrementBalance;

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private User user;

}
