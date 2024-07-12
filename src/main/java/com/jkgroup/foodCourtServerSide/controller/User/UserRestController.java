package com.jkgroup.foodCourtServerSide.controller.User;

import com.jkgroup.foodCourtServerSide.constant.Exception.ExceptionConstant;
import com.jkgroup.foodCourtServerSide.dto.User.UserDTO;
import com.jkgroup.foodCourtServerSide.dto.User.UserWillUpdateDTO;
import com.jkgroup.foodCourtServerSide.enums.UserRoleEnum;
import com.jkgroup.foodCourtServerSide.exception.ValidationException;
import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.model.User;
import com.jkgroup.foodCourtServerSide.service.User.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@Tag(name = "User REST Controller", description = "Endpoints related to User")
@RequestMapping("/api/user")
public class UserRestController {
    @Autowired
    private UserService userService;


    // [GET] Role Enum List
    @RequestMapping(value = "/role-list", method = RequestMethod.GET)
    public ResponseEntity<?> roleEnumList() {
        List<UserRoleEnum> userRoleEnums = Arrays.asList(UserRoleEnum.values());

        // Throw exception if the list is empty (case 1), is inaccessible (case 2)
        if (userRoleEnums == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve roles. Please try again later!");
        } else if (userRoleEnums.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No roles found!");
        } else {
            return ResponseEntity
                    .ok(userRoleEnums);
        }
    }

    // [GET] All List With Searching (not soft-delete)
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> listAllNonDeletedItems(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(required = false, defaultValue = "ALL") String searchType,
            @RequestParam(required = false, defaultValue = "") String searchText
    ) {
        Page<User> usersList = userService.getAllWithSearching(searchType, searchText, pageNumber, pageSize);

        // Throw exception if the list is empty (case 1), is inaccessible (case 2)
        if (usersList == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve users. Please try again later!");
        } else if (usersList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No users found!");
        } else {
            return ResponseEntity
                    .ok(usersList);
        }
    }

    // [GET] Trash List
    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    public ResponseEntity<?> trashList(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize
    ) {
        Page<User> trashList = userService.getAllDeletedUsers(pageNumber, pageSize);

        if (trashList == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve trash list. Please try again later!");
        } else if (trashList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No trash list found!");
        } else {
            return ResponseEntity
                    .ok(trashList);
        }
    }

    // [GET] Detail Of An User
    @GetMapping("/{id}")
    public ResponseEntity<?> detailOfItem(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity
                    .ok(user);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with ID: " + id + " not found!");
        }
    }

    // [GET] User Detail by Email
    @GetMapping("/find-by-email")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with email: " + email + " not found!");
        }
    }

    // [POST] Create An User
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createNewItem(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) { // Check for validation errors
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
//            System.out.println("ERRORS :" + errors.toString());
            throw new ValidationException(ExceptionConstant.VALIDATION_ERROR, errors);
        }

        User createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // [POST] Upload Avatar And Change Avatar (if it existed in the Database)
    @PostMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("email") String email,
                                          @RequestParam("file") MultipartFile file) {
        try {
            Optional<User> userOptional = userService.saveAvatar(email, file);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return ResponseEntity.ok(user); // Return the updated User object
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload avatar");
        }
    }

    // [PUT] Update User
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateItem(
            @PathVariable int userId,
            @Valid @RequestBody UserWillUpdateDTO willUpdateDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) { // Check for validation errors
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationException(ExceptionConstant.VALIDATION_ERROR, errors);
        }

        User updatedUser = userService.updateUser(userId, willUpdateDTO);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with ID: " + userId + " not found!");
        }
    }

    // [PUT] Soft Delete An User
    @PutMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteItem(@PathVariable int id) {
        User user = userService.getUserById(id);

        if (user != null) {
            User softDeletedDish = userService.softDeleteUser(id);
            return ResponseEntity
                    .ok(softDeletedDish);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with ID: " + id + " not found!");
        }
    }

    // [PUT] Restore From Trash
    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable int id) {
        User user = userService.getUserById(id);

        if (user != null) {
            User restoreUser = userService.restoreUser(id);
            return ResponseEntity
                    .ok(restoreUser);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with ID: " + id + " not found!");
        }
    }

    // [DELETE] Permanently Delete
    @DeleteMapping("/permanently-delete/{id}")
    public ResponseEntity<?> permanentlyDelete(@PathVariable int id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User with ID: " + id + " not found!");
        }
    }

}
