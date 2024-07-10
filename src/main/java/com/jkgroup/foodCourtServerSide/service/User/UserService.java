package com.jkgroup.foodCourtServerSide.service.User;

import com.jkgroup.foodCourtServerSide.dto.User.UserDTO;
import com.jkgroup.foodCourtServerSide.dto.User.UserWillUpdateDTO;
import com.jkgroup.foodCourtServerSide.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserService {
    // [GET] All list with searching (not soft-delete)
    public Page<User> getAllWithSearching(String searchType, String searchText, int pageNumber, int pageSize);

    // [GET] Get trash item list
    public Page<User> getAllDeletedUsers(int pageNumber, int pageSize);

    // [GET] Get detail of item
    public User getUserById(int id);

    // [GET] Get User Detail By Email
    public Optional<User> getUserByEmail(String email);

    // [POST] Create new item
    public User createUser(UserDTO userDTO);

    // [POST] Upload Avatar And Change Avatar (if it existed in the Database)
    public Optional<User> saveAvatar(String email, MultipartFile file) throws IOException;

    // [PUT] Update item
    public User updateUser(int userId, UserWillUpdateDTO userDTO);

    // [PUT] Soft-delete item
    public User softDeleteUser(int userId);

    // [PUT] Restore item from trash
    public User restoreUser(int userId);

    // [DELETE] Permanently delete
    public void deleteUserById(int userId);

}
