/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package real_time_image_processing;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.indexer.*;
import org.bytedeco.javacpp.opencv_core.IplImage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_calib3d.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

/**
 *
 * @author samuelthomas
 */
public class FaceDetector {
    
        public static void main(String[] args) {
        try {
            
            
            String classifierName = null;
            
            if (args.length > 0) {
                classifierName = args[0];
            } else {
                URL url = null;
                try {
                    url = new URL("https://raw.github.com/Itseez/opencv/2.4/data/haarcascades/haarcascade_frontalface_alt.xml");
                } catch (MalformedURLException ex) {
                    Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                File file = null;
                try {
                    file = Loader.extractResource(url, null, "classifier", ".xml");
                } catch (IOException ex) {
                    Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
                }
                file.deleteOnExit();
                classifierName = file.getAbsolutePath();
            }
            
            // Preload the opencv_objdetect module to work around a known bug.
            Loader.load(opencv_objdetect.class);
            
            // We can "cast" Pointer objects by instantiating a new object of the desired class.
            CvHaarClassifierCascade classifier = new CvHaarClassifierCascade(cvLoad(classifierName));
            if (classifier.isNull()) {
                System.err.println("Error loading classifier file \"" + classifierName + "\".");
                System.exit(1);
            }
            
            // The available FrameGrabber classes include OpenCVFrameGrabber (opencv_highgui),
            // DC1394FrameGrabber, FlyCaptureFrameGrabber, OpenKinectFrameGrabber,
            // PS3EyeFrameGrabber, VideoInputFrameGrabber, and FFmpegFrameGrabber.
            FrameGrabber grabber = null;
            try {
                grabber = FrameGrabber.createDefault(0);
            } catch (FrameGrabber.Exception ex) {
                Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
            grabber.start();
            
            // FAQ about IplImage:
            // - For custom raw processing of data, createBuffer() returns an NIO direct
            //   buffer wrapped around the memory pointed by imageData, and under Android we can
            //   also use that Buffer with Bitmap.copyPixelsFromBuffer() and copyPixelsToBuffer().
            // - To get a BufferedImage from an IplImage, we may call getBufferedImage().
            // - The createFrom() factory method can construct an IplImage from a BufferedImage.
            // - There are also a few copy*() methods for BufferedImage<->IplImage data transfers.
            IplImage grabbedImage = grabber.grab();
            int width  = grabbedImage.width();
            int height = grabbedImage.height();
            IplImage grayImage    = IplImage.create(width, height, IPL_DEPTH_8U, 1);
            IplImage rotatedImage = grabbedImage.clone();
            
            // Objects allocated with a create*() or clone() factory method are automatically released
            // by the garbage collector, but may still be explicitly released by calling release().
            // You shall NOT call cvReleaseImage(), cvReleaseMemStorage(), etc. on objects allocated this way.
            CvMemStorage storage = CvMemStorage.create();
            
            // The OpenCVFrameRecorder class simply uses the CvVideoWriter of opencv_highgui,
            // but FFmpegFrameRecorder also exists as a more versatile alternative.
            FrameRecorder recorder = null;
            try {
                recorder = FrameRecorder.createDefault("output.avi", width, height);
            } catch (FrameRecorder.Exception ex) {
                Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                recorder.start();
            } catch (FrameRecorder.Exception ex) {
                Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // CanvasFrame is a JFrame containing a Canvas component, which is hardware accelerated.
            // It can also switch into full-screen mode when called with a screenNumber.
            // We should also specify the relative monitor/camera response for proper gamma correction.
            CanvasFrame frame = new CanvasFrame("Some Title", CanvasFrame.getDefaultGamma()/grabber.getGamma());
            
            // Let's create some random 3D rotation...
            CvMat randomR = CvMat.create(3, 3), randomAxis = CvMat.create(3, 1);
            // We can easily and efficiently access the elements of matrices and images
            // through an Indexer object with the set of get() and put() methods.
            DoubleIndexer Ridx = randomR.createIndexer(), axisIdx = randomAxis.createIndexer();
            axisIdx.put(0, (Math.random()-0.5)/4, (Math.random()-0.5)/4, (Math.random()-0.5)/4);
            cvRodrigues2(randomAxis, randomR, null);
            double f = (width + height)/2.0;  Ridx.put(0, 2, Ridx.get(0, 2)*f);
            Ridx.put(1, 2, Ridx.get(1, 2)*f);
            Ridx.put(2, 0, Ridx.get(2, 0)/f); Ridx.put(2, 1, Ridx.get(2, 1)/f);
            System.out.println(Ridx);
            
            // We can allocate native arrays using constructors taking an integer as argument.
            CvPoint hatPoints = new CvPoint(3);
            
            while (frame.isVisible() && (grabbedImage = grabber.grab()) != null) {
                cvClearMemStorage(storage);
                
                // Let's try to detect some faces! but we need a grayscale image...
                cvCvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
                CvSeq faces = cvHaarDetectObjects(grayImage, classifier, storage,1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
                
                int total = faces.total();
                for (int i = 0; i < total; i++) {
                    CvRect r = new CvRect(cvGetSeqElem(faces, i));
                    int x = r.x(), y = r.y(), w = r.width(), h = r.height();
                    cvRectangle(grabbedImage, cvPoint(x, y), cvPoint(x+w, y+h), CvScalar.RED, 1, CV_AA, 0);
                    
                    // To access or pass as argument the elements of a native array, call position() before.
                    hatPoints.position(0).x(x-w/10)   .y(y-h/10);
                    hatPoints.position(1).x(x+w*11/10).y(y-h/10);
                    hatPoints.position(2).x(x+w/2)    .y(y-h/2);
                    cvFillConvexPoly(grabbedImage, hatPoints.position(0), 3, CvScalar.GREEN, CV_AA, 0);
                }
                
                // Let's find some contours! but first some thresholding...
                cvThreshold(grayImage, grayImage, 64, 255, CV_THRESH_BINARY);
                
                // To check if an output argument is null we may call either isNull() or equals(null).
                CvSeq contour = new CvSeq(null);
                cvFindContours(grayImage, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
                
                while (contour != null && !contour.isNull()) {
                    if (contour.elem_size() > 0) {
                        CvSeq points = cvApproxPoly(contour, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, cvContourPerimeter(contour)*0.02, 0);
                        cvDrawContours(grabbedImage, points, CvScalar.BLUE, CvScalar.BLUE, -1, 1, CV_AA);
                    }
                    contour = contour.h_next();
                }
                
                cvWarpPerspective(grabbedImage, rotatedImage, randomR);
                
                frame.showImage(rotatedImage);
                try {
                    recorder.record(rotatedImage);
                } catch (FrameRecorder.Exception ex) {
                    Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            frame.dispose();
            try {
                recorder.stop();
            } catch (FrameRecorder.Exception ex) {
                Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
            }
            grabber.stop();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(FaceDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
