package com.blynder.blynder.controller;

import com.blynder.blynder.dto.UserDTO;
import com.blynder.blynder.model.FollowInfo;
import com.blynder.blynder.model.Stream;
import com.blynder.blynder.model.User;
import com.blynder.blynder.repository.FollowInfoRepository;
import com.blynder.blynder.repository.UserRepository;
import com.blynder.blynder.service.ChatModeratingService;
import com.blynder.blynder.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:4200")
public class UserController {
    private final UserRepository repository;
    private final ChatModeratingService chatModeratingService;
    private final FollowInfoRepository followInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final SessionRegistry sessionRegistry; //Todo przydal by sie refaktor do nowego serwisu dla operacji admina


    public UserController(UserRepository repository, ChatModeratingService chatModeratingService,
                          FollowInfoRepository followInfoRepository, PasswordEncoder passwordEncoder,
                          StorageService storageService, SessionRegistry sessionRegistry) {
        this.repository = repository;
        this.chatModeratingService = chatModeratingService;
        this.followInfoRepository = followInfoRepository;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return repository.findAll();
    }


    @GetMapping("/users/{username}")
    UserDTO getUserByUsername(@PathVariable String username) {
        User user = repository.findByUsername(username).orElseThrow();
        UserDTO userDTO = UserDTO.convertToUserDTO(user);
        Optional<User> userLogged =
                repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        userLogged.ifPresent(loggedUser -> {
            userDTO.setFollowed(followInfoRepository.findByFollowingUserAndUserBeingFollowed(loggedUser, user).isPresent());
            userDTO.setModerator(user.getStream().getModerators().contains(loggedUser) || user.getId() == loggedUser.getId());
        });
        return userDTO;
    }

    @GetMapping("/users/{username}/info/{userToCheck}")
    UserDTO getUserInfoByUsername(@PathVariable String username, @PathVariable String userToCheck) {
        User user = repository.findByUsername(username).orElseThrow();
        User userToGetInfo = repository.findByUsername(userToCheck).orElseThrow();
        UserDTO userDTO = UserDTO.convertToUserDTO(userToGetInfo);
        userDTO.setModerator(user.getStream().getModerators().contains(userToGetInfo) || user.getId() == userToGetInfo.getId());
        userDTO.setTimeouted(chatModeratingService.isTimeouted(userToCheck, user.getUsername()));
        return userDTO;

    }


