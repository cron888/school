package ru.hogwarts.school.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload/{studentId}")
    public Optional<Avatar> upload(@PathVariable Long studentId,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        Avatar avatar = avatarService.saveAvatar(studentId, file);
        return Optional.ofNullable(avatar);
    }

    @GetMapping(value = "/{id}/db", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getFromDb(@PathVariable Long id) {
        return avatarService.getAvatarData(id);
    }

    @GetMapping("/disk/{filePath}")
    public byte[] getFromDisk(@PathVariable String filePath) {
        return avatarService.getAvatarFromDisk(filePath);
    }

    @GetMapping("/all")
    public Page<Avatar> getAllAvatars(Pageable pageable) {
        return avatarService.getAllAvatars(pageable);
    }
}
