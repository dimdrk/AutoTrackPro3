package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper;

import org.springframework.stereotype.Component;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VehicleMapper {
    
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
        
        return vehicle;        
    }
}
