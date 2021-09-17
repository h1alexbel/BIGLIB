package errday.leaf;

import org.openimaj.demos.faces.Mustache;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.image.processing.face.detection.keypoints.FKEFaceDetector;
import org.openimaj.image.processing.face.detection.keypoints.FacialKeypoint;
import org.openimaj.image.processing.face.detection.keypoints.KEDetectedFace;
import org.openimaj.math.geometry.point.Point2dImpl;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LeafTest {

    public static void main(String[] args) throws IOException {
        MBFImage fImages = ImageUtilities.readMBF(new File("src/main/resources/worlds-largest-selfie.jpg"));
        FaceDetector faceDetector = new HaarCascadeDetector();
        List <DetectedFace> faces = faceDetector.detectFaces(Transforms.calculateIntensity(fImages));
        for (DetectedFace face : faces) {
            fImages.drawShape(face.getBounds(), RGBColour.YELLOW);
        }
        ImageUtilities.write(fImages,new File("src/main/resources/world-largest-selfie_detected.jpg") );
        //    processVideo();
    }


    private static void processVideo() throws VideoCaptureException {
        VideoCapture videoCapture = new VideoCapture(320,240);
        VideoDisplay<MBFImage> videoDisplay = VideoDisplay.createVideoDisplay(videoCapture);
        videoDisplay.addVideoListener(new VideoDisplayListener<MBFImage>() {
            @Override
            public void afterUpdate(VideoDisplay<MBFImage> videoDisplay) {

            }

            @Override
            public void beforeUpdate(MBFImage fImages) {
                FaceDetector faceDetector = new FKEFaceDetector();
                List <KEDetectedFace> faces = faceDetector.detectFaces(Transforms.calculateIntensity(fImages));
                for (KEDetectedFace face : faces) {
//     red detection               fImages.drawShape(face.getBounds(), RGBColour.RED);
                    try {
                        fImages.internalAssign(new Mustache().addMustaches(fImages));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    FacialKeypoint [] facialKeypoints = face.getKeypoints();
                    for (FacialKeypoint point : facialKeypoints) {
                        Point2dImpl clone = point.position.clone();
                        clone.translate((float)face.getBounds().minX(),(float)face.getBounds().minY());
                        fImages.drawPoint(clone, RGBColour.BLUE, 4);
                    }

                }
            }
        });
    }
}
