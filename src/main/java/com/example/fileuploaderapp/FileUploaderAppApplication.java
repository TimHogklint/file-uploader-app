/**
 * Spring-boot course, PLUJUH21 Teknikhägskolan Lund. 2022
 *
 * @author: Tim Högklint
 * @email : tim.hogklint@gmail.com
 */
package com.example.fileuploaderapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 *
 * Tasks
 * ------------------------------------------------------------------------------------------------------
 *
 * - Registering and authorization.
 *   The application allows users to create a user on an allowed route. I used your example on how to
 *   include the JWT filter chain to authorize requests and store user logon token.
 *
 * - User actions upload, download and delete
 *   Reference used , besides chat-app-example.
 *   https://www.devglan.com/spring-boot/spring-boot-file-upload-download
 *
 *   - Upload
 *     Heavily reference chat example you posted in conjunction with
 *     internet tutorials. My choice to use Multipart file were just
 *     due to the fact that it felt most natural way of exchanging
 *     chat examples "messages"
 *
 *   - Download
 *     This is where the devglan article really helped out as it had
 *     postman instructions swell. Downloading file was the last thing
 *     I did in this process, and it was very satisfying seeing my stupid
 *     picture being displayed in postman as GET request.
 *
 *     Reason I went with multipart file was that it had ability save different type
 *     of files. There were other options like HttpServletRequest ? but it seems like
 *     it might not work when you have filters processing your requests.
 *
 *     I chose multipart file because it was very approachable - however I have seen
 *     people being concerned it might eventually store things in memory if a file is
 *     very big ?
 *
 *     Reference : https://stackoverflow.com/questions/39748536/spring-upload-non-multipart-file-as-a-stream
 *     I feel somewhat perplexed; All in all - I thought about it at least.
 *
 *   - Delete
 *     Probably the most naive way of doing it but hey I tried ! I speak more
 *     about why below in user file management task / requirement.
 *
 * - User file management / access.
 *   Meanwhile, users cant access other users files - I feel when I delete a file
 *   it's a raw call to FileRepository; that call is not associate with any user.
 *   "delete file with this UUID". Its unlikely that a user could delete another users file
 *   due to me checking if a user "owns" a file with that UUID first.
 *
 *   I haven't been able to target other users files at least.
 *   Same goes with viewing - users can only "find" their own files.
 *
 * - Saving users/files to database
 *   I choose PostgreSQL - meanwhile I only *really* worked with mongodb before I consider
 *   this to have been a very valuable experience , and I will probably stick
 *   with PostgreSQL in the future.
 *
 * - Application must be stateless.
 *   Since I built the application as the course progressed I too had the hashmap token solution.
 *   I solved it by using JWT to house the user logon in the encryption. Application don't have
 *   any dependency on variables stored in memory.
 *
 * ------------------------------------------------------------------------------------------------------
 *
 * Additional
 * - Postman requests
 *
 * All access route.
 * http://localhost:8080/register - username , password
 *
 * Requires a body with username, password
 * http://localhost:8080/login - jwt authorization token
 *
 * Requires "Authorization" field in postman headers
 * http://localhost:8080/send - upload file via form-data
 * http://localhost:8080/files - gets user associated with jwt token and returns all uploaded files by that user.
 * http://localhost:8080/files/secretfile.jpeg - gets file IF user has uploaded a file with that name.
 *
 * delete file UUID , internally I make sure a user has a file with that UUID, so you cant delete other users files.
 * http://localhost:8080/files/delete/36ca3c17-9104-454a-9e43-931434d855a8
 *
 * Admissions
 * - Checking input , numbers in names , blank input.
 * - Could have spent more time writing "No such file" and error message in the like if input or request was bad.
 *
 * ------------------------------------------------------------------------------------------------------
 *
 * Personal
 *
 * The school planning has been in perpetual state of - "we will start doing more java"; never really got there.
 * Then they changed the name of the course to fullstack-developer from java-developer.
 * Probably for the best !
 *
 * No fault on your part;
 *
 * But this was like getting dunked in ice-cold water.
 * This might be entirely "me" problem though, it's a very self-managed course after all.
 *
 *
 * Tim.
 *
 * ------------------------------------------------------------------------------------------------------
 */

@SpringBootApplication
public class FileUploaderAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileUploaderAppApplication.class, args);
    }
}
