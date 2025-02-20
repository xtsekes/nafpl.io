package dev.nafplio.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
class UserRolePrimaryKey {
    private String userId;
    private String roleId;
}

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@IdClass(UserRolePrimaryKey.class)
@Table(name = "userroles")
public class UserRole implements dev.nafplio.auth.UserRole<String, String> {
    @Id
    private String userId;

    @Id
    private String roleId;
}