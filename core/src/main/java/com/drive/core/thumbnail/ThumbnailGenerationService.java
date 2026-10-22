package com.drive.core.thumbnail;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;

import javax.imageio.ImageIO;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.AssetManager;

@Component(service = ThumbnailGenerationService.class)
public class ThumbnailGenerationService {

    private static final Logger LOG =
            LoggerFactory.getLogger(ThumbnailGenerationService.class);

    private static final int MAX_WIDTH = 320;
    private static final int MAX_HEIGHT = 320;
    private static final String RENDITION_NAME = "drive-thumbnail.png";

    @Reference
    private ResourceResolverFactory resolverFactory;

    public boolean generateThumbnail(String assetPath) {


        try (ResourceResolver resolver =
                     resolverFactory.getServiceResourceResolver(
                             Collections.singletonMap(
                                     ResourceResolverFactory.SUBSERVICE,
                                     "thumbnail-writer"))) {

            Resource resource = resolver.getResource(assetPath);

            if (resource == null) {
                LOG.error("Asset resource not found: {}", assetPath);
                return false;
            }

            Asset asset = resource.adaptTo(Asset.class);

            if (asset == null) {
                LOG.error("Resource is not an AEM asset: {}", assetPath);
                return false;
            }


            try (InputStream originalStream =
                         asset.getOriginal().getStream()) {

                BufferedImage original =
                        ImageIO.read(originalStream);

                if (original == null) {
                    LOG.error("Could not read image: {}", assetPath);
                    return false;
                }

                int originalWidth = original.getWidth();
                int originalHeight = original.getHeight();


                double scale = Math.min(
                        (double) MAX_WIDTH / originalWidth,
                        (double) MAX_HEIGHT / originalHeight);

                scale = Math.min(scale, 1.0);

                int width = Math.max(
                        1, (int) Math.round(originalWidth * scale));

                int height = Math.max(
                        1, (int) Math.round(originalHeight * scale));

                BufferedImage thumbnail = new BufferedImage(
                        width, height, BufferedImage.TYPE_INT_RGB);

                Graphics2D graphics = thumbnail.createGraphics();

                try {
                    graphics.setRenderingHint(
                            RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    graphics.setRenderingHint(
                            RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);

                    graphics.setRenderingHint(
                            RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

                    graphics.drawImage(
                            original, 0, 0, width, height, null);
                } finally {
                    graphics.dispose();
                }

                ByteArrayOutputStream output =
                        new ByteArrayOutputStream();

                if (!ImageIO.write(thumbnail, "png", output)) {
                    LOG.error("PNG encoder is unavailable");
                    return false;
                }

                AssetManager assetManager =
                        resolver.adaptTo(AssetManager.class);

                if (assetManager == null) {
                    LOG.error("AssetManager is unavailable");
                    return false;
                }

                try (InputStream thumbnailStream =
                             new ByteArrayInputStream(output.toByteArray())) {

                    asset.addRendition(
                            RENDITION_NAME,
                            thumbnailStream,
                            "image/png");
                }

                LOG.info(
                        "Saved rendition {} for {} ({}x{})",
                        RENDITION_NAME, assetPath, width, height);

                return true;
            }

        } catch (Exception e) {
            LOG.error(
                    "Error generating thumbnail for " + assetPath, e);
            return false;
        }
    }
}
