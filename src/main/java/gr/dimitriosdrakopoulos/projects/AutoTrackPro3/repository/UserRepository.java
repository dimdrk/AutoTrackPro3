package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhonenumber(String phonenumber);
    Optional<User> findByDriverLicence(String driverLicence);
    Optional<User> findByIdentityNumber(String identityNumber);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhonenumber(String phonenumber);
    boolean existsByDriverLicence(String driverLicence);
    boolean existsByIdentityNumber(String identityNumber);

}
