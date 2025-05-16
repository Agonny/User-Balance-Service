package com.example.userBalanceApp.dao.selector;

import com.example.userBalanceApp.filter.UserFilter;
import com.example.userBalanceApp.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.userBalanceApp.constant.FieldName.*;

@Component
public class UserDynamicQuerySelector {

    private final EntityManager entityManager;

    private final CriteriaBuilder criteriaBuilder;

    public UserDynamicQuerySelector(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    @Transactional
    public Page<User> getUsersByFilter(UserFilter userFilter, Pageable pageable) {
        CriteriaQuery<User> cq = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = cq.from(User.class);

        List<Predicate> predicates = createPredicates(userRoot, userFilter);

        cq.select(userRoot);
        cq.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(criteriaBuilder.asc(userRoot.get(ID.getValue())));

        List<User> results = entityManager.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setHint("jakarta.persistence.fetchgraph", entityManager.getEntityGraph("user-with-full-data"))
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(results, pageable, getCountOfElements(userFilter));
    }

    private long getCountOfElements(UserFilter userFilter) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        List<Predicate> predicates = createPredicates(countRoot, userFilter);

        countQuery.select(criteriaBuilder.count(countRoot)).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private <T> List<Predicate> createPredicates(Root<T> root, UserFilter userFilter) {
        List<Predicate> predicates = new ArrayList<>();

        if(Objects.nonNull(userFilter.getDateOfBirth())) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get(DATE_OF_BIRTH.getValue()), userFilter.getDateOfBirth())
            );
        }

        if(Objects.nonNull(userFilter.getName())) {
            predicates.add(criteriaBuilder.like(
                    root.get(NAME.getValue()), "%" + userFilter.getName() + "%")
            );
        }

        if(Objects.nonNull(userFilter.getEmail())) {
            root.join(EMAIL_DATA.getValue(), JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(
                    root.get(EMAIL_DATA.getValue()).get(EMAIL.getValue()), userFilter.getEmail())
            );
        }

        if(Objects.nonNull(userFilter.getPhone())) {
            root.join(PHONE_DATA.getValue(), JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(
                    root.get(PHONE_DATA.getValue()).get(PHONE.getValue()), userFilter.getPhone())
            );
        }

        return predicates;
    }

}
