package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper;

import org.springframework.stereotype.Component;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.ServiceRecord;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ServiceRecordMapper {
    
    private final VehicleRepository vehicleRepository;
    
    public ServiceRecordReadOnlyDTO mapServiceRecordReadOnlyDTO(ServiceRecord serviceRecord) {
        var dto = new ServiceRecordReadOnlyDTO();

        dto.setId(serviceRecord.getId());
        dto.setDateOfService(serviceRecord.getDateOfService());
        dto.setServiceType(serviceRecord.getServiceType());
        dto.setDescription(serviceRecord.getDescription());
        dto.setOdometer(serviceRecord.getOdometer());
        dto.setParts(serviceRecord.getParts());
        dto.setCost(serviceRecord.getCost());
        dto.setNextService(serviceRecord.getNextService());
        dto.setRecommentdations(serviceRecord.getRecommentdations());
        dto.setWarranty(serviceRecord.getWarranty());
        
        // Map vehicle information
        if (serviceRecord.getVehicle() != null) {
            dto.setVehicleId(serviceRecord.getVehicle().getId());
            dto.setVehicleVin(serviceRecord.getVehicle().getVin());
            dto.setVehicleLicencePlate(serviceRecord.getVehicle().getLicencePlate());
        }

        return dto;
    }

    public ServiceRecord mapToServiceRecordEntity(ServiceRecordInsertDTO serviceRecordInsertDTO) {
        ServiceRecord serviceRecord = new ServiceRecord();

        serviceRecord.setDateOfService(serviceRecordInsertDTO.getDateOfService());
        serviceRecord.setServiceType(serviceRecordInsertDTO.getServiceType());
        serviceRecord.setDescription(serviceRecordInsertDTO.getDescription());
        serviceRecord.setOdometer(serviceRecordInsertDTO.getOdometer());
        serviceRecord.setParts(serviceRecordInsertDTO.getParts());
        serviceRecord.setCost(serviceRecordInsertDTO.getCost());
        serviceRecord.setNextService(serviceRecordInsertDTO.getNextService());
        serviceRecord.setRecommentdations(serviceRecordInsertDTO.getRecommentdations());
        serviceRecord.setWarranty(serviceRecordInsertDTO.getWarranty());
        
        // Fetch and set the vehicle
        if (serviceRecordInsertDTO.getVehicleId() != null) {
            var vehicle = vehicleRepository.findById(serviceRecordInsertDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + serviceRecordInsertDTO.getVehicleId()));
            serviceRecord.setVehicle(vehicle);
        }

        return serviceRecord;
    }
}
