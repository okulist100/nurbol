package kg.mega.university.controller;

import kg.mega.university.domain.Message;
import kg.mega.university.domain.User;
import kg.mega.university.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {
    private final MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public MainController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @GetMapping("/")
    public String greeting(Model model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = messageRepo.findAll();

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("uploads", uploadPath);

        return "main";
    }


    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "00") String text,
            @RequestParam String tag, Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Message message = new Message(text, tag, user);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }

        messageRepo.save(message);

        Iterable<Message> messages = messageRepo.findAll();

        model.addAttribute("messages", messages);

        return "main";
    }
    @RequestMapping("/main/{fileName}")
    @ResponseBody
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {

        if (fileName.contains(".doc")) response.setContentType("application/msword");
        if (fileName.contains(".docx")) response.setContentType("application/msword");
        if (fileName.contains(".xls")) response.setContentType("application/vnd.ms-excel");
        if (fileName.contains(".csv")) response.setContentType("application/vnd.ms-excel");
        if (fileName.contains(".ppt")) response.setContentType("application/ppt");
        if (fileName.contains(".pdf")) response.setContentType("application/pdf");
        if (fileName.contains(".zip")) response.setContentType("application/zip");
        if (fileName.contains(".txt")) response.setContentType("application/txt");
        response.setHeader("Content-Disposition", "attachment; filename=" +fileName);
        response.setHeader("Content-Transfer-Encoding", "binary");
        try {
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(uploadPath+'/'+fileName);
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf)) > 0) {
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();
        }
        catch(IOException e) {
            e.printStackTrace();

        }
    }
    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ) {
        Set<Message> messages = user.getMessages();

        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));

        return "userMessages";
    }
    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

                message.setText(text);
                message.setTag(tag);
                saveFile(message, file);

            messageRepo.save(message);

        return "redirect:/user-messages/" + user;
    }
    private void saveFile(Message message, @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            message.setFilename(resultFilename);
        }
    }
    @GetMapping("/del-user-messages/{user}")


    public String deleteMessage(
            @PathVariable Long user,
            @RequestParam("message") Long messageId
    ) throws IOException {

        messageRepo.deleteById(messageId);

        return "redirect:/user-messages/" + user;
    }

}
