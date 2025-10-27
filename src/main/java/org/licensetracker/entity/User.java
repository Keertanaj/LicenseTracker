package org.licensetracker.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="user_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String mobile;
    @Enumerated(EnumType.STRING)
    private Role role;
}
