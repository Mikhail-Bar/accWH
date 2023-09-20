package com.accWH.accWH.repository.specifications;

import com.accWH.accWH.model.Certificate;
import com.accWH.accWH.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CertificateSpecification {
    public static Specification<Certificate> dateCertificateBetweenAndUserIn(
            LocalDate startDate, LocalDate endDate, List<User> users) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(root.get("dateCertificate"), startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
            }

            if (!users.isEmpty()) {
                predicates.add(root.get("user").in(users));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
