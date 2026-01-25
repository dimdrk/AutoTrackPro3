package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications;

import org.springframework.data.jpa.domain.Specification;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import jakarta.persistence.criteria.Join;

public class VehicleSpecification {
    
    private VehicleSpecification() {

    }
    
    public static Specification<Vehicle> vehicleBelongsToUser(User user) {
        return ((root, query, criteriaBuilder) -> {
            if (user == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            // Vehicle belongs to user if they are in owners or drivers
            Join<Vehicle, User> owners = root.join("owners");
            Join<Vehicle, User> drivers = root.join("drivers");
            
            return criteriaBuilder.or(
                criteriaBuilder.equal(owners, user),
                criteriaBuilder.equal(drivers, user)
            );
        });
    }

    public static Specification<Vehicle> vehicleStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");
        });
    }
}
