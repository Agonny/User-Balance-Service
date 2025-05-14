package com.example.userBalanceApp.model;

import com.example.userBalanceApp.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

import static com.example.userBalanceApp.constant.SequenceName.EMAIL_DATA_GENERATOR;
import static com.example.userBalanceApp.constant.SequenceName.EMAIL_DATA_SEQUENCE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.EMAIL_DATA)
public class EmailData implements UniqueData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = EMAIL_DATA_GENERATOR)
    @SequenceGenerator(name = EMAIL_DATA_GENERATOR, sequenceName = EMAIL_DATA_SEQUENCE, allocationSize = 10)
    private Long id;

    private String email;

    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private User user;

    @Override
    public boolean isDataEquals(String qualifier) {
        return email.equals(qualifier);
    }

    @Override
    public String getQualifier() {
        return email;
    }
}
