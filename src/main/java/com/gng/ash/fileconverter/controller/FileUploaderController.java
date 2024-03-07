package com.gng.ash.fileconverter.controller;

import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.service.FileConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.wink.json4j.JSONArray;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.gng.ash.fileconverter.Constants.VALIDATION_RESULT;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileUploaderController {
    private final FileConverter fileConverter;
    private final HttpServletRequest request;


    public FileUploaderController(FileConverter fileConverter, HttpServletRequest request) {
        this.fileConverter = fileConverter;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }


    @PostMapping("/upload")
    public ResponseEntity<String> fileUploading(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        ValidationResult validationResult = (ValidationResult) request.getAttribute(VALIDATION_RESULT);
        String remoteAddr = request.getRemoteAddr();
        JSONArray jsonArray = fileConverter.convert(file);
        //byte[] bytes = jsonArray.toString().getBytes(StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .contentLength(jsonArray.toString().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=OutcomeFile.json")
                .body(jsonArray.toString());


    }


    /*@GetMapping(value = "/image")
    public @ResponseBody byte[] getImage() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/com/baeldung/produceimage/image.jpg");
        return IOUtils.toByteArray(in);
    }*/


}
