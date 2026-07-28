package com.example.demo;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByUserName(request.getUserName())
				.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}

		String token = jwtUtil.generateToken(user.getUserName());
		return new LoginResponse(token, user.getUserName());
	}

	public User register(String userName, String rawPassword) {
		if (userRepository.existsByUserName(userName)) {
			throw new IllegalArgumentException("Username already exists");
		}
		User user = new User(userName, passwordEncoder.encode(rawPassword));
		return userRepository.save(user);
	}
}
