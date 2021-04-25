package com.appsdeveloperblog.photoapp.api.users.data;

import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "albums-ws") // feign client will communicate with
public interface AlbumServiceClient {

    @GetMapping("/users/{id}/albums")
    public List<AlbumResponseModel> getAlbums(@PathVariable String id);



}
