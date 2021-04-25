package com.appsdeveloperblog.photoapp.api.users.shared;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Autowired
    Environment environment;

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                break;
            case 404: {
                if (methodKey.contains("getAlbums")) {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()),
                            environment.getProperty("albums.execptions.albums-not-found"));
                } else {
                    return new ResponseStatusException(HttpStatus.valueOf(response.status()), response.reason());
                }
            }
            default: {
                return new Exception(response.reason());
            }
        }
        return null;
    }

}
