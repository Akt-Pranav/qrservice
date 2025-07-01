package com.pksa.qrservice.controller;



import com.pksa.qrservice.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/qr")
public class QrController {

    @Autowired
    private QRService qrService;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateQRCode(@RequestParam String data) {
        byte[] image = qrService.generateQRCode(data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add("X-Message", "QR generated successfully");

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
