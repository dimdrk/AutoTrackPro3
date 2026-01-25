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

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gender;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.RoleType;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_persistsAndReturnsUser() throws Exception {
        Map<String, Object> dto = validUserPayload("User123", "Passw0rd@1", "user123@example.com", "1234567890", "DL-CR-123456", "ID-12345");

        mockMvc.perform(post("/api/register/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("User123"))
                .andExpect(jsonPath("$.email").value("user123@example.com"));

        assertThat(userRepository.findByUsername("User123")).isPresent();
        assertThat(userRepository.findByEmail("user123@example.com")).isPresent();
    }

    @Test
    void listUsers_returnsUsers() throws Exception {
        // Seed two users
        User u1 = seedUser("SeedUser1", "seed1@example.com", "1111111111", "ID-11111");
        User u2 = seedUser("SeedUser2", "seed2@example.com", "2222222222", "ID-22222");

        mockMvc.perform(post("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").exists());

        assertThat(userRepository.findById(u1.getId())).isPresent();
        assertThat(userRepository.findById(u2.getId())).isPresent();
    }

    @Test
    void updateUser_updatesStoredEntity() throws Exception {
        User existing = seedUser("UpUser", "up@example.com", "3333333333", "ID-33333");

        Map<String, Object> dto = validUserPayload("UpUser1", "Passw0rd@2", "up2@example.com", "3333333333", "DL-UP-123456", "ID-33333");
        dto.put("city", "Thessaloniki");

        mockMvc.perform(patch("/api/user/update")
                        .param("id", existing.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("up2@example.com"))
                .andExpect(jsonPath("$.city").value("Thessaloniki"));

        assertThat(userRepository.findById(existing.getId())).get()
                .extracting(User::getEmail)
                .isEqualTo("up2@example.com");
    }

    @Test
    void deleteUser_removesEntity() throws Exception {
        User existing = seedUser("DelUser", "del@example.com", "4444444444", "ID-44444");

        mockMvc.perform(delete("/api/user/delete")
                        .param("id", existing.getId().toString()))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(existing.getId())).isEmpty();
    }

    private Map<String, Object> validUserPayload(String username, String password, String email, String phone, String driverLicence, String identity) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("isActive", true);
        payload.put("username", username);
        payload.put("password", password);
        payload.put("firstname", "John");
        payload.put("lastname", "Doe");
        payload.put("email", email);
        payload.put("phonenumber", phone);
        payload.put("gender", Gender.MALE.name());
        payload.put("role", RoleType.OWNER.name());
        payload.put("driverLicence", driverLicence);
        payload.put("licenceExpiration", LocalDate.of(2030, 1, 1));
        payload.put("licenceCategory", "B");
        payload.put("identityNumber", identity);
        payload.put("city", "Athens");
        return payload;
    }

    private User seedUser(String username, String email, String phone, String identity) {
        User u = new User();
        u.setIsActive(true);
        u.setUsername(username);
        u.setPassword("Passw0rd@1");
        u.setFirstname("Jane");
        u.setLastname("Smith");
        u.setEmail(email);
        u.setPhonenumber(phone);
        u.setGender(Gender.FEMALE);
        u.setRoleType(RoleType.OWNER);
        u.setDriverLicence("DL-" + username);
        u.setLicenceExpiration(LocalDate.of(2030, 6, 1));
        u.setLicenceCategory("B");
        u.setIdentityNumber(identity);
        u.setCity("Athens");
        return userRepository.save(u);
    }
}
