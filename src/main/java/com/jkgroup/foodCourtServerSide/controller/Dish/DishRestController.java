package com.jkgroup.foodCourtServerSide.controller.Dish;

import com.jkgroup.foodCourtServerSide.constant.Exception.ExceptionConstant;
import com.jkgroup.foodCourtServerSide.dto.Dish.DishDTO;
import com.jkgroup.foodCourtServerSide.enums.CategoryEnum;
import com.jkgroup.foodCourtServerSide.exception.ValidationException;
import com.jkgroup.foodCourtServerSide.model.Dish;
import com.jkgroup.foodCourtServerSide.service.Dish.DishServiceImpl;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Tag(name = "Dish REST Controller", description = "Endpoints related to Dish")
@RequestMapping("/api/dish")
public class DishRestController {
    @Autowired
    private DishServiceImpl dishService;

    // [GET] Category Enum List
    @RequestMapping(value = "/category-list", method = RequestMethod.GET)
    public ResponseEntity<?> categoryEnumList() {
        List<CategoryEnum> categoryEnums = Arrays.asList(CategoryEnum.values());

        // Throw exception if the list is empty (case 1), is inaccessible (case 2)
        if (categoryEnums == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve categories. Please try again later!");
        } else if (categoryEnums.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No categories found!");
        } else {
            return ResponseEntity
                    .ok(categoryEnums);
        }
    }

    // [GET] Get Dishes By Category Name
    @RequestMapping(value = "/dishes-by-category", method = RequestMethod.GET)
    public ResponseEntity<?> getAllDishesByCategoryName(
            @RequestParam(defaultValue = "MAIN_COURSES") String categoryName,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        CategoryEnum categoryEnum;
        try {
            categoryEnum = CategoryEnum.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle invalid category name
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid category name!");
        }

        Page<Dish> dishesList = dishService.getDishesByCategoryName(categoryEnum, pageNumber, pageSize);

        if (dishesList == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve dishes. Please try again later!");
        } else if (dishesList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No dishes found!");
        } else {
            return ResponseEntity
                    .ok(dishesList);
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
        Page<Dish> dishesList = dishService.getAllWithSearching(searchType, searchText, pageNumber, pageSize);

        // Throw exception if the list is empty (case 1), is inaccessible (case 2)
        if (dishesList == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve dishes. Please try again later!");
        } else if (dishesList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No dishes found!");
        } else {
            return ResponseEntity
                    .ok(dishesList);
        }
    }

    // [POST] Create A Dish
    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity<?> createNewItem(
            @Valid @ModelAttribute DishDTO dishDTO,
            @RequestPart("image") MultipartFile image,
            BindingResult bindingResult
    ) throws IOException {

        if (bindingResult.hasErrors()) { // Check for validation errors
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
//            System.out.println("ERRORS :" + errors.toString());
            throw new ValidationException(ExceptionConstant.VALIDATION_ERROR, errors);
        }

        Dish createdDish = dishService.createDish(dishDTO, image);
        return new ResponseEntity<>(createdDish, HttpStatus.CREATED);
    }

    // [GET] Detail Of A Dish
    @GetMapping("/{id}")
    public ResponseEntity<?> detailOfItem(@PathVariable Integer id) {
        Dish dish = dishService.getDishById(id);
        if (dish != null) {
            return ResponseEntity
                    .ok(dish);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Dish with ID: " + id + " not found!");
        }
    }

    // [PUT] Soft Delete A Dish
    @PutMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteItem(@PathVariable int id) {
        Dish dish = dishService.getDishById(id);

        if (dish != null) {
            Dish softDeletedDish = dishService.softDeleteDish(id);
            return ResponseEntity
                    .ok(softDeletedDish);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Dish with ID: " + id + " not found!");
        }
    }

    // [PUT] Update Dish
    @PutMapping("/update/{dishId}")
    public ResponseEntity<?> updateItem(
            @PathVariable int dishId,
            @ModelAttribute DishDTO dishDTO,
            @RequestParam(value = "image", required = false) MultipartFile imageFile
    ) {
        Dish updatedDish = dishService.updateDish(dishId, dishDTO, imageFile);
        if (updatedDish != null) {
            return ResponseEntity.ok(updatedDish);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Dish with ID: " + dishId + " not found!");
        }
    }

    // [GET] Trash List
    @RequestMapping(value = "/trash", method = RequestMethod.GET)
    public ResponseEntity<?> trashList(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "2") int pageSize
    ) {
        Page<Dish> trashList = dishService.getAllDeletedDishes(pageNumber, pageSize);

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

    // [PUT] Restore From Trash
    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable int id) {
        Dish dish = dishService.getDishById(id);

        if (dish != null) {
            Dish restoreDish = dishService.restoreDish(id);
            return ResponseEntity
                    .ok(restoreDish);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Dish with ID: " + id + " not found!");
        }
    }

    // [DELETE] Permanently Delete
    @DeleteMapping("/permanently-delete/{id}")
    public ResponseEntity<?> permanentlyDelete(@PathVariable int id) {
        try {
            dishService.deleteDishById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Dish with ID: " + id + " not found!");
        }
    }
}
