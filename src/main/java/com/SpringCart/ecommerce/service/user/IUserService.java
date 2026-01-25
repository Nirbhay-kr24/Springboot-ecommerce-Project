package com.SpringCart.ecommerce.service.user;

import com.SpringCart.ecommerce.dto.UserDto;
import com.SpringCart.ecommerce.model.User;
import com.SpringCart.ecommerce.request.CreateUserRequest;
import com.SpringCart.ecommerce.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
