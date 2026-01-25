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
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.filters.ServiceRecordFilters;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.specifications.ServiceRecordSpecification;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.ServiceRecordReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper.ServiceRecordMapper;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.ServiceRecord;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.ServiceRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceRecordService {
    
    private final ServiceRecordMapper serviceRecordMapper;
    private final ServiceRecordRepository serviceRecordRepository;

    @Transactional(rollbackOn = Exception.class)
    public ServiceRecordReadOnlyDTO saveServiceRecord(ServiceRecordInsertDTO serviceRecordInsertDTO) throws AppObjectAlreadyExists {
        // Removed duplicate date check since multiple records can have the same date for different vehicles
        ServiceRecord serviceRecord = serviceRecordMapper.mapToServiceRecordEntity(serviceRecordInsertDTO);
        ServiceRecord savedServiceRecord = serviceRecordRepository.save(serviceRecord);
        return serviceRecordMapper.mapServiceRecordReadOnlyDTO(savedServiceRecord);
    }

    @Transactional(rollbackOn = Exception.class)
    public ServiceRecordReadOnlyDTO updateServiceRecord(Long id, ServiceRecordInsertDTO serviceRecordUpdateDTO) throws AppObjectNotFoundException {
        var existing = serviceRecordRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("ServiceRecord", "Service record with id: " + id + " not found."));

        ServiceRecord serviceRecord = serviceRecordMapper.mapToServiceRecordEntity(serviceRecordUpdateDTO);
        serviceRecord.setId(existing.getId());
        ServiceRecord updatedServiceRecord = serviceRecordRepository.save(serviceRecord);
        return serviceRecordMapper.mapServiceRecordReadOnlyDTO(updatedServiceRecord);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteServiceRecord(Long id) throws AppObjectNotFoundException {
        if (serviceRecordRepository.findById(id).isEmpty()) {
            throw new AppObjectNotFoundException("ServiceRecord", "Service record with id: " + id + " not found.");
        }

        serviceRecordRepository.deleteById(id);
    }
    
    // Check if usefull
    public ServiceRecordReadOnlyDTO getServiceRecordById(Long id) throws AppObjectNotFoundException {

        ServiceRecord serviceRecord = serviceRecordRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("ServiceRecord", "Service record with id: " + id + " not found."));
        ServiceRecordReadOnlyDTO ServiceRecordToReturn = serviceRecordMapper.mapServiceRecordReadOnlyDTO(serviceRecord);
        return ServiceRecordToReturn;
    }

    public Page<ServiceRecordReadOnlyDTO> getPaginatedServiceRecord(int page, int size) {

        String defaultSort = "id";
        Pageable pageable = PageRequest.of(page, size, Sort.by(defaultSort).ascending());

        return serviceRecordRepository.findAll(pageable).map(serviceRecordMapper::mapServiceRecordReadOnlyDTO);
    }

    public Page<ServiceRecordReadOnlyDTO> getPaginatedSortedServiceRecord(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return serviceRecordRepository.findAll(pageable).map(serviceRecordMapper::mapServiceRecordReadOnlyDTO);
    }

    @org.springframework.transaction.annotation.Transactional
    public Paginated<ServiceRecordReadOnlyDTO> getServiceRecordsFilteredPaginated(ServiceRecordFilters filters) {
        var filtered = serviceRecordRepository.findAll(getSpecsFromFilters(filters), filters.getPageable());
        return new Paginated<>(filtered.map(serviceRecordMapper::mapServiceRecordReadOnlyDTO));
    }

    @org.springframework.transaction.annotation.Transactional
    public List<ServiceRecordReadOnlyDTO> getServiceRecordsFiltered(ServiceRecordFilters filters) {
        return serviceRecordRepository.findAll(getSpecsFromFilters(filters))
                .stream().map(serviceRecordMapper::mapServiceRecordReadOnlyDTO).toList();
    }

    private Specification<ServiceRecord> getSpecsFromFilters(ServiceRecordFilters filters) {
        return Specification
                .where(ServiceRecordSpecification.serviceRecordVehicleIs(filters.getLicencePlate()))
                .and(ServiceRecordSpecification.serviceRecordStringFieldLike("id", filters.getId()))
                .and(ServiceRecordSpecification.serviceRecordStringFieldLike("dateOfService", filters.getDateOfService().toString()));
    }
}
