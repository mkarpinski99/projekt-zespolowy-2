package com.blynder.blynder.controller;

import com.blynder.blynder.model.User;
import com.blynder.blynder.repository.UserRepository;
import com.blynder.blynder.service.ChatModeratingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@Validated
public class ChatController {

    public final ChatModeratingService moderatingService;
    public final UserRepository userRepository;

    public ChatController(ChatModeratingService moderatingService, UserRepository userRepository) {
        this.moderatingService = moderatingService;
        this.userRepository = userRepository;
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or isAuthenticated()")
    @PostMapping("/chat/{streamerUsername}/timeout")
    void timeoutUser(@RequestParam("user") String username, @RequestParam("time") @Min(value = 0) int time,
                     @PathVariable String streamerUsername) // zwaliduj input todo i samego siebie tez nie timeoutowac
    {
        User userLogged =
                userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        User streamer = userRepository.findByUsername(streamerUsername).orElseThrow();
        if (streamer.getStream().getModerators().contains(userLogged) || userLogged.getAuthorities().equals(
                "ROLE_ADMIN") || userLogged.getId() == streamer.getId()) {
            moderatingService.timeout(username, time, streamerUsername);
        }
    }

    @PostMapping("/chat/{streamerUsername}/mod")
        //todo zabezpieczyc
    void addModeratorToStream(@RequestParam("user") String username, @PathVariable String streamerUsername) {
        User streamer = userRepository.findByUsername(streamerUsername).orElseThrow();
        User userToAddToMods = userRepository.findByUsername(username).orElseThrow();
        streamer.getStream().getModerators().add(userToAddToMods);
        userToAddToMods.getModeratorOn().add(streamer.getStream());
        userRepository.save(streamer);
        userRepository.save(userToAddToMods);
    }

    @PostMapping("/chat/{streamerUsername}/unmod")
        //todo zabezpieczyc
    void removeModeratorFromStream(@RequestParam("user") String username, @PathVariable String streamerUsername) {
        User streamer = userRepository.findByUsername(streamerUsername).orElseThrow();
        User userToRemoveFromMods = userRepository.findByUsername(username).orElseThrow();
        streamer.getStream().getModerators().remove(userToRemoveFromMods);
        userToRemoveFromMods.getModeratorOn().remove(streamer.getStream());
        userRepository.save(streamer);
        userRepository.save(userToRemoveFromMods);
    }


}
