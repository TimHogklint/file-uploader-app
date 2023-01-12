/*
 * FileDTO is used instead of sending back the File object. This more
 * appropriate data to display on frontend than the File itself.
 */

package com.example.fileuploaderapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class FileDTO {

    private String id;
    private byte[] data;
    private Date date;
    private String username;
    private String filename;
    private String filetype;
}
