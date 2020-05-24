package ru.nsu.ccfit.mvcentertainment.communify.backend.controllers;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.nio.file.Files;

@UtilityClass
public class StreamingResponseFactory {

    public ResponseEntity<StreamingResponseBody> getStreamingResponse(File file) {
        StreamingResponseBody responseBody =
                outputStream -> Files.copy(file.toPath(), outputStream);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", file.getName())
                ).contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(responseBody);
    }

}
