package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications;

import org.springframework.data.jpa.domain.Specification;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.ServiceRecord;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import jakarta.persistence.criteria.Join;

public class ServiceRecordSpecification {

    private ServiceRecordSpecification() {

    }
    
    public static Specification<ServiceRecord> serviceRecordVehicleIs(String licencePlate) {
        return ((root, query, criteriaBuilder) -> {
            if (licencePlate ==null || licencePlate.isBlank()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            Join<ServiceRecord, Vehicle> serviceRecordsVehicle = root.join("vehicle");
            return criteriaBuilder.equal(serviceRecordsVehicle.get("licencePlate"), licencePlate);
        });
    }

    public static Specification<ServiceRecord> serviceRecordStringFieldLike(String field, String value) {
        return ((root, query, criteriaBuilder) -> {
            if (value == null || value.trim().isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.upper(root.get(field)), "%" + value.toUpperCase() + "%");
        });
    }
    
}
