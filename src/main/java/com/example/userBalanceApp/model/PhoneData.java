package com.example.userBalanceApp.model;

import com.example.userBalanceApp.constant.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import static com.example.userBalanceApp.constant.SequenceName.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TableName.PHONE_DATA)
public class PhoneData implements UniqueData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PHONE_DATA_GENERATOR)
    @SequenceGenerator(name = PHONE_DATA_GENERATOR, sequenceName = PHONE_DATA_SEQUENCE, allocationSize = 10)
    private Long id;

    private String phone;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private User user;

    @Override
    public boolean isDataEquals(String qualifier) {
        return phone.equals(qualifier);
    }

    @Override
    public String getQualifier() {
        return phone;
    }

    @Override
    public void dropUser() {
        user.getPhoneData().remove(this);
        this.setUser(null);
    }

}
