package com.example.userBalanceApp.model;

import com.example.userBalanceApp.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.example.userBalanceApp.constant.SequenceName.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.USER)
@NamedEntityGraph(name = "user-with-full-data", attributeNodes = {@NamedAttributeNode(value = "emailData"), @NamedAttributeNode(value = "phoneData")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_GENERATOR)
    @SequenceGenerator(name = USER_GENERATOR, sequenceName = USER_SEQUENCE, allocationSize = 10)
    private Long id;

    private String name;

    private LocalDate dateOfBirth;

    private String password;

    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<EmailData> emailData = new LinkedHashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<PhoneData> phoneData = new LinkedHashSet<>();

    @PrimaryKeyJoinColumn
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private Account account;

}
