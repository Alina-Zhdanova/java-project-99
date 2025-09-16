package hexlet.code.service;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.UserHasTasksException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();
        var result = users.stream()
            .map(userMapper::map)
            .toList();

        return result;
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) {
        var user = userMapper.map(userCreateDTO);
        userRepository.save(user);
        var userDTO = userMapper.map(user);

        return userDTO;
    }

    public UserDTO findUserById(Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        var userDTO = userMapper.map(user);

        return userDTO;
    }

    public UserDTO updateUser(UserUpdateDTO userUpdateDTO, Long id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        userMapper.update(userUpdateDTO, user);
        userRepository.save(user);
        var userDTO = userMapper.map(user);

        return userDTO;
    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new UserHasTasksException("Deletion is not possible, the User is associated with one or more tasks.");
        }
    }
}
