package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.mapper;

import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserInsertDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.UserReadOnlyDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.Vehicle;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    
    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        var dto = new UserReadOnlyDTO();

        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setPhonenumber(user.getPhonenumber());
        dto.setDriverLicence(user.getDriverLicence());
        dto.setLicenceExpiration(user.getLicenceExpiration());
        dto.setLicenceCategory(user.getLicenceCategory());
        dto.setIdentityNumber(user.getIdentityNumber());
        dto.setCity(user.getCity());
        dto.setIsActive(user.getIsActive());
        dto.setOwnedVehicleIds(user.getOwnedVehicles().stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList()));
        dto.setDrivenVehicleIds(user.getDrivenVehicles().stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList()));

        return dto;
    }

    public User mapToUserEntity(UserInsertDTO userInsertDTO) {
        User user = new User();

        user.setUsername(userInsertDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userInsertDTO.getPassword()));
        user.setFirstname(userInsertDTO.getFirstname());
        user.setLastname(userInsertDTO.getLastname());
        user.setEmail(userInsertDTO.getEmail());
        user.setPhonenumber(userInsertDTO.getPhonenumber());
        user.setGender(userInsertDTO.getGender());
        user.setRoleType(userInsertDTO.getRole());
        user.setDriverLicence(userInsertDTO.getDriverLicence());
        user.setLicenceExpiration(userInsertDTO.getLicenceExpiration());
        user.setLicenceCategory(userInsertDTO.getLicenceCategory());
        user.setIdentityNumber(userInsertDTO.getIdentityNumber());
        user.setCity(userInsertDTO.getCity());
        user.setIsActive(userInsertDTO.getIsActive());

        return user;
    }
}
