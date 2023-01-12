/*
 * This is our file - which is what we are saving in the database.
 */

package com.example.fileuploaderapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class File {

    @Id
    private String id;

    @Lob
    private byte[] data;
    private String filename;
    private String filetype;

    @ManyToOne
    private User user;

    private Date date;

    public File (byte[] data,String filename,String filetype,User user){
        this.id = UUID.randomUUID().toString();
        this.data = data;
        this.filename = filename;
        this.filetype = filetype;
        this.user = user;
        this.date = new Date();
    }
}
