package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String email;
	private String nickname;
	private String password;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;

	public User(String email, String password, String nickname, UserRole userRole) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.userRole = userRole;
	}

	public User(Long id, String email, String nickname, UserRole userRole) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.userRole = userRole;
	}

	public static User fromAuthUser(AuthUser authUser) {
		UserRole userRole = authUser.getAuthorities().stream()
			.map(authority -> UserRole.of(authority.getAuthority()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않은 UserRole"));

		return new User(authUser.getId(), authUser.getEmail(), authUser.getNickname(), userRole);
	}

	public void changePassword(String password) {
		this.password = password;
	}

	public void updateRole(UserRole userRole) {
		this.userRole = userRole;
	}
}
