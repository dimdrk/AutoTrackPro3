package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceRecordReadOnlyDTO {
    private Long id;
    private Long vehicleId;
    private String vehicleVin;
    private String vehicleLicencePlate;
    private LocalDate dateOfService;
    private String serviceType;
    private String description;
    private String odometer;
    private String parts;
    private String cost;
    private LocalDate nextService;
    private String recommentdations;
    private String warranty;
}
