package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService,@Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
      List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDto(userRepository.findByUserNameAndIsDeleted(username,false));
    }

    @Override
    public void save(UserDTO user) {

      userRepository.save(userMapper.convertToEntity(user));
    }

    // This delete is for hard deleted
    @Override
    public void deleteByUserName(String username) {
    userRepository.deleteByUserName(username);
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User>users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role,false);
        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO update(UserDTO user) {
        // Find current user
        User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(),false); // has id
        // Map update user dto to entity object
        User convertedUser = userMapper.convertToEntity(user); // has id?
        // Set id to the converted object
        convertedUser.setId(user1.getId());
        // Save the updated user in the db
        userRepository.save(convertedUser);

        return findByUserName(user.getUserName());
    }

    // This delete is for soft deleted
    @Override
    public void delete(String username) {
    // Go to db and get that user with user…name
    // Change the isDeleted field to true
    // Save the object in the db
        User user = userRepository.findByUserNameAndIsDeleted(username,false);

        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());
            //harold@manager.com-1 because of the change of the userName after deletion if the same user want to use same username just like "harold@manager.com" he/she can use without a problem.
            userRepository.save(user);
        }
    }

    // here we use private method because we use this method only in this class.
    private boolean checkIfUserCanBeDeleted(User user){
        switch (user.getRole().getDescription()){
            case "Manager":
            List<ProjectDTO> projectDTOList=projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
            return projectDTOList.size() ==0;

            case "Employee":
            List<TaskDTO> taskDTOList=taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
            return taskDTOList.size() ==0;

            default:
                return true;
        }

    }

}
