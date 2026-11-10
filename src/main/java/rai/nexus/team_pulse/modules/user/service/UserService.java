package rai.nexus.team_pulse.modules.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rai.nexus.team_pulse.common.config.exception.BadRequestException;
import rai.nexus.team_pulse.common.config.exception.ResourceNotFoundException;
import rai.nexus.team_pulse.common.service.user.PasswordGeneratorService;
import rai.nexus.team_pulse.modules.user.dto.CreateUserRequestDto;
import rai.nexus.team_pulse.modules.user.dto.UpdateUserRequestDto;
import rai.nexus.team_pulse.modules.user.dto.UserResponseDto;
import rai.nexus.team_pulse.modules.user.entity.User;
import rai.nexus.team_pulse.modules.user.repository.UserRepository;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGeneratorService passwordGeneratorService;

    @Transactional
    public UserResponseDto create(
            CreateUserRequestDto dto
    ) {
        // validate email availability
        log.debug("Validating email {}...", dto.email());
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }

        // generate the password
        log.debug("Generating and hashing password...");
        String generatedPassword = passwordGeneratorService.generatePassword(8);

        // hash the password to be saved into the DB
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        // create user
        log.debug("Creating and saving user...");
        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(hashedPassword);
        user.setRole(dto.role());

        // save the user
        User savedUser = userRepository.save(user);

        log.info("User created successfully");

        return responseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAll(Pageable pageable) {

        log.debug("Retrieving all users...");
        Page<User> users = userRepository.findAll(pageable);
        log.info("Retrieved all users successfully");

        return users.map(this::responseDto);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getOne(UUID id) {
        log.debug("Retrieving user with id {}...", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.info("Retrieved user with id {} successfully", id);

        return responseDto(user);
    }

    @Transactional
    public UserResponseDto update(UUID id, UpdateUserRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.debug("Updating user with id {}...", id);
        // update the user
        user.setEmail(dto.email());
        user.setRole(dto.role());

        // save changes
        User savedUser = userRepository.save(user);

        log.info("User successfully updated");

        return responseDto(savedUser);
    }

    @Transactional
    public void delete(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.debug("Deleting user...");
         user.setIsActive(false);

         log.info("User successfully deleted");

//        userRepository.delete(user);

    }

    private UserResponseDto responseDto(User user) {
        return new UserResponseDto(
                user.getId().toString(),
                user.getEmail(),
                user.getRole(),
                user.getIsActive(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getVersion()
        );
    }
}
