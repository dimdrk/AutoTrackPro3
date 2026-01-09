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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectAlreadyExists;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectInvalidArgumentException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotAuthorizedException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotFoundException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppServerException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.Paginated;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.ServiceRecordFilters;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.service.ServiceRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ServiceRecordRestController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRecordRestController.class);
    private final ServiceRecordService serviceRecordService;

    @GetMapping("/seviceRecords")
    public ResponseEntity<Page<ServiceRecordReadOnlyDTO>> getAllServiceRecords(        
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ServiceRecordReadOnlyDTO> vehiclesPage = serviceRecordService.getPaginatedServiceRecord(page, size);
        return new ResponseEntity<>(vehiclesPage, HttpStatus.OK);
    }

    @PostMapping("/seviceRecords/save")
    public ResponseEntity<ServiceRecordReadOnlyDTO> saveServiceRecord(
            @Valid @RequestPart(name = "vehicle") ServiceRecordInsertDTO serviceRecordInsertDTO) throws AppObjectInvalidArgumentException, AppObjectAlreadyExists, AppServerException {

                ServiceRecordReadOnlyDTO serviceRecordReadOnlyDTO = serviceRecordService.saveServiceRecord(serviceRecordInsertDTO);
        return new ResponseEntity<>(serviceRecordReadOnlyDTO, HttpStatus.OK);
    }

    @PatchMapping("/serviceRecords/update/{id}")
    public ResponseEntity<ServiceRecordReadOnlyDTO> updateServiceRecord(
            @RequestParam(name = "id") Long id,
            @Valid @RequestBody ServiceRecordInsertDTO serviceRecordUpdateDTO) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, AppServerException {

                ServiceRecordReadOnlyDTO serviceRecordReadOnlyDTO = serviceRecordService.updateServiceRecord(id, serviceRecordUpdateDTO);
        return new ResponseEntity<>(serviceRecordReadOnlyDTO, HttpStatus.OK);
    }

    @DeleteMapping("/serviceRecords/delete/")
    public ResponseEntity<ServiceRecordReadOnlyDTO> deleteServiceRecord(
                @RequestParam(name = "id") Long id) throws AppObjectInvalidArgumentException, AppObjectNotFoundException, AppServerException {
        
                    ServiceRecordReadOnlyDTO serviceRecordReadOnlyDTO = serviceRecordService.getServiceRecordById(id);
                    serviceRecordService.deleteServiceRecord(id);

        return new ResponseEntity<>(serviceRecordReadOnlyDTO, HttpStatus.OK);
    }
    
    @PostMapping("/seviceRecords/all")
    public ResponseEntity<List<ServiceRecordReadOnlyDTO>> getServiceRecords(@RequestBody ServiceRecordFilters filters, Principal principal)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = ServiceRecordFilters.builder().build();
            return ResponseEntity.ok(serviceRecordService.getServiceRecordsFiltered(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get service records.", e);
            throw e;
        }
    }
    
    @PostMapping("/seviceRecords/all/paginated")
    public ResponseEntity<Paginated<ServiceRecordReadOnlyDTO>> getServiceRecordsPaginated(@RequestBody ServiceRecordFilters filters, Principal principal)
            throws AppObjectNotFoundException, AppObjectNotAuthorizedException {
        try {
            if (filters == null) filters = ServiceRecordFilters.builder().build();
            return ResponseEntity.ok(serviceRecordService.getServiceRecordsFilteredPaginated(filters));
        } catch (Exception e) {
            LOGGER.error("ERROR: Could not get service records.", e);
            throw e;
        }
    }
    
}
