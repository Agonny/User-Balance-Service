package com.example.userBalanceApp.dao.postgres;

import com.example.userBalanceApp.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user-with-full-data")
    @Query(value = "Select u from User u " +
            "join fetch u.emailData ed " +
            "join fetch u.phoneData pd " +
            "where ed.email = :data or pd.phone = :data")
    Optional<User> findUserByAuthenticationData(String data);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "user-with-full-data")
    Optional<User> findById(Long id);

}
