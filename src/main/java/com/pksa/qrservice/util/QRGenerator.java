package com.pksa.qrservice.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class QRGenerator {

    public static byte[] createQRImage(String data, int width, int height) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
        BufferedImage qrImageRaw = MatrixToImageWriter.toBufferedImage(matrix);
        BufferedImage qrImage = new BufferedImage(qrImageRaw.getWidth(), qrImageRaw.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Copy raw QR to RGB image
        Graphics2D g = qrImage.createGraphics();
        g.drawImage(qrImageRaw, 0, 0, null);
        g.dispose();

        // Step 1: Try fetching favicon
        String faviconUrl = getFaviconURL(data);
        BufferedImage logo;
        try (InputStream faviconStream = new URL(faviconUrl).openStream()) {
            logo = ImageIO.read(faviconStream);
        } catch (Exception e) {
            System.out.println("Favicon failed: " + e.getMessage());

            // Step 2: Fallback to cloud image
            String fallbackUrl = "https://github.com/Akt-Pranav/QRCode-Generator/blob/main/pksa-logo.png";
            try (InputStream fallbackStream = new URL(fallbackUrl).openStream()) {
                logo = ImageIO.read(fallbackStream);
            } catch (Exception ex) {
                System.out.println("Fallback logo failed too.");
                return toByteArray(qrImage); // Return plain QR
            }
        }

        // Step 3: Resize & overlay logo
        int logoSize = width / 5;
        Image scaledLogo = logo.getScaledInstance(logoSize, logoSize, Image.SCALE_SMOOTH);

        Graphics2D g2d = qrImage.createGraphics();
        int x = (qrImage.getWidth() - logoSize) / 2;
        int y = (qrImage.getHeight() - logoSize) / 2;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     // Create a rounded version of the logo
        BufferedImage roundedLogo = new BufferedImage(logoSize, logoSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D roundG = roundedLogo.createGraphics();
        roundG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Make circular clip
        roundG.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, logoSize, logoSize));
        roundG.drawImage(scaledLogo, 0, 0, logoSize, logoSize, null);
        roundG.dispose();

        // Draw rounded logo in the center of QR
        g2d.drawImage(roundedLogo, x, y, null);
        g2d.dispose();

        return toByteArray(qrImage);
    }

    private static byte[] toByteArray(BufferedImage image) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    private static String getFaviconURL(String data) {
        try {
            String domain = new URL(data).getHost();
            return "https://www.google.com/s2/favicons?sz=64&domain_url=" + domain;
        } catch (Exception e) {
            return "https://www.google.com/s2/favicons?sz=64&domain_url=example.com";
        }
    }
}
