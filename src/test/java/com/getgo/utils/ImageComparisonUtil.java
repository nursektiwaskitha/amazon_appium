package com.getgo.utils;

import nu.pattern.OpenCV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * ImageComparisonUtil provides methods for comparing images
 * Supports 80-90% match threshold for image comparison
 */
public class ImageComparisonUtil {
    
    private static final Logger logger = LogManager.getLogger(ImageComparisonUtil.class);
    private static boolean opencvLoaded = false;
    
    static {
        try {
            OpenCV.loadLocally();
            opencvLoaded = true;
            logger.info("OpenCV loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load OpenCV", e);
        }
    }
    
    /**
     * Compare two images and return similarity percentage
     * @param imagePath1 Path to first image
     * @param imagePath2 Path to second image
     * @return Similarity percentage (0-100)
     */
    public static double compareImages(String imagePath1, String imagePath2) {
        if (!opencvLoaded) {
            logger.error("OpenCV not loaded. Cannot compare images.");
            return 0.0;
        }
        
        try {
            Mat img1 = Imgcodecs.imread(imagePath1);
            Mat img2 = Imgcodecs.imread(imagePath2);
            
            if (img1.empty() || img2.empty()) {
                logger.error("One or both images could not be loaded");
                return 0.0;
            }
            
            // Resize images to same size if different
            if (img1.size().width != img2.size().width || img1.size().height != img2.size().height) {
                Imgproc.resize(img2, img2, img1.size());
            }
            
            // Convert to grayscale
            Mat gray1 = new Mat();
            Mat gray2 = new Mat();
            Imgproc.cvtColor(img1, gray1, Imgproc.COLOR_BGR2GRAY);
            Imgproc.cvtColor(img2, gray2, Imgproc.COLOR_BGR2GRAY);
            
            // Calculate difference
            Mat diff = new Mat();
            Core.absdiff(gray1, gray2, diff);
            
            // Calculate similarity
            double totalPixels = diff.rows() * diff.cols();
            double nonZeroPixels = Core.countNonZero(diff);
            double similarity = ((totalPixels - nonZeroPixels) / totalPixels) * 100;
            
            logger.info(String.format("Image comparison result: %.2f%% similarity", similarity));
            
            return similarity;
            
        } catch (Exception e) {
            logger.error("Error comparing images", e);
            return 0.0;
        }
    }
    
    /**
     * Compare images using histogram comparison (better for different sizes)
     * This method is more robust when comparing thumbnails vs full-size images
     * @param imagePath1 Path to first image
     * @param imagePath2 Path to second image
     * @return Similarity percentage (0-100)
     */
    public static double compareImagesHistogram(String imagePath1, String imagePath2) {
        if (!opencvLoaded) {
            logger.error("OpenCV not loaded. Cannot compare images.");
            return 0.0;
        }
        
        try {
            Mat img1 = Imgcodecs.imread(imagePath1);
            Mat img2 = Imgcodecs.imread(imagePath2);
            
            if (img1.empty() || img2.empty()) {
                logger.error("One or both images could not be loaded");
                return 0.0;
            }
            
            // Convert to HSV color space (better for color comparison)
            Mat hsv1 = new Mat();
            Mat hsv2 = new Mat();
            Imgproc.cvtColor(img1, hsv1, Imgproc.COLOR_BGR2HSV);
            Imgproc.cvtColor(img2, hsv2, Imgproc.COLOR_BGR2HSV);
            
            // Calculate histograms
            Mat hist1 = new Mat();
            Mat hist2 = new Mat();
            
            int hBins = 50, sBins = 60;
            int[] histSize = {hBins, sBins};
            float[] hRanges = {0, 180};
            float[] sRanges = {0, 256};
            float[][] ranges = {hRanges, sRanges};
            int[] channels = {0, 1};
            
            java.util.List<Mat> hsvList1 = java.util.Arrays.asList(hsv1);
            java.util.List<Mat> hsvList2 = java.util.Arrays.asList(hsv2);
            
            Imgproc.calcHist(hsvList1, new org.opencv.core.MatOfInt(channels), 
                new Mat(), hist1, new org.opencv.core.MatOfInt(histSize), 
                new org.opencv.core.MatOfFloat(hRanges[0], hRanges[1], sRanges[0], sRanges[1]));
            Core.normalize(hist1, hist1, 0, 1, Core.NORM_MINMAX);
            
            Imgproc.calcHist(hsvList2, new org.opencv.core.MatOfInt(channels), 
                new Mat(), hist2, new org.opencv.core.MatOfInt(histSize), 
                new org.opencv.core.MatOfFloat(hRanges[0], hRanges[1], sRanges[0], sRanges[1]));
            Core.normalize(hist2, hist2, 0, 1, Core.NORM_MINMAX);
            
            // Compare histograms using correlation method
            double correlation = Imgproc.compareHist(hist1, hist2, Imgproc.HISTCMP_CORREL);
            double similarity = correlation * 100; // Convert to percentage
            
            logger.info(String.format("Histogram comparison result: %.2f%% similarity", similarity));
            
            return similarity;
            
        } catch (Exception e) {
            logger.error("Error comparing images with histogram", e);
            return 0.0;
        }
    }
    
