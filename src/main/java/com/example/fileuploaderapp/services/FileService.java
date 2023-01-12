/*
 * Used by FileController to speak with FileRepository
 */
package com.example.fileuploaderapp.services;

import com.example.fileuploaderapp.models.File;
import com.example.fileuploaderapp.models.User;
import com.example.fileuploaderapp.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    @Autowired
    public FileService(
            FileRepository fileRepository,
            UserService userService)
    {
        this.fileRepository = fileRepository;
    }

    /**
     * This service method requests the repository to save the file in the database.
     * @param user associated file with a user.
     * @param data save the date.
     * @param filename name of the file.
     * @param filetype type of media.
     * @return the file we just saved as sign it was successful.
     */
    public File sendFile(User user , byte[] data, String filename, String filetype){
        var file = new File(data,filename,filetype,user);
        return fileRepository.save(file);
    }

    /**
     * Used by both route "files" and "files/filename" to get files.
     * @param userId files associated with this user id.
     * @return a collection of files this user have uploaded.
     */
    public List<File> getFiles(String userId)
    {
       return fileRepository.findByUserUserId(userId);
    }

    /**
     * Used to delete a file - It's important to note here that the file have
     * already been associated with a file - so a user cant delete another
     * users files.
     * @param fileId the file id to delete.
     */
    public void deleteFile(String fileId){ fileRepository.deleteFileById(fileId);}
}
