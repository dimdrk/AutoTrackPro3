package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceRecordInsertDTO {

    @NotNull(message = "Vehicle ID must not be null.")
    private Long vehicleId;

    @NotNull(message = "Date of Service must not be null.")
    private LocalDate dateOfService;

    @NotNull(message = "Service type must not be null.")
    private String serviceType;

    @NotNull(message = "Description must not be null.")
    private String description;

    @NotNull(message = "Odometer must not be null.")
    private Long odometer;

    @NotNull(message = "Parts must not be null.")
    private String parts;

    @NotNull(message = "Cost must not be null.")
    private String cost;

    private LocalDate nextService;

    private Long nextServiceOdometer;

    private String recommentdations;

    private Boolean warranty;

    private String warrantyInfo;
}
