package com.appsdeveloperblog.photoapp.api.users.service;

import com.appsdeveloperblog.photoapp.api.users.data.AlbumServiceClient;
import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.data.UserRepository;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;
import com.netflix.discovery.converters.Auto;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    ////////////////////////////////////
    //      DEPENDENCY INJECTION      //
    ////////////////////////////////////
    @Autowired
    private UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
/*    @Autowired
    RestTemplate restTemplate;*/
    @Autowired
    Environment env;
    @Autowired
    AlbumServiceClient albumServiceClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    ////////////////////////////////////
    //      IMPLEMENT METHOD          //
    ////////////////////////////////////
    @Override
    public UserDto createUser(UserDto userDetails) {

        // UUID.randomUUID() create random user id
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        // create model mapper
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // map the UserDto to UserEntity
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        // save
        userRepository.save(userEntity);
        // return the saved UserEntity
        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);


        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

/*        String albumsUrl = String.format(env.getProperty("albums.url"), userId);
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AlbumResponseModel>>() {});*/
        logger.info("Before calling albums Microservice");
        List<AlbumResponseModel> albumsList = albumServiceClient.getAlbums(userId);
        logger.info("After calling albums Microservice");
        userDto.setAlbums(albumsList);

        return userDto;
    }
}
