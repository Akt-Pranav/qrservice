package com.pksa.qrservice.service;

import com.pksa.qrservice.exception.QRException;
import com.pksa.qrservice.util.QRGenerator;
import org.springframework.stereotype.Service;

@Service
public class QRService {

    public byte[] generateQRCode(String data) {
        if (data == null || data.isBlank()) {
            throw new QRException("Input data cannot be empty");
        }

        try {
            return QRGenerator.createQRImage(data, 300, 300);
        } catch (Exception e) {
            throw new QRException("Failed to generate QR code: " + e.getMessage());
        }
    }
}
