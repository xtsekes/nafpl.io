package dev.nafplio.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
class UserRolePrimaryKey {
    private String userId;
    private String roleId;
}

@Setter
@Getter
@NoArgsConstructor
@Entity
@IdClass(UserRolePrimaryKey.class)
@Table(name = "userroles")
class UserRole {
    @Id
    private String userId;

    @Id
    private String roleId;
}