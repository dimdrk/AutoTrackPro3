package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto;

import java.time.LocalDate;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gender;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInsertDTO {

    @NotNull(message = "Is active must not be null.")
    private Boolean isActive;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9]).{5,}$", message = "Invalid username.")
    @NotEmpty(message = "Username must not be empty.")
    private String username;

    @Pattern(regexp = "^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[@#$%!^&*]).{8,}$", message = "Invalid password.")
    @NotEmpty(message = "Password must not be empty.")
    private String password;

    @NotEmpty(message = "Firstname must not be empty.")
    private String firstname;

    @NotEmpty(message = "Lastname must not be empty.")
    private String lastname;

    @Email(message = "Invalid email.")
    private String email;
    
    @Pattern(regexp = "^(?=.*?[0-9]).{10,}$", message = "Invalid phone number.")
    @NotEmpty(message = "Phone number must not be empty.")
    private String phonenumber;

    @NotNull(message = "Gender must not be null.")
    private Gender gender;

    @NotNull(message = "Role must not be null.")
    private RoleType role;
    
    @NotNull(message = "Driver's licence must not be null.")
    private String driverLicence;

    @NotNull(message = "Licence expiration must not be null.")
    private LocalDate licenceExpiration;

    @NotNull(message = "Licence category must not be null.")
    private String licenceCategory;

    @NotNull(message = "Identity number must not be null.")
    private String identityNumber;
    
    @NotNull(message = "City must not be null.")
    private String city;

}
