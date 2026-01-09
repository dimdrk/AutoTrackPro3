package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Color;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Fuel;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gearbox;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.VehicleType;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.VehicleRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class VehicleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void createVehicle_persistsAndReturnsVehicle() throws Exception {
        Map<String, Object> dto = validVehiclePayload("VIN-123", "ABC-1234");

        mockMvc.perform(post("/api/vehicles/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("VIN-123"))
                .andExpect(jsonPath("$.licencePlate").value("ABC-1234"));

        assertThat(vehicleRepository.findByVin("VIN-123")).isPresent();
    }

    @Test
    void updateVehicle_updatesStoredEntity() throws Exception {
        Vehicle existing = new Vehicle();
        existing.setVin("VIN-200");
        existing.setLicencePlate("XYZ-2000");
        existing.setMake("Honda");
        existing.setModel("Civic");
        existing.setType(VehicleType.CAR);
        existing.setColor(Color.BLACK);
        existing.setProductionDate(LocalDate.of(2020, 1, 1));
        existing.setFuel(Fuel.PETROL);
        existing.setGearbox(Gearbox.MANUAL);
        existing.setOdometer("12000");
        existing = vehicleRepository.save(existing);

        Map<String, Object> dto = validVehiclePayload("VIN-200", "XYZ-2000");
        dto.put("model", "Accord");

        mockMvc.perform(patch("/api/vehicles/update/" + existing.getId() + "?id=" + existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Accord"));

        assertThat(vehicleRepository.findById(existing.getId())).get()
                .extracting(Vehicle::getModel)
                .isEqualTo("Accord");
    }

    @Test
    void deleteVehicle_removesEntity() throws Exception {
        Vehicle existing = new Vehicle();
        existing.setVin("VIN-300");
        existing.setLicencePlate("XYZ-3000");
        existing.setMake("Ford");
        existing.setModel("Focus");
        existing.setType(VehicleType.CAR);
        existing.setColor(Color.BLUE);
        existing.setProductionDate(LocalDate.of(2019, 5, 5));
        existing.setFuel(Fuel.DIESEL);
        existing.setGearbox(Gearbox.AUTOMATIC);
        existing.setOdometer("34000");
        existing = vehicleRepository.save(existing);

        mockMvc.perform(delete("/api/vehicles/delete/")
                        .param("id", existing.getId().toString()))
                .andExpect(status().isOk());

        assertThat(vehicleRepository.findById(existing.getId())).isEmpty();
    }

    private Map<String, Object> validVehiclePayload(String vin, String licencePlate) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("vin", vin);
        payload.put("licencePlate", licencePlate);
        payload.put("make", "Toyota");
        payload.put("model", "Corolla");
        payload.put("type", VehicleType.CAR.name());
        payload.put("color", Color.BLACK.name());
        payload.put("productionDate", LocalDate.of(2021, 1, 1));
        payload.put("fuel", Fuel.PETROL.name());
        payload.put("gearbox", Gearbox.MANUAL.name());
        payload.put("odometer", "10000");
        return payload;
    }
}
