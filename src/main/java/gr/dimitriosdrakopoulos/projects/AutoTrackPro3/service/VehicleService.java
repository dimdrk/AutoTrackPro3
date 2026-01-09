package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectAlreadyExists;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotFoundException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.Paginated;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.VehicleFilters;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications.VehicleSpecification;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper.VehicleMapper;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
    
    private final VehicleMapper vehicleMapper;
    private final VehicleRepository vehicleRepository;

    
    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO saveVehicle(VehicleInsertDTO vehicleInsertDTO) throws  AppObjectAlreadyExists {
        
        if (vehicleRepository.findByVin(vehicleInsertDTO.getVin()).isPresent()) {
            throw new AppObjectAlreadyExists("Vehicle", "Vehicle with VIN: " + vehicleInsertDTO.getVin() + " already exist.");
        }

        if (vehicleRepository.findByLicencePlate(vehicleInsertDTO.getLicencePlate()).isPresent()) {
            throw new AppObjectAlreadyExists("Vehicle", "Vehicle with licence plate: " + vehicleInsertDTO.getLicencePlate() + " already exist.");
        }
        Vehicle vehicle = vehicleMapper.mapToVehicleEntity(vehicleInsertDTO);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleReadOnlyDTO(savedVehicle);
    }

    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO updateVehicle(Long id, VehicleInsertDTO vehicleUpdateDTO) throws AppObjectNotFoundException {
        var existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + id + " not found."));

        Vehicle vehicle = vehicleMapper.mapToVehicleEntity(vehicleUpdateDTO);
        vehicle.setId(existing.getId());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleReadOnlyDTO(updatedVehicle);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteVehicle(Long id) throws AppObjectNotFoundException {
        if (vehicleRepository.findById(id).isEmpty()) {
            throw new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + id + " not found.");
        }

        vehicleRepository.deleteById(id);
    }
    
    // Check if usefull
    public VehicleReadOnlyDTO getVehicleById(Long id) throws AppObjectNotFoundException {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + id + " not found."));
        VehicleReadOnlyDTO vehicleToReturn = vehicleMapper.mapToVehicleReadOnlyDTO(vehicle);
        return vehicleToReturn;
    }

    public Page<VehicleReadOnlyDTO> getPaginatedVehicles(int page, int size) {

        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        return vehicleRepository.findAll(pageable).map(vehicleMapper::mapToVehicleReadOnlyDTO);
    }

    public Page<VehicleReadOnlyDTO> getPaginatedSortedVehicles(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return vehicleRepository.findAll(pageable).map(vehicleMapper::mapToVehicleReadOnlyDTO);
    }

    @org.springframework.transaction.annotation.Transactional
    public Paginated<VehicleReadOnlyDTO> getVehiclesFilteredPaginated(VehicleFilters filters) {
        var filtered = vehicleRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(vehicleMapper::mapToVehicleReadOnlyDTO));
    }

    @org.springframework.transaction.annotation.Transactional
    public List<VehicleReadOnlyDTO> getVehiclesFiltered(VehicleFilters filters) {
        return vehicleRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(vehicleMapper::mapToVehicleReadOnlyDTO).toList();
    }
    
    public List<VehicleReadOnlyDTO> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream().map(vehicleMapper::mapToVehicleReadOnlyDTO).toList();
    }

    private Specification<Vehicle> getSpecsFromFilters(VehicleFilters filters) {
        return Specification
                .where(VehicleSpecification.vehicleStringFieldLike("id", filters.getId()))
                .and(VehicleSpecification.vehicleStringFieldLike("licencePlate", filters.getLicencePlate()))
                .and(VehicleSpecification.vehicleStringFieldLike("vin", filters.getVin()));
                // .and(VehicleSpecification.vehicleOwnerIs(filters.getUsername()));
    }
}
