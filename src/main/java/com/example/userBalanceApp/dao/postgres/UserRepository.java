package com.example.userBalanceApp.dao.postgres;

import com.example.userBalanceApp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user-with-full-data")
    @Query(value = "Select u from User u left join fetch u.emailData ed, u.phoneData pd " +
            "where ed.email =:email or pd.phone =:phone")
    Optional<User> findUserByAuthenticationData(String email, String phone);

}
