package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleMapper {
    
    private final UserRepository userRepository;
    
    public VehicleReadOnlyDTO mapToVehicleReadOnlyDTO(Vehicle vehicle) {
        var dto = new VehicleReadOnlyDTO();

        dto.setId(vehicle.getId());
        dto.setVin(vehicle.getVin());
        dto.setLicencePlate(vehicle.getLicencePlate());
        dto.setMake(vehicle.getMake());
        dto.setModel(vehicle.getModel());
        dto.setType(vehicle.getType());
        dto.setColor(vehicle.getColor());
        dto.setProductionDate(vehicle.getProductionDate());
        dto.setFuel(vehicle.getFuel());
        dto.setGearbox(vehicle.getGearbox());
        dto.setOdometer(vehicle.getOdometer());
        dto.setOwnerUsernames(vehicle.getOwners().stream()
                .map(User::getUsername)
                .collect(Collectors.toList()));
        dto.setDriverUsernames(vehicle.getDrivers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList()));

        return dto;
    }

    public Vehicle mapToVehicleEntity(VehicleInsertDTO vehicleInsertDTO) {
        Vehicle vehicle = new Vehicle();

        vehicle.setVin(vehicleInsertDTO.getVin());
        vehicle.setLicencePlate(vehicleInsertDTO.getLicencePlate());
        vehicle.setMake(vehicleInsertDTO.getMake());
        vehicle.setModel(vehicleInsertDTO.getModel());
        vehicle.setType(vehicleInsertDTO.getType());
        vehicle.setColor(vehicleInsertDTO.getColor());
        vehicle.setProductionDate(vehicleInsertDTO.getProductionDate());
        vehicle.setFuel(vehicleInsertDTO.getFuel());
        vehicle.setGearbox(vehicleInsertDTO.getGearbox());
        vehicle.setOdometer(vehicleInsertDTO.getOdometer());
        
        // Note: The authenticated user should be added as the first owner in the service layer
        // Set additional owners if provided
        if (vehicleInsertDTO.getAdditionalOwnerIds() != null && !vehicleInsertDTO.getAdditionalOwnerIds().isEmpty()) {
            List<User> additionalOwners = userRepository.findAllById(vehicleInsertDTO.getAdditionalOwnerIds());
            vehicle.setOwners(additionalOwners);
        }
        
        // Set drivers
        if (vehicleInsertDTO.getDriverIds() != null && !vehicleInsertDTO.getDriverIds().isEmpty()) {
            List<User> drivers = userRepository.findAllById(vehicleInsertDTO.getDriverIds());
            vehicle.setDrivers(drivers);
        }
        
        return vehicle;        
    }
}
