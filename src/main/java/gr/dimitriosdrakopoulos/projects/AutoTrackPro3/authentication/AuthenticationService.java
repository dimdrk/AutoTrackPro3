package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.authentication;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.exceptions.AppObjectNotAuthorizedException;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.AuthenticationRequestDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.dto.AuthenticationResponseDTO;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.UserRepository;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized."));

        String token = jwtService.generateToken(authentication.getName(), user.getRoleType().name());
        return new AuthenticationResponseDTO(user, token);
    }
}
