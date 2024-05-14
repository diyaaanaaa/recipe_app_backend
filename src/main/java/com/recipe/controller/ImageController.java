package com.recipe.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ApplicationContext ctx;

    @Value("${img.dir}")
    private String imgMainDir;


    @GetMapping(value = {"/{image}"}, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public ResponseEntity<Resource> getImage(@PathVariable(value = "folder", required = false) String folder,
                                             @PathVariable("image") String image) {


        String s =  "file:"+imgMainDir;
        if (folder != null){
            s+=folder+File.separator+image.trim();
        }
        else{
            s+= image.trim();


            System.out.println(s);
        }
        Resource resource = ctx.getResource(s);
        return ResponseEntity.ok(resource);
    }


}
