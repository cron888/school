package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class AvatarService {

    private static final Logger log = LoggerFactory.getLogger(AvatarService.class);
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public Avatar create(Avatar avatar) {
        log.info("Вызван метод create");
        return avatarRepository.save(avatar);
    }

    public Avatar get(Long id) {
        log.info("Вызван метод get");
        return avatarRepository.findById(id).orElse(null);
    }

    public Avatar getByStudentId(Long studentId) {
        log.info("Вызван метод getByStudentId");
        return avatarRepository.findByStudentId(studentId).orElse(null);
    }

    public boolean delete(Long id) {
        log.info("Вызван метод delete");
        if (!avatarRepository.existsById(id)) {
            return false;
        }
        avatarRepository.deleteById(id);
        return true;
    }

    public Page<Avatar> getAllAvatars(Pageable pageable) {
        log.info("Вызван метод getAllAvatars");
        return avatarRepository.findAll(pageable);
    }

    public Avatar saveAvatar(Long studentId, MultipartFile file) throws IOException {
        log.info("Вызван метод saveAvatar");
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return null;
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get("uploads");
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        return avatarRepository.save(avatar);
    }

    public byte[] getAvatarData(Long id) {
        log.info("Вызван метод getAvatarData");
        Avatar avatar = avatarRepository.findById(id).orElse(null);
        if (avatar == null) {
            return null;
        }
        return avatar.getData();
    }

    public byte[] getAvatarFromDisk(String filePath) {
        log.info("Вызван метод getAvatarFromDisk");
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            return null;
        }
    }
}
