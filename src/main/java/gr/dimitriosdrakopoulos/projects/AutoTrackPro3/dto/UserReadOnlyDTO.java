package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {
    
    private Boolean isActive;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phonenumber;
    private String driverLicence;
    private LocalDate licenceExpiration;
    private String licenceCategory;
    private String identityNumber;
    private String city;
    private List<Long> ownedVehicleIds;
    private List<Long> drivenVehicleIds;
}
