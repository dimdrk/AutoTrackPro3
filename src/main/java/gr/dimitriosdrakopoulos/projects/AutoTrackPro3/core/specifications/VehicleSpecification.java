package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications;

import org.springframework.data.jpa.domain.Specification;

// import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Owner;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
// import jakarta.persistence.criteria.Join;

public class VehicleSpecification {
    
    private VehicleSpecification() {

    }
    
    // public static Specification<Vehicle> vehicleOwnerIs(String username) {
    //     return ((root, query, criteriaBuilder) -> {
    //         if (username ==null || username.isBlank()) {
    //             return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    //         }

    //         Join<Vehicle, Owner> ownerVehicles = root.join("user");
    //         return criteriaBuilder.equal(ownerVehicles.get("username"), username);
    //     });
    // }

    public static Specification<Vehicle> vehicleStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");
        });
    }
}