    @PreAuthorize(value = "hasRole('ROLE_ADMIN')" + "or authentication.getName().equals(#username)")
    @PutMapping("/users/{username}")
    UserDTO editUser(@PathVariable String username, @RequestBody UserDTO edited) {
        return repository.findByUsername(username).map(user -> {
            user.getStream().setStreamTitle(edited.getStreamTitle());
            repository.save(user);
            return user;
        }).map(UserDTO::convertToUserDTO).orElseThrow();
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/users/{username}/ban")
    void banUser(@PathVariable String username, @RequestParam @Min(value = 0) int time) {
        User userToBan = repository.findByUsername(username).orElseThrow();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, (cal.get(Calendar.SECOND) + time));

        userToBan.setBanned(true);
        userToBan.setBannedUntil(cal.getTime());
        repository.save(userToBan);

        List<org.springframework.security.core.userdetails.User> authList =
                sessionRegistry.getAllPrincipals().stream().filter(o -> o instanceof org.springframework.security.core.userdetails.User).map(o -> (org.springframework.security.core.userdetails.User) o).filter(user -> user.getUsername().equals(userToBan.getUsername())).collect(Collectors.toList());
        authList.forEach(user -> {
            List<SessionInformation> infos = sessionRegistry.getAllSessions(user, false);
            for (SessionInformation info : infos) {
                info.expireNow();
//                sessionRegistry.removeSessionInformation(info.getSessionId());
            }
        });
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("/users/{username}/unban")
    void unbanUser(@PathVariable String username) {
        User userToUnban = repository.findByUsername(username).orElseThrow();
        if (userToUnban.isBanned()) {
            userToUnban.setBanned(false);
            userToUnban.setBannedUntil(null);
            repository.save(userToUnban);
        }
    }


    @PreAuthorize(value = "hasRole('ROLE_ADMIN')" + "or authentication.getName().equals(#username)")
    @PostMapping("/users/{username}/avatar")
    public void uploadAvatar(@PathVariable String username, @RequestPart("file") MultipartFile file) {
        User userToAddAvatar = repository.findByUsername(username).orElseThrow();
        String extension = storageService.getExtension(file);
        storageService.store(file, username);
        userToAddAvatar.setAvatarPath(storageService.getDownloadUrl(username + extension));
        repository.save(userToAddAvatar);
    }

    @GetMapping("/users/avatar/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveAvatar(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

//    @DeleteMapping("/files/images/{imageId}")
//    public void deleteImage(@PathVariable int imageId) throws NotFoundException, IOException {
//        ImageData imageData = imageDataRepository.findById(imageId).orElseThrow(() -> new NotFoundException(imageId));
//        try{
//            storageService.delete(imageData.getImageUrl().substring(imageData.getImageUrl().lastIndexOf('/')+1));
//
//        }
//        catch (NoSuchFileException e){
//            e.printStackTrace();
//
//        }
//        imageDataRepository.deleteById(imageId);
//    } //TODO usuwanie awatara, pozatym moze sie dodac np user.jpg, user.png itd, ten sam user ale inne
//     rozszerzenie// w dodatku odrzucaj nie-obrazki (appmangaer)


    @PreAuthorize(value = "hasRole('ROLE_ADMIN')" //TODO pododawaj te preautoryzacje gdie trzeba
            + "or authentication.getName().equals(#username)")
    @PutMapping("/users/{username}/password")
    void changePasswordIfOldPasswordIsKnown(@PathVariable String username, @RequestBody Map<String, Object> passwords) {
        User user = repository.findByUsername(username).orElseThrow();
        if (passwordEncoder.matches(passwords.get("old_password").toString(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwords.get("new_password").toString()));
            repository.save(user);
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password!");

    }


    @PreAuthorize(value = "hasRole('ROLE_ADMIN')" //TODO pododawaj te preautoryzacje gdie trzeba
            + "or authentication.getName().equals(#username) ")
    @DeleteMapping("/users/{username}")
    @Transactional
    public void deleteUserByUsername(@PathVariable String username) { //public jest potrzebny
        if (!repository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exists");
        }
        repository.deleteByUsername(username);

    }

    @GetMapping("/users/{username}/followers")
    List<UserDTO> getFollowersFromUser(@PathVariable String username) {
        List<FollowInfo> followInfos = repository.findByUsername(username).map(User::getFollowers).orElseThrow();
        return followInfos.stream().map(FollowInfo::getFollowingUser).map(UserDTO::convertToUserDTO).collect(Collectors.toList());
    }

    @GetMapping("/users/{username}/followers/count")
    int getFollowersCount(@PathVariable String username) {
        List<FollowInfo> followInfos = repository.findByUsername(username).map(User::getFollowers).orElseThrow();
        return (int) followInfos.stream().map(FollowInfo::getFollowingUser).count();
    }

    @GetMapping("/users/{username}/following")
    List<UserDTO> getFollowingFromUser(@PathVariable String username) {
        List<FollowInfo> followInfos = repository.findByUsername(username).map(User::getFollowing).orElseThrow();
        return followInfos.stream().map(FollowInfo::getUserBeingFollowed).map(UserDTO::convertToUserDTO).collect(Collectors.toList());
    }


    @PostMapping("/users/register")
    UserDTO newUser(@RequestBody User newUser) {
        if (repository.findByUsername(newUser.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        Stream stream = new Stream();
        stream.setStreamSecretKey(UUID.randomUUID().toString());
        stream.setStreamTitle(newUser.getUsername());
        stream.setUser(newUser);
        newUser.setStream(stream);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return UserDTO.convertToUserDTO(repository.save(newUser));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/users/{username}/follow")
        //TODO pilnuj zeby samego siebie nie sfollowowwac
    void followUser(@PathVariable String username) {
        User follower =
                repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        if (username.equals(follower.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong username");
        User userToFollow = repository.findByUsername(username).orElseThrow();

        followInfoRepository.findByFollowingUserAndUserBeingFollowed(follower, userToFollow).ifPresent(followInfo -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already followed");
        });
        FollowInfo newFollow = new FollowInfo();
        newFollow.setUserBeingFollowed(userToFollow);
        newFollow.setFollowingUser(follower);
        followInfoRepository.save(newFollow);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/users/{username}/unfollow")
        //TODO sprawdzic czy wgl jest followowany
        //TODO nie mozna followowac samego siebie przeca
    void unfollowUser(@PathVariable String username) {
        User follower =
                repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow();
        if (username.equals(follower.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong username");
        User userToUnfollow = repository.findByUsername(username).orElseThrow();

        FollowInfo followInfo = followInfoRepository.findByFollowingUserAndUserBeingFollowed(follower,
                userToUnfollow).orElseThrow();
        followInfoRepository.delete(followInfo);
    }

}
