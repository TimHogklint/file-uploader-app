package com.example.fileuploaderapp.controllers;

import com.example.fileuploaderapp.dtos.FileDTO;
import com.example.fileuploaderapp.services.FileService;
import com.example.fileuploaderapp.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Getter
@Setter
public class FileController {

    private final FileService fileService;

    private final UserService userService;

    @Autowired
    public FileController(FileService fileService,UserService userService){
        this.fileService = fileService;
        this.userService = userService;
    }

    /**
     * Upload a single file while relating it with a user.
     * @param user is used to relate file with user.
     * @param sendFile represents the content of 'multipart' file - byte[] array.
     * @return - will return a simplified file model.
     * @throws IOException upload file failed.
     */
    @PostMapping("/send")
    public ResponseEntity<FileDTO> sendFile(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("file") MultipartFile sendFile
    ) throws IOException {

        var currUser = userService.loadUserByUsername(user.getUsername());
        if(currUser == null)
            return ResponseEntity.notFound().build();

        var file = fileService.sendFile(currUser,sendFile.getBytes(),sendFile.getOriginalFilename(), sendFile.getContentType());

        var dto = new FileDTO(
                file.getId(),
                file.getData(),
                file.getDate(),
                file.getUser().getUsername(), // uploaded by
                file.getFilename(),
                file.getFiletype()
                );

        return ResponseEntity.ok(dto);
    }

    @Getter
    @Setter
    public static class SendFile
    {
        private byte[] data;
    }

    /**
     * This route gets all files associated with a single user.
     * @param user is used to relate file with user.
     * @return returns simplified version of file without user password.
     */
    @GetMapping("/files")
    public ResponseEntity<List<FileDTO>> getFiles(
            @AuthenticationPrincipal UserDetails user
    ){

        var currUser = userService.loadUserByUsername(user.getUsername());
        if(currUser == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( fileService
                .getFiles(currUser.getUserId())
                .stream()
                .map(file -> {
                    return new FileDTO(
                            file.getId(),
                            file.getData(),
                            file.getDate(),
                            file.getUser().getUsername(), // uploaded by
                            file.getFilename(),
                            file.getFiletype()
                    );
                })
                .collect(Collectors.toList()));
    }

    /**
     * Gets a single file from the list of files that user uploaded.
     * @param user is used to relate file with user.
     * @param fileName identifier to look for among users files. This requires the full name : example "clouds.jpg"
     * @return
     */
    @GetMapping("/files/{fileName}")
    public ResponseEntity getFile(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String fileName
    ){

        var currUser = userService.loadUserByUsername(user.getUsername());
        if(currUser == null)
            return ResponseEntity.notFound().build();

        // Security wise - I guess it's always best to go through files by user.
        // and not send a naive request for just file id.
        List<FileDTO> files =  fileService
                .getFiles(currUser.getUserId())
                .stream()
                .map(file -> {
                    return new FileDTO(
                            file.getId(),
                            file.getData(),
                            file.getDate(),
                            file.getUser().getUsername(), // uploaded by
                            file.getFilename(),
                            file.getFiletype()
                    );
                })
                .collect(Collectors.toList());

        // find the file with filename from that users file collection.
        for (FileDTO file : files)
        {
            //System.out.println(file.getFilename());

            if (file.getFilename().equals(fileName))
            {
                //System.out.println("Found file with name " + fileName + " belonging to this user.");
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(file.getFiletype()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                        .body(file.getData());
            }
        }

        // if we get to there - we did not find the file by this user.
        return ResponseEntity.notFound().build();
    }

    /**
     * First get all files by this user , then look for the correct UUID.
     * @param user is used to relate file with user.
     * @param id identifier to look for among users files.
     * @return
     */
    @GetMapping("/files/delete/{id}")
    public ResponseEntity deleteFile(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String id
    )
    {

        //System.out.println("Deleting single file. - " + id);

        var currUser = userService.loadUserByUsername(user.getUsername());
        if(currUser == null)
            return ResponseEntity.notFound().build();

        // Security wise - I guess it's always best to go through files by user.
        // and not send a naive request for just file id.
        List<FileDTO> files =  fileService
                .getFiles(currUser.getUserId())
                .stream()
                .map(file -> {
                    return new FileDTO(
                            file.getId(),
                            file.getData(),
                            file.getDate(),
                            file.getUser().getUsername(), // uploaded by
                            file.getFilename(),
                            file.getFiletype()
                    );
                })
                .collect(Collectors.toList());

        // find the file with id from that output.
        for (FileDTO file : files)
        {
            //System.out.println(file.getFilename());

            if (file.getId().equals(id))
            {
                //System.out.println("Found file " + file.getFilename() + " belonging to this user and same UUID in request");
               fileService.deleteFile(file.getId());
               return ResponseEntity.ok().build();
            }
        }

        // if we get to there - we did not find the file by this user.
        //System.out.println("File not found");
        return ResponseEntity.notFound().build();
    }
}
