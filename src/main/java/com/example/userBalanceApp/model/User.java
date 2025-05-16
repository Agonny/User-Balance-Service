package com.example.userBalanceApp.model;

import com.example.userBalanceApp.constant.TableName;
import com.example.userBalanceApp.serializer.LocalDateDeserializer;
import com.example.userBalanceApp.serializer.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.example.userBalanceApp.constant.SequenceName.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.USER, schema = "public")
@NamedEntityGraph(name = "user-with-full-data", attributeNodes = {@NamedAttributeNode(value = "emailData"), @NamedAttributeNode(value = "phoneData")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_GENERATOR)
    @SequenceGenerator(name = USER_GENERATOR, sequenceName = USER_SEQUENCE, allocationSize = 10)
    private Long id;

    private String name;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;

    private String password;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<EmailData> emailData = new LinkedHashSet<>();

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<PhoneData> phoneData = new LinkedHashSet<>();

    @PrimaryKeyJoinColumn
    @EqualsAndHashCode.Exclude
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    private Account account;

    public List<String> emailDataList() {
        List<String> data = new ArrayList<>();

        for(EmailData emailData : emailData.stream().toList()) {
            data.add(emailData.getEmail());
        }

        return data;
    }

    public List<String> phoneDataList() {
        List<String> data = new ArrayList<>();

        for(PhoneData phoneData : phoneData.stream().toList()) {
            data.add(phoneData.getPhone());
        }

        return data;
    }
}
