/*
 * IMAGE PROCESSING FOR REAL TIME OVERCROWDED AREA DETECTION
 */
package real_time_image_processing;

import fr.ece.pfe_project.panel.MainPanel.FaceDetectorListener;
import java.io.IOException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.net.URL;

import org.bytedeco.javacv.*;
import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT;
import static org.bytedeco.javacpp.opencv_highgui.CV_CAP_PROP_FRAME_WIDTH;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

/**
 *
 * @author samuelthomas
 *
 * Face detection implementation for crowd management
 */
public class FaceDetectorThread extends Thread {

    public interface FaceDetectorInterface {

        public void getCountFaceDetected(int number_of_faces);
    }

    public FaceDetectorThread(FaceDetectorListener listener) {
        faceDetectorListener = listener;
    }

    private boolean active = false;
    private int cameraID = -1;
    private FaceDetectorListener faceDetectorListener;

    public boolean isActive() {
        return active;
    }

    public void setCameraID(int id) {
        this.cameraID = id;
    }

    public int getCameraID() {
        return this.cameraID;
    }

    public void launch(int id) {
        setCameraID(id);
        start();
    }

    @Override
    public void run() {
        System.err.println("RUN");
        this.active = true;
        faceDetectionThread(this.cameraID);
    }

    public void faceDetectionThread(int id_camera) {

        System.err.println("START FACE DETECT #" + id_camera);
        int number_of_faces_detected = 0;

        try {
            String classifierName = null;

            URL url = null;
            try {
                url = new URL("https://raw.github.com/Itseez/opencv/2.4/data/haarcascades/haarcascade_frontalface_alt.xml");
            } catch (MalformedURLException ex) {
                Logger.getLogger(FaceDetectorThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            File file = null;
            try {
                file = Loader.extractResource(url, null, "classifier", ".xml");
            } catch (IOException ex) {
                Logger.getLogger(FaceDetectorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            file.deleteOnExit();
            classifierName = file.getAbsolutePath();

            // Preload the opencv_objdetect module to work around a known bug.
            Loader.load(opencv_objdetect.class);

            // We can "cast" Pointer objects by instantiating a new object of the desired class.
            CvHaarClassifierCascade classifier = new CvHaarClassifierCascade(cvLoad(classifierName));
            if (classifier.isNull()) {
                System.err.println("Error loading classifier file \"" + classifierName + "\".");
                System.exit(1);
            }

            FrameGrabber grabber = null;
            try {
                grabber = FrameGrabber.createDefault(id_camera);
            } catch (FrameGrabber.Exception ex) {
                Logger.getLogger(FaceDetectorThread.class.getName()).log(Level.SEVERE, null, ex);
            }

            grabber.setImageWidth(300);
            grabber.setImageHeight(300);

            grabber.start();

//***************************************************************************************************************//
            // IMAGE COMPARISON
            // Image template
            System.out.println("taking template picture");
            IplImage template = grabber.grab();
            int template_width = template.width();
            int template_height = template.height();
            
            System.out.println("creating gray template IplImage");
            IplImage grayTemplate = IplImage.create(template_width, template_height, IPL_DEPTH_8U, 1);
            cvCvtColor(template, grayTemplate, CV_BGR2GRAY);
            System.out.println("Template picture has been translated to B&W picture");
            
            CvMat mat_template = new CvMat(grayTemplate);
            System.out.println("B&W template matrix has been created");
            
            //CanvasFrame gray_frame_template = new CanvasFrame("Template");
            System.out.println("B&W template frame created");
            //gray_frame_template.showImage(grayTemplate);
            System.out.println("B&W template frame display");

            // Image to compare
            IplImage img_to_compare = grabber.grab();
            System.out.println("creating gray template IplImage");
            IplImage gray_img_to_compare = IplImage.create(template_width, template_height, IPL_DEPTH_8U, 1);
            cvCvtColor(img_to_compare, gray_img_to_compare, CV_BGR2GRAY);
            System.out.println("Template picture has been translated to B&W picture");
            
            CvMat mat_img_to_compare = new CvMat(grayTemplate);
            System.out.println("B&W img_to_compare matrix has been created");
            
            //CanvasFrame frame_img_to_compare = new CanvasFrame("New Image For comparison");
            //frame_img_to_compare.showImage(gray_img_to_compare);
            
            // Are the images equal?
            if (mat_img_to_compare.equals(mat_template))
                System.out.println("MATRIX ARE THE SAME");
            else System.out.println("MATRIX ARE NOT THE SAME");
            
            // Image result from comparison
            IplImage result = IplImage.create(template_width, template_height, IPL_DEPTH_8U, 1);
            System.out.println("Start Image comparison ");
            cvAbsDiff( grayTemplate, gray_img_to_compare, result);
            System.out.println("Display Image comaprison result");
            //CanvasFrame result_frame = new CanvasFrame("result");
            //result_frame.showImage(result);
            
            IplImage bitImage = IplImage.create(template_width, template_height, IPL_DEPTH_8U, 1);
            
            //cvThreshold(result,bitImage,tr,255,CV_THRESH_BINARY);

//***************************************************************************************************************//
            
            IplImage grabbedImage = grabber.grab();
            int width = grabbedImage.width();
            int height = grabbedImage.height();
            IplImage grayImage = IplImage.create(width, height, IPL_DEPTH_8U, 1);

            CvMemStorage storage = CvMemStorage.create();

            // Frame declaration for video display
            //CanvasFrame frame = new CanvasFrame("Face Detection", CanvasFrame.getDefaultGamma() / grabber.getGamma());
            while (isActive() && (grabbedImage = grabber.grab()) != null) {
                if (!isActive()) {
                    break;
                }

                number_of_faces_detected = 0;
                cvClearMemStorage(storage);

                // Let's try to detect some faces! but we need a grayscale image
                cvCvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
                CvSeq faces = cvHaarDetectObjects(grayImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

                int total = faces.total();
                for (int i = 0; i < total; i++) {
                    CvRect r = new CvRect(cvGetSeqElem(faces, i));
                    int x = r.x(), y = r.y(), w = r.width(), h = r.height();
                    cvRectangle(grabbedImage, cvPoint(x, y), cvPoint(x + w, y + h), CvScalar.RED, 1, CV_AA, 0);
                    number_of_faces_detected++;
                }

                System.out.println("Number of faces detected: " + number_of_faces_detected);

                faceDetectorListener.getCountFaceDetected(number_of_faces_detected);

                // Let's find some contours! but first some thresholding...
                cvThreshold(grayImage, grayImage, 64, 255, CV_THRESH_BINARY);

                // To check if an output argument is null we may call either isNull() or equals(null).
                CvSeq contour = new CvSeq(null);
                cvFindContours(grayImage, storage, contour, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);

                while (contour != null && !contour.isNull()) {
                    if (!isActive()) {
                        break;
                    }

                    if (contour.elem_size() > 0) {
                        CvSeq points = cvApproxPoly(contour, Loader.sizeof(CvContour.class), storage, CV_POLY_APPROX_DP, cvContourPerimeter(contour) * 0.02, 0);
                        //cvDrawContours(grabbedImage, points, CvScalar.BLUE, CvScalar.BLUE, -1, 1, CV_AA);
                    }
                    contour = contour.h_next();
                }

            } // END OF WHILE

            grabber.stop();
        } catch (FrameGrabber.Exception ex) {
            Logger.getLogger(FaceDetectorThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    } // END OF METHOD

    public void stopFaceDetection() {
        active = false;
    }

} // END OF CLASS
