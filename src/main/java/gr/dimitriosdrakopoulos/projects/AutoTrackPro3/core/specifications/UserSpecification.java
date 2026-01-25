package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications;

import org.springframework.data.jpa.domain.Specification;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import jakarta.persistence.criteria.Join;

public class UserSpecification {

    private UserSpecification() {

    }

    public static Specification<User> userUsernameIs(String username) {
        return ((root, query, criteriaBuilder) -> {
            if (username == null || username.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            Join<User, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("username"), username);
        });
    }

    public static Specification<User> userEmailIs(String email) {
        return ((root, query, criteriaBuilder) -> {
            if (email ==null || email.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            Join<User, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("email"), email);
        });
    }

    public static Specification<User> userIsActive(Boolean isActive) {
        return ((root, query, criteriaBuilder) -> {
            if (isActive ==null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            Join<User, User> user = root.join("user");
            return criteriaBuilder.equal(user.get("isActive"), isActive);
        });
    }

    public static Specification<User> userStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");
        });
    }
    
}