    /**
     * Compare images with better handling for different sizes
     * Uses both pixel comparison and histogram comparison
     * @param imagePath1 Path to first image
     * @param imagePath2 Path to second image
     * @return Similarity percentage (0-100)
     */
    public static double compareImagesRobust(String imagePath1, String imagePath2) {
        if (!opencvLoaded) {
            logger.error("OpenCV not loaded. Cannot compare images.");
            return 0.0;
        }
        
        try {
            // Get both similarity scores
            double pixelSimilarity = compareImages(imagePath1, imagePath2);
            double histogramSimilarity = compareImagesHistogram(imagePath1, imagePath2);
            
            // Use the higher score (more forgiving for size differences)
            double finalSimilarity = Math.max(pixelSimilarity, histogramSimilarity);
            
            logger.info(String.format("Robust comparison - Pixel: %.2f%%, Histogram: %.2f%%, Final: %.2f%%", 
                pixelSimilarity, histogramSimilarity, finalSimilarity));
            
            return finalSimilarity;
            
        } catch (Exception e) {
            logger.error("Error in robust image comparison", e);
            return 0.0;
        }
    }
    
    /**
     * Save screenshot comparison report
     * @param actualImagePath Path to actual image
     * @param expectedImagePath Path to expected image
     * @param outputPath Path to save difference image
     * @return Similarity percentage
     */
    public static double saveComparisonReport(String actualImagePath, 
                                             String expectedImagePath, 
                                             String outputPath) {
        if (!opencvLoaded) {
            logger.error("OpenCV not loaded. Cannot create comparison report.");
            return 0.0;
        }
        
        try {
            Mat img1 = Imgcodecs.imread(actualImagePath);
            Mat img2 = Imgcodecs.imread(expectedImagePath);
            
            if (img1.empty() || img2.empty()) {
                logger.error("One or both images could not be loaded");
                return 0.0;
            }
            
            // Resize if needed
            if (img1.size().width != img2.size().width || img1.size().height != img2.size().height) {
                Imgproc.resize(img2, img2, img1.size());
            }
            
            // Calculate difference
            Mat diff = new Mat();
            Core.absdiff(img1, img2, diff);
            
            // Save difference image
            Imgcodecs.imwrite(outputPath, diff);
            logger.info("Comparison report saved to: " + outputPath);
            
            // Calculate similarity
            Mat grayDiff = new Mat();
            Imgproc.cvtColor(diff, grayDiff, Imgproc.COLOR_BGR2GRAY);
            double totalPixels = grayDiff.rows() * grayDiff.cols();
            double nonZeroPixels = Core.countNonZero(grayDiff);
            double similarity = ((totalPixels - nonZeroPixels) / totalPixels) * 100;
            
            return similarity;
            
        } catch (Exception e) {
            logger.error("Error creating comparison report", e);
            return 0.0;
        }
    }
}
