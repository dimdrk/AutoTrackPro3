package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.rest;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectAlreadyExists;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectInvalidArgumentException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotAuthorizedException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotFoundException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppServerException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.Paginated;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.VehicleFilters;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.VehicleReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VehicleRestController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleRestController.class);
    private final VehicleService vehicleService;

    @GetMapping("/vehicles")
    public ResponseEntity<Page<VehicleReadOnlyDTO>> getAllVehicles(        
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<VehicleReadOnlyDTO> vehiclesPage = vehicleService.getPaginatedVehicles(page, size);
        return new ResponseEntity<>(vehiclesPage, HttpStatus.OK);
    }

    @PostMapping("/vehicles/save")
    public ResponseEntity<VehicleReadOnlyDTO> saveVehicle(
            @Valid @RequestBody VehicleInsertDTO vehicleInsertDTO) throws AppObjectInvalidArgumentException, AppObjectAlreadyExists, AppServerException {

        VehicleReadOnlyDTO vehicleReadOnlyDTO = vehicleService.saveVehicle(vehicleInsertDTO);
        return new ResponseEntity<>(vehicleReadOnlyDTO, HttpStatus.OK);
    }

    @PatchMapping("/vehicles/update/{id}")
    public ResponseEntity<VehicleReadOnlyDTO> updateVehicle(
            @RequestParam(name = "id") Long id,
            @Valid @RequestBody VehicleInsertDTO vehicleUpdateDTO) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, AppObjectNotAuthorizedException, AppServerException {

                VehicleReadOnlyDTO vehicleReadOnlyDTO = vehicleService.updateVehicle(id, vehicleUpdateDTO);
        return new ResponseEntity<>(vehicleReadOnlyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/vehicles/delete/")
    public ResponseEntity<VehicleReadOnlyDTO> deleteVehicle(
                @RequestParam(name = "id") Long id) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, AppObjectNotAuthorizedException, AppServerException {
        
                    VehicleReadOnlyDTO vehicleReadOnlyDTO = vehicleService.getVehicleById(id);
                    vehicleService.deleteVehicle(id);

        return new ResponseEntity<>(vehicleReadOnlyDTO, HttpStatus.OK);
    }

    @PostMapping("/vehicles/all")
    public ResponseEntity<List<VehicleReadOnlyDTO>> getVehicles(@RequestBody VehicleFilters filters, Principal principal)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = VehicleFilters.builder().build();
            return ResponseEntity.ok(vehicleService.getVehiclesFiltered(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get vehicles.", e);
            throw e;
        }
    }
    
    @PostMapping("/vehicles/all/paginated")
    public ResponseEntity<Paginated<VehicleReadOnlyDTO>> getVehiclesPaginated(@RequestBody VehicleFilters filters, Principal principal)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = VehicleFilters.builder().build();
            return ResponseEntity.ok(vehicleService.getVehiclesFilteredPaginated(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get vehicles.", e);
            throw e;
        }
    }

    // Owner management endpoints
    @PostMapping("/vehicles/{vehicleId}/owners/{userId}")
    public ResponseEntity<VehicleReadOnlyDTO> addOwner(
            @RequestParam(name = "vehicleId") Long vehicleId,
            @RequestParam(name = "userId") Long userId) throws AppObjectNotFoundException, AppObjectNotAuthorizedException, AppObjectInvalidArgumentException {
        
        VehicleReadOnlyDTO vehicleReadOnlyDTO = vehicleService.addOwnerToVehicle(vehicleId, userId);
        return new ResponseEntity<>(vehicleReadOnlyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/vehicles/{vehicleId}/owners/{userId}")
    public ResponseEntity<VehicleReadOnlyDTO> removeOwner(
            @RequestParam(name = "vehicleId") Long vehicleId,
            @RequestParam(name = "userId") Long userId) throws AppObjectNotFoundException, AppObjectNotAuthorizedException, AppObjectInvalidArgumentException {
        
        VehicleReadOnlyDTO vehicleReadOnlyDTO = vehicleService.removeOwnerFromVehicle(vehicleId, userId);
        return new ResponseEntity<>(vehicleReadOnlyDTO, HttpStatus.OK);
    }

    // Driver management endpoints
    @PostMapping("/vehicles/{vehicleId}/drivers/{userId}")
    public ResponseEntity<VehicleReadOnlyDTO> addDriver(
            @RequestParam(name = "vehicleId") Long vehicleId,
            @RequestParam(name = "userId") Long userId) throws AppObjectNotFoundException, AppObjectNotAuthorizedException, AppObjectInvalidArgumentException {
        
        VehicleReadOnlyDTO vehicleReadOnlyDTO = vehicleService.addDriverToVehicle(vehicleId, userId);
        return new ResponseEntity<>(vehicleReadOnlyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/vehicles/{vehicleId}/drivers/{userId}")
    public ResponseEntity<VehicleReadOnlyDTO> removeDriver(
            @RequestParam(name = "vehicleId") Long vehicleId,
            @RequestParam(name = "userId") Long userId) throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        
        VehicleReadOnlyDTO vehicleReadOnlyDTO = vehicleService.removeDriverFromVehicle(vehicleId, userId);
        return new ResponseEntity<>(vehicleReadOnlyDTO, HttpStatus.OK);
    }
}
