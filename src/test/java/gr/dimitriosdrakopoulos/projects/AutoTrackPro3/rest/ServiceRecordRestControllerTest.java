package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Color;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Fuel;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gender;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gearbox;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.RoleType;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.VehicleType;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.ServiceRecord;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.ServiceRecordRepository;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.UserRepository;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.VehicleRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ServiceRecordRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceRecordRepository serviceRecordRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    private User testOwner;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        serviceRecordRepository.deleteAll();
        vehicleRepository.deleteAll();
        userRepository.deleteAll();
        
        // Create a test owner user
        testOwner = new User();
        testOwner.setIsActive(true);
        testOwner.setUsername("TestServiceOwner");
        testOwner.setPassword("Password123@");
        testOwner.setFirstname("Test");
        testOwner.setLastname("ServiceOwner");
        testOwner.setEmail("testserviceowner@example.com");
        testOwner.setPhonenumber("6666666666");
        testOwner.setGender(Gender.MALE);
        testOwner.setRoleType(RoleType.OWNER);
        testOwner.setDriverLicence("DL-TEST-SERVICE");
        testOwner.setLicenceExpiration(LocalDate.of(2030, 12, 31));
        testOwner.setLicenceCategory("B");
        testOwner.setIdentityNumber("ID-TEST-SERVICE");
        testOwner.setCity("Test City");
        testOwner = userRepository.save(testOwner);

        // Mock the security context to return this user
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(testOwner, null, testOwner.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        // Clear security context after each test
        SecurityContextHolder.clearContext();
        // Clean up data after each test
        serviceRecordRepository.deleteAll();
        vehicleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createServiceRecord_persistsAndReturnsRecord() throws Exception {
        Vehicle vehicle = createTestVehicle("VIN-TEST-001", "TEST-001");
        
        Map<String, Object> dto = validServiceRecordPayload(LocalDate.of(2024, 1, 10), vehicle.getId());

        MockMultipartFile jsonPart = new MockMultipartFile(
                "vehicle", "", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(multipart("/api/seviceRecords/save").file(jsonPart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceType").value("Oil Change"));

        assertThat(serviceRecordRepository.findByDateOfService(LocalDate.of(2024, 1, 10))).isPresent();
    }

    @Test
    void updateServiceRecord_updatesStoredEntity() throws Exception {
        Vehicle vehicle = createTestVehicle("VIN-TEST-002", "TEST-002");
        
        ServiceRecord existing = new ServiceRecord();
        existing.setVehicle(vehicle);
        existing.setDateOfService(LocalDate.of(2024, 2, 1));
        existing.setServiceType("Inspection");
        existing.setDescription("Initial desc");
        existing.setOdometer("12000");
        existing.setParts("Filters");
        existing.setCost("200");
        existing.setNextService(LocalDate.of(2024, 8, 1));
        existing.setRecommentdations("None");
        existing.setWarranty("Warranty");
        existing = serviceRecordRepository.save(existing);

        Map<String, Object> dto = validServiceRecordPayload(existing.getDateOfService(), vehicle.getId());
        dto.put("description", "Updated desc");

        mockMvc.perform(patch("/api/serviceRecords/update/" + existing.getId() + "?id=" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated desc"));

        assertThat(serviceRecordRepository.findById(existing.getId())).get()
                .extracting(ServiceRecord::getDescription)
                .isEqualTo("Updated desc");
    }

    @Test
    void deleteServiceRecord_removesEntity() throws Exception {
        Vehicle vehicle = createTestVehicle("VIN-TEST-003", "TEST-003");
        
        ServiceRecord existing = new ServiceRecord();
        existing.setVehicle(vehicle);
        existing.setDateOfService(LocalDate.of(2024, 3, 1));
        existing.setServiceType("Tires");
        existing.setDescription("Rotate");
        existing.setOdometer("15000");
        existing.setParts("None");
        existing.setCost("50");
        existing.setNextService(LocalDate.of(2024, 9, 1));
        existing.setRecommentdations("Check");
        existing.setWarranty("Warranty");
        existing = serviceRecordRepository.save(existing);

        mockMvc.perform(delete("/api/serviceRecords/delete/")
                        .param("id", existing.getId().toString()))
                .andExpect(status().isOk());

        assertThat(serviceRecordRepository.findById(existing.getId())).isEmpty();
    }
    
    private Vehicle createTestVehicle(String vin, String licensePlate) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vin);
        vehicle.setLicencePlate(licensePlate);
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setType(VehicleType.CAR);
        vehicle.setColor(Color.BLACK);
        vehicle.setProductionDate(LocalDate.of(2020, 1, 1));
        vehicle.setFuel(Fuel.PETROL);
        vehicle.setGearbox(Gearbox.AUTOMATIC);
        vehicle.setOdometer("50000");
        vehicle.setOwners(new ArrayList<>());
        vehicle.getOwners().add(testOwner);
        return vehicleRepository.save(vehicle);
    }

    private Map<String, Object> validServiceRecordPayload(LocalDate dateOfService, Long vehicleId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("vehicleId", vehicleId);
        payload.put("dateOfService", dateOfService);
        payload.put("serviceType", "Oil Change");
        payload.put("description", "Full service");
        payload.put("odometer", "10000");
        payload.put("parts", "Filters");
        payload.put("cost", "150");
        payload.put("nextService", dateOfService.plusMonths(6));
        payload.put("recommentdations", "Check brakes");
        payload.put("warranty", "Yes");
        return payload;
    }
}
