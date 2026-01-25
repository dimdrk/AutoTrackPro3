package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectAlreadyExists;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectInvalidArgumentException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotAuthorizedException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotFoundException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.Paginated;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.VehicleFilters;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications.VehicleSpecification;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper.VehicleMapper;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.RoleType;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.UserRepository;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {
    
    private final VehicleMapper vehicleMapper;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    
    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO saveVehicle(VehicleInsertDTO vehicleInsertDTO) throws  AppObjectAlreadyExists {
        
        if (vehicleRepository.findByVin(vehicleInsertDTO.getVin()).isPresent()) {
            throw new AppObjectAlreadyExists("Vehicle", "Vehicle with VIN: " + vehicleInsertDTO.getVin() + " already exist.");
        }

        if (vehicleRepository.findByLicencePlate(vehicleInsertDTO.getLicencePlate()).isPresent()) {
            throw new AppObjectAlreadyExists("Vehicle", "Vehicle with licence plate: " + vehicleInsertDTO.getLicencePlate() + " already exist.");
        }
        
        Vehicle vehicle = vehicleMapper.mapToVehicleEntity(vehicleInsertDTO);
        
        // Get the authenticated user and add as the first owner
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Initialize owners list if not already initialized by mapper
        if (vehicle.getOwners() == null) {
            vehicle.setOwners(new ArrayList<>());
        }
        
        // Add authenticated user as first owner
        vehicle.getOwners().add(0, authenticatedUser);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleReadOnlyDTO(savedVehicle);
    }

    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO updateVehicle(Long id, VehicleInsertDTO vehicleUpdateDTO) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + id + " not found."));

        // Authorization check: only owners can update the vehicle
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkOwnerAuthorization(existing, authenticatedUser);

        // Update vehicle properties but preserve existing relationships
        existing.setVin(vehicleUpdateDTO.getVin());
        existing.setLicencePlate(vehicleUpdateDTO.getLicencePlate());
        existing.setMake(vehicleUpdateDTO.getMake());
        existing.setModel(vehicleUpdateDTO.getModel());
        existing.setType(vehicleUpdateDTO.getType());
        existing.setColor(vehicleUpdateDTO.getColor());
        existing.setProductionDate(vehicleUpdateDTO.getProductionDate());
        existing.setFuel(vehicleUpdateDTO.getFuel());
        existing.setGearbox(vehicleUpdateDTO.getGearbox());
        existing.setOdometer(vehicleUpdateDTO.getOdometer());
        
        // Note: Owners and drivers are not updated here to preserve relationships
        // Use separate endpoints to manage vehicle owners and drivers
        
        Vehicle updatedVehicle = vehicleRepository.save(existing);
        return vehicleMapper.mapToVehicleReadOnlyDTO(updatedVehicle);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteVehicle(Long id) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + id + " not found."));

        // Authorization check: only owners can delete the vehicle
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkOwnerAuthorization(vehicle, authenticatedUser);

        vehicleRepository.deleteById(id);
    }
    
    // Check if usefull
    public VehicleReadOnlyDTO getVehicleById(Long id) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + id + " not found."));
        
        // Authorization check: user must be owner, driver, or SUPER_ADMIN
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkUserAccessToVehicle(vehicle, authenticatedUser);
        
        return vehicleMapper.mapToVehicleReadOnlyDTO(vehicle);
    }

    public Page<VehicleReadOnlyDTO> getPaginatedVehicles(int page, int size) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        // SUPER_ADMIN sees all vehicles, others see only their vehicles
        if (authenticatedUser.getRoleType() == RoleType.SUPER_ADMIN) {
            return vehicleRepository.findAll(pageable).map(vehicleMapper::mapToVehicleReadOnlyDTO);
        } else {
            return vehicleRepository.findByOwnersContainingOrDriversContaining(authenticatedUser, authenticatedUser, pageable)
                    .map(vehicleMapper::mapToVehicleReadOnlyDTO);
        }
    }

    public Page<VehicleReadOnlyDTO> getPaginatedSortedVehicles(int page, int size, String sortBy, String sortDirection) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // SUPER_ADMIN sees all vehicles, others see only their vehicles
        if (authenticatedUser.getRoleType() == RoleType.SUPER_ADMIN) {
            return vehicleRepository.findAll(pageable).map(vehicleMapper::mapToVehicleReadOnlyDTO);
        } else {
            return vehicleRepository.findByOwnersContainingOrDriversContaining(authenticatedUser, authenticatedUser, pageable)
                    .map(vehicleMapper::mapToVehicleReadOnlyDTO);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    public Paginated<VehicleReadOnlyDTO> getVehiclesFilteredPaginated(VehicleFilters filters) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Specification<Vehicle> specs = getSpecsFromFilters(filters);
        
        // SUPER_ADMIN sees all vehicles, others see only their vehicles
        if (authenticatedUser.getRoleType() != RoleType.SUPER_ADMIN) {
            specs = specs.and(VehicleSpecification.vehicleBelongsToUser(authenticatedUser));
        }
        
        var filtered = vehicleRepository.findAll(specs, filters.getPageable());
        return new Paginated<>(filtered.map(vehicleMapper::mapToVehicleReadOnlyDTO));
    }

    @org.springframework.transaction.annotation.Transactional
    public List<VehicleReadOnlyDTO> getVehiclesFiltered(VehicleFilters filters) {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Specification<Vehicle> specs = getSpecsFromFilters(filters);
        
        // SUPER_ADMIN sees all vehicles, others see only their vehicles
        if (authenticatedUser.getRoleType() != RoleType.SUPER_ADMIN) {
            specs = specs.and(VehicleSpecification.vehicleBelongsToUser(authenticatedUser));
        }
        
        return vehicleRepository.findAll(specs)
                .stream().map(vehicleMapper::mapToVehicleReadOnlyDTO).toList();
    }
    
    public List<VehicleReadOnlyDTO> getAllVehicles() {
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // SUPER_ADMIN sees all vehicles, others see only their vehicles
        if (authenticatedUser.getRoleType() == RoleType.SUPER_ADMIN) {
            return vehicleRepository.findAll()
                    .stream().map(vehicleMapper::mapToVehicleReadOnlyDTO).toList();
        } else {
            return vehicleRepository.findByOwnersContainingOrDriversContaining(authenticatedUser, authenticatedUser)
                    .stream().map(vehicleMapper::mapToVehicleReadOnlyDTO).toList();
        }
    }

    private Specification<Vehicle> getSpecsFromFilters(VehicleFilters filters) {
        return Specification
                .where(VehicleSpecification.vehicleStringFieldLike("id", filters.getId()))
                .and(VehicleSpecification.vehicleStringFieldLike("licencePlate", filters.getLicencePlate()))
                .and(VehicleSpecification.vehicleStringFieldLike("vin", filters.getVin()));
                // .and(VehicleSpecification.vehicleOwnerIs(filters.getUsername()));
    }

    // Owner management methods
    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO addOwnerToVehicle(Long vehicleId, Long userId) throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppObjectNotAuthorizedException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + vehicleId + " not found."));
        
        // Authorization check: only owners can add other owners
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkOwnerAuthorization(vehicle, authenticatedUser);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with id: " + userId + " not found."));
        
        vehicle.addOwner(user);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleReadOnlyDTO(savedVehicle);
    }

    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO removeOwnerFromVehicle(Long vehicleId, Long userId) throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppObjectNotAuthorizedException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + vehicleId + " not found."));
        
        // Authorization check: only owners can remove other owners
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkOwnerAuthorization(vehicle, authenticatedUser);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with id: " + userId + " not found."));
        
        vehicle.removeOwner(user);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleReadOnlyDTO(savedVehicle);
    }

    // Driver management methods
    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO addDriverToVehicle(Long vehicleId, Long userId) throws AppObjectNotFoundException, AppObjectInvalidArgumentException, AppObjectNotAuthorizedException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + vehicleId + " not found."));
        
        // Authorization check: only owners can add drivers
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkOwnerAuthorization(vehicle, authenticatedUser);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with id: " + userId + " not found."));
        
        vehicle.addDriver(user);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleReadOnlyDTO(savedVehicle);
    }

    @Transactional(rollbackOn = Exception.class)
    public VehicleReadOnlyDTO removeDriverFromVehicle(Long vehicleId, Long userId) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new AppObjectNotFoundException("Vehicle", "Vehicle with id: " + vehicleId + " not found."));
        
        // Authorization check: only owners can remove drivers
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        checkOwnerAuthorization(vehicle, authenticatedUser);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with id: " + userId + " not found."));
        
        vehicle.removeDriver(user);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return vehicleMapper.mapToVehicleReadOnlyDTO(savedVehicle);
    }

    // Authorization helper methods
    private void checkOwnerAuthorization(Vehicle vehicle, User user) throws AppObjectNotAuthorizedException {
        if (user.getRoleType() != RoleType.SUPER_ADMIN && !vehicle.getOwners().contains(user)) {
            throw new AppObjectNotAuthorizedException("Vehicle", "User is not authorized to perform this action. Only vehicle owners can modify the vehicle.");
        }
    }

    private void checkUserAccessToVehicle(Vehicle vehicle, User user) throws AppObjectNotAuthorizedException {
        if (user.getRoleType() != RoleType.SUPER_ADMIN && 
            !vehicle.getOwners().contains(user) && 
            !vehicle.getDrivers().contains(user)) {
            throw new AppObjectNotAuthorizedException("Vehicle", "User is not authorized to access this vehicle.");
        }
    }
}
