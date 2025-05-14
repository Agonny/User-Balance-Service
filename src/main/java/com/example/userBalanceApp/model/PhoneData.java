package com.example.userBalanceApp.model;

import com.example.userBalanceApp.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

import static com.example.userBalanceApp.constant.SequenceName.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.PHONE_DATA)
public class PhoneData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PHONE_DATA_GENERATOR)
    @SequenceGenerator(name = PHONE_DATA_GENERATOR, sequenceName = PHONE_DATA_SEQUENCE, allocationSize = 10)
    private Long id;

    private String phone;

    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private User user;

}
