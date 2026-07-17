package com.bookingplatform.controller;

import com.bookingplatform.dto.common.ApiResponse;
import com.bookingplatform.dto.common.PaginationResponse;
import com.bookingplatform.dto.user.UserRequest;
import com.bookingplatform.dto.user.UserResponse;
import com.bookingplatform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
     * ==========================
     * GET USER BY ID
     * ==========================
     */

    @GetMapping("/{id}")
    public ApiResponse<UserResponse>
    getUserById(

            @PathVariable
            Long id

    ) {

        return ApiResponse
                .<UserResponse>builder()

                .success(true)

                .message(
                        "User fetched successfully."
                )

                .data(
                        userService
                                .getUserById(id)
                )

                .build();
    }

    /*
     * ==========================
     * GET ALL USERS
     * ==========================
     */

    @GetMapping
    public ApiResponse
            <PaginationResponse<UserResponse>>

    getAllUsers(

            @RequestParam(
                    defaultValue = "0"
            )

            int page,

            @RequestParam(
                    defaultValue = "10"
            )

            int size

    ) {

        return ApiResponse

                .<PaginationResponse<UserResponse>>
                        builder()

                .success(true)

                .message(
                        "Users fetched successfully."
                )

                .data(

                        userService
                                .getAllUsers(
                                        page,
                                        size
                                )

                )

                .build();
    }

    /*
     * ==========================
     * SEARCH USERS
     * ==========================
     */

    @GetMapping("/search")
    public ApiResponse
            <PaginationResponse<UserResponse>>

    searchUsers(

            @RequestParam
            String keyword,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "10"
            )
            int size

    ) {

        return ApiResponse

                .<PaginationResponse<UserResponse>>
                        builder()

                .success(true)

                .message(
                        "Search completed."
                )

                .data(

                        userService
                                .searchUsers(

                                        keyword,

                                        page,

                                        size

                                )

                )

                .build();
    }

    /*
     * ==========================
     * UPDATE USER
     * ==========================
     */

    @PutMapping("/{id}")
    public ApiResponse<UserResponse>
    updateUser(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            UserRequest request

    ) {

        return ApiResponse
                .<UserResponse>builder()

                .success(true)

                .message(
                        "User updated successfully."
                )

                .data(

                        userService
                                .updateUser(
                                        id,
                                        request
                                )

                )

                .build();
    }

    /*
     * ==========================
     * DELETE USER
     * ==========================
     */

    @DeleteMapping("/{id}")
    public ApiResponse<Void>
    deleteUser(

            @PathVariable
            Long id

    ) {

        userService.deleteUser(
                id
        );

        return ApiResponse
                .<Void>builder()

                .success(true)

                .message(
                        "User deleted successfully."
                )

                .data(null)

                .build();
    }


}