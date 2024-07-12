package com.jkgroup.foodCourtServerSide.service.Dish;

import com.jkgroup.foodCourtServerSide.dto.Dish.DishDTO;
import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.model.SearchForm;
import com.jkgroup.foodCourtServerSide.repository.Dish.DishRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private SearchForm searchForm;

    // [GET] Get all list (not soft-delete, pagination, searching)
    @Override
    public Page<Dish> getAllWithSearching(String searchType, String searchText, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // Set default values if searchType or searchText is empty
        String effectiveSearchType = searchType.isEmpty() ? "ALL" : searchType;
        String effectiveSearchText = searchText.isEmpty() ? "" : searchText;
        searchForm.setSearchType(effectiveSearchType);
        searchForm.setSearchText(effectiveSearchText);

        // Perform search based on searchType
        switch (effectiveSearchType) {
            case "ALL":
                return dishRepository.findByIsDeletedFalse(pageable);
            case "NAME":
                String nameInput = searchForm.getSearchText();
                return dishRepository.findAllByName(nameInput, pageable);
            case "PRICE":
                BigDecimal priceInput = BigDecimal.valueOf(Long.parseLong(searchForm.getSearchText()));
                return dishRepository.findAllIsEqualAndLessThanThePrice(priceInput, pageable);
            default:
                // If searchType is not recognized, return an empty Page
                return Page.empty();
        }
    }

    // [GET] Get detail of item
    @Override
    public Dish getDishById(Integer id) {
        return dishRepository.findById(id).orElse(null);
    }

    // [GET] Get trash item list
    @Override
    public Page<Dish> getAllDeletedDishes(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return dishRepository.findByIsDeletedTrue(pageable);
    }

    // [GET] Get all items by category enum and no soft-delete
    @Override
    public Page<Dish> getDishesByCategoryName(CategoryEnum categoryName, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return dishRepository.findByCategoryEnumAndIsDeletedFalse(categoryName, pageable);
    }

    // [POST] Create new item
    @Override
    public Dish createDish(DishDTO dishDTO, MultipartFile image) throws IOException {
        byte[] imageData = image.getBytes();

        Dish dish = new Dish();
        dish.setName(dishDTO.getName());
        dish.setOriginalPrice(dishDTO.getOriginalPrice());
        dish.setDiscount(dishDTO.getDiscount());
        dish.setPreparationTime(dishDTO.getPreparationTime());
        dish.setMealSet(dishDTO.getMealSet());
        dish.setImage(imageData);
        dish.setCategoryEnum(dishDTO.getCategoryEnum());
        dish.setCreatedAt(LocalDateTime.now());
        dish.setCreatedBy(dishDTO.getCreatedBy());
        dish.setUpdatedBy(dishDTO.getUpdatedBy());
        dish.setDeleted(false);

        return dishRepository.save(dish);
    }

    // [PUT] Soft delete item
    @Override
    public Dish softDeleteDish(int dishId) {
        Dish dish = dishRepository.findById(dishId).orElse(null);

        if (dish != null) {
            dish.setDeleted(true);
            return dishRepository.save(dish);
        }
        return null;
    }

    // [PUT] Update item
    @Override
    public Dish updateDish(int dishId, DishDTO dishDTO, MultipartFile imageFile) {
        Dish existingDish = dishRepository.findById(dishId).orElse(null);
        if (existingDish != null) {
            // Update existingDish with data from dishDTO
            BeanUtils.copyProperties(dishDTO, existingDish, "dishId", "createdAt", "createdBy", "isDeleted");

            // Update image if provided
            try {
                if (imageFile != null && !imageFile.isEmpty()) {
                    existingDish.setImage(imageFile.getBytes());
                }
            } catch (IOException e) {
                // Handle exception
                e.printStackTrace();
            }

            // Set update by
            existingDish.setUpdatedBy(dishDTO.getUpdatedBy());

            // Save the updated dish
            return dishRepository.save(existingDish);
        }
        return null; // Dish not found
    }

    // [PUT] Restore item from trash
    @Override
    public Dish restoreDish(int dishId) {
        Dish dish = dishRepository.findById(dishId).orElse(null);

        if (dish != null) {
            dish.setDeleted(false);
            return dishRepository.save(dish);
        }
        return null;
    }

    // [DELETE] Permanently delete item from trash
    @Override
    public void deleteDishById(int dishId) {
        Dish dish = dishRepository.findById(dishId).orElse(null);

        if (dish != null) {
            dishRepository.deleteById(dishId);
        } else {
            throw new IllegalArgumentException("Dish with ID " + dishId + " does not exist.");
        }
    }

}
