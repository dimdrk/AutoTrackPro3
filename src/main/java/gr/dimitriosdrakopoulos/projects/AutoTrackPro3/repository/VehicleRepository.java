package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle>  {
    
    Optional<Vehicle> findByVin(String vin);
    Optional<Vehicle> findByLicencePlate(String licencePlate);
    boolean existsByVin(String vin);
    boolean existsByLicencePlate(String licencePlate);
    
    // Find vehicles where user is either owner or driver
    List<Vehicle> findByOwnersContainingOrDriversContaining(User owner, User driver);
    Page<Vehicle> findByOwnersContainingOrDriversContaining(User owner, User driver, Pageable pageable);
}
