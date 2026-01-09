package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.ServiceRecord;

public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Long>, JpaSpecificationExecutor<ServiceRecord>  {
    
    Optional<ServiceRecord> findByDateOfService(LocalDate dateOfService);
}
