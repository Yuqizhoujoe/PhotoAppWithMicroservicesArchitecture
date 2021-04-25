package com.appsdeveloperblog.photoapp.api.users.ui.controllers;

import com.appsdeveloperblog.photoapp.api.users.service.UserService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserRequestModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.CreateUserResponseModel;
import com.appsdeveloperblog.photoapp.api.users.ui.model.UserResponseModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private Environment env;

    @Autowired
    private UserService userService;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + env.getProperty("local.server.port") + ", with token = "
                + env.getProperty("token.secret");
    }

    @PostMapping(
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        // create mapper
        ModelMapper modelMapper = new ModelMapper();
        // set up the modelMapper configuration
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // map the userDetail to userDto
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        // create user instance
        UserDto createdUser = userService.createUser(userDto);
        // map the userDto - createdUser to CreateUserResponseModel
        CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);
        // return the CreateUserResponseModel
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping(value="/{userId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {

        UserDto userDto = userService.getUserByUserId(userId);
        UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

}
