package com.harri.training1.services;

import com.harri.training1.exceptions.UserFoundException;
import com.harri.training1.mapper.AutoMapper;
import com.harri.training1.models.dto.UserDto;
import com.harri.training1.models.entities.User;
import com.harri.training1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The UserService class provides user-related services.
 */

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<UserDto, Long>{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final AutoMapper<User, UserDto> mapper;

    /**
     * Get all the users in system
     *
     * @return a list of users
     */
    @Override
    public List<UserDto> findAll(){
        List<User> users = userRepository.findAll();

        if(users.isEmpty()) {
            LOGGER.error("No any user exist in the system!");
            throw new UserFoundException("No any user in users table!");
        }

        return users.stream()
                .map(user -> mapper.toDto(user, UserDto.class)).toList();
    }

    /**
     * Get user from repository by user id
     *
     * @param id user id
     * @return a user
     */
    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id);

        if(user == null) {
            LOGGER.error("This user with id = " + id + " not exist!");
            throw new UserFoundException("This user with id = " + id + " not exist!");
        }

        return mapper.toDto(user, UserDto.class);
    }

    @Override
    public void update(UserDto userDto){
        var user = mapper.toModel(userDto, User.class);
        long result = userRepository.update(user);

        if (result == 0){
            LOGGER.error("User with id = " + userDto.getId() + " Not exist!");
            throw new UserFoundException("User with id = " + userDto.getId() + " Not exist!");
        }
    }

    /**
     * Retrieve the user with specific name
     * @param name the username I want to search based on it
     * @return the list of users
     */
    public UserDto findByUsername(String name){
        User user = userRepository.findByUsername(name);
        return mapper.toDto(user, UserDto.class);
    }

    /**
     * Retrieve the users with specific role
     *
     * @param roleName the role
     * @return a list of user with specific role
     */
    public List<UserDto> findByRole(String roleName){
        List<User> users = userRepository.findByRole(roleName);

        if (users.isEmpty())
            throw new UserFoundException("User not exist with role: " + roleName);

        return users.stream()
                .map(user -> mapper.toDto(user, UserDto.class))
                .toList();
    }

    /**
     * Delete a user
     *
     * @param id user id to delete
     */
    public void deleteById(Long id){
        Long result = userRepository.deleteById(id);

        if (result == 0){
            LOGGER.error("User with id = " + id + " Not exist!");
            throw new UserFoundException("User with id = " + id + " Not exist!");
        }
    }
}
