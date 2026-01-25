package gr.dimitriosdrakopoulos.projects.AutoTrackPro3.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.Gender;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.core.enums.RoleType;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.model.User;
import gr.dimitriosdrakopoulos.projects.AutoTrackPro3.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Check if admin user already exists
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUuid(java.util.UUID.randomUUID().toString());
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFirstname("Admin");
                admin.setLastname("User");
                admin.setEmail("admin@autotrackpro.com");
                admin.setPhonenumber("+30-1234567890");
                admin.setGender(Gender.MALE);
                admin.setRoleType(RoleType.SUPER_ADMIN);
                admin.setDriverLicence("DL-ADMIN-001");
                admin.setLicenceExpiration(LocalDate.now().plusYears(5));
                admin.setLicenceCategory("B");
                admin.setIdentityNumber("ID-ADMIN-001");
                admin.setCity("Athens");
                admin.setIsActive(true);
                
                userRepository.save(admin);
                System.out.println("✅ Admin user created successfully!");
                System.out.println("   Username: admin");
                System.out.println("   Password: admin123");
            }
            
            // Check if test user already exists
            if (userRepository.findByUsername("testuser").isEmpty()) {
                User testUser = new User();
                testUser.setUuid(java.util.UUID.randomUUID().toString());
                testUser.setUsername("testuser");
                testUser.setPassword(passwordEncoder.encode("test123"));
                testUser.setFirstname("Test");
                testUser.setLastname("User");
                testUser.setEmail("test@autotrackpro.com");
                testUser.setPhonenumber("+30-9876543210");
                testUser.setGender(Gender.MALE);
                testUser.setRoleType(RoleType.OWNER);
                testUser.setDriverLicence("DL-TEST-001");
                testUser.setLicenceExpiration(LocalDate.now().plusYears(3));
                testUser.setLicenceCategory("B");
                testUser.setIdentityNumber("ID-TEST-001");
                testUser.setCity("Thessaloniki");
                testUser.setIsActive(true);
                
                userRepository.save(testUser);
                System.out.println("✅ Test user created successfully!");
                System.out.println("   Username: testuser");
                System.out.println("   Password: test123");
            }
        };
    }
}
