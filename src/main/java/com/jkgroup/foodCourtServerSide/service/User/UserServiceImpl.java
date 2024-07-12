package com.jkgroup.foodCourtServerSide.service.User;

import com.jkgroup.foodCourtServerSide.constant.Exception.ExceptionConstant;
import com.jkgroup.foodCourtServerSide.dto.User.UserDTO;
import com.jkgroup.foodCourtServerSide.dto.User.UserWillUpdateDTO;
import com.jkgroup.foodCourtServerSide.exception.ValidationException;
import com.jkgroup.foodCourtServerSide.model.SearchForm;
import com.jkgroup.foodCourtServerSide.model.User;
import com.jkgroup.foodCourtServerSide.repository.User.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SearchForm searchForm;


    // [GET] Get all list (not soft-delete, pagination, searching)
    @Override
    public Page<User> getAllWithSearching(String searchType, String searchText, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Set default values if searchType or searchText is empty
        String effectiveSearchType = searchType.isEmpty() ? "ALL" : searchType;
        String effectiveSearchText = searchText.isEmpty() ? "" : searchText;
        searchForm.setSearchType(effectiveSearchType);
        searchForm.setSearchText(effectiveSearchText);

        // Perform search based on searchType
        switch (effectiveSearchType) {
            case "ALL":
                return userRepository.findByIsDeletedFalse(pageable);
            case "NAME":
                String nameInput = searchForm.getSearchText();
                return userRepository.findAllByName(nameInput, pageable);
            case "PHONE":
                String phoneInput = searchForm.getSearchText();
                return userRepository.findAllByPhone(phoneInput, pageable);
            default:
                // If searchType is not recognized, return an empty Page
                return Page.empty();
        }
    }

    // [GET] Get detail of item
    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    // [GET] Get trash item list
    @Override
    public Page<User> getAllDeletedUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findByIsDeletedTrue(pageable);
    }

    // [GET] User Detail by Email
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // [POST] Create new item
    @Override
    public User createUser(UserDTO userDTO) {
        List<String> errors = new ArrayList<>();

        // Check if the username already exists
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            errors.add("Username already exists");
        }

        // Check if the email already exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            errors.add("Email already exists");
        }

        // Check if the phone already exists
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            errors.add("Phone already exists");
        }

        // If there are validation errors, throw ValidationException
        if (!errors.isEmpty()) {
            throw new ValidationException(ExceptionConstant.DUPLICATE_ERROR, errors);
        }

        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setPhone(userDTO.getPhone());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setRoleEnum(userDTO.getUserRoleEnum());
        user.setCreatedAt(LocalDateTime.now());
        user.setCreatedBy(userDTO.getCreatedBy());
        user.setUpdatedBy(userDTO.getUpdatedBy());
        user.setDeleted(false);

        return userRepository.save(user);
    }

    // [POST] Upload Avatar And Change Avatar (if it existed in the Database)
    @Override
    public Optional<User> saveAvatar(String email, MultipartFile file) throws IOException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAvatar(file.getBytes());
            userRepository.save(user);
            return Optional.of(user); // Return the Updated User Object. "Optional.of(user)" creates a new Optional containing the updated User object. This "Optional<User>" is then returned, allowing the caller to handle the updated user information as needed
        } else {
            return Optional.empty();
        }
    }

    // [PUT] Update item
    @Override
    public User updateUser(int userId, UserWillUpdateDTO userDTO) {
        User existingUser = userRepository.findById(userId).orElse(null);
        List<String> errors = new ArrayList<>();
        if (existingUser != null) {
            // Check if the email already exists (excluding current user)
            if (userRepository.existsByEmailAndUserIdNot(userDTO.getEmail(), userId)) {
                errors.add("Email already exists");
            }

            // Check if the phone already exists (excluding current user)
            if (userRepository.existsByPhoneAndUserIdNot(userDTO.getPhone(), userId)) {
                errors.add("Phone already exists");
            }

            // If there are validation errors, throw ValidationException
            if (!errors.isEmpty()) {
                throw new ValidationException(ExceptionConstant.DUPLICATE_ERROR, errors);
            }

            // Update existingUser with data from dishDTO
            BeanUtils.copyProperties(userDTO, existingUser, "userId", "userName", "password", "createdAt", "createdBy", "isDeleted");

            // Save the updated item
            return userRepository.save(existingUser);
        }
        return null; // User not found
    }

    // [PUT] Soft delete item
    @Override
    public User softDeleteUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.setDeleted(true);
            return userRepository.save(user);
        }
        return null;
    }

    // [PUT] Restore item from trash
    @Override
    public User restoreUser(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.setDeleted(false);
            return userRepository.save(user);
        }
        return null;
    }

    // [DELETE] Permanently delete item from trash
    @Override
    public void deleteUserById(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            userRepository.deleteById(userId);
        } else {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
    }

}
