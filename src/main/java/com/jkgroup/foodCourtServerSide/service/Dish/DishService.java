package com.jkgroup.foodCourtServerSide.service.Dish;

import com.jkgroup.foodCourtServerSide.dto.Dish.DishDTO;
import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import com.jkgroup.foodCourtServerSide.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DishService {
    // [POST] Create new item
    public Dish createDish(DishDTO dishDTO, MultipartFile image) throws IOException;

    // [GET] All list with searching (not soft-delete)
    public Page<Dish> getAllWithSearching(String searchType, String searchText, int pageNumber, int pageSize);

    // [GET] Trash list
    public Page<Dish> getAllDeletedDishes(int pageNumber, int pageSize);

    // [GET] Get all items by category enum and no soft-delete
    public Page<Dish> getDishesByCategoryName(CategoryEnum categoryName, int pageNumber, int pageSize);

    // [GET] Detail of item
    public Dish getDishById(Integer id);

    // [PUT] Soft-delete item
    public Dish softDeleteDish(int dishId);

    // [PUT] Update item
    public Dish updateDish(int dishId, DishDTO dishDTO, MultipartFile imageFile);

    // [PUT] Restore item from trash
    public Dish restoreDish(int dishId);

    // [DELETE] Permanently delete
    public void deleteDishById(int dishId);

}
