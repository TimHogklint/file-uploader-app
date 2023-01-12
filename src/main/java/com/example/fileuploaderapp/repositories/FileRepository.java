/*
 * File repository : spring-boot interface connection to database.
 * Creates easy way to query content.
 */
package com.example.fileuploaderapp.repositories;

import com.example.fileuploaderapp.models.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File,String> {

    /**
     * Find a files by its relation to User id.
     * @param userId files associated with this users' id.
     * @return returns a list of all files by this user.
     */
    List<File> findByUserUserId (String userId);

    /**
     * Delete a file associated with "this" filename , important note - it's been
     * matched with a user by this time. So a user cant delete some other users
     * files.
     * @param fileId delete file with this UUID.
     */
    void deleteFileById(String fileId);
}
