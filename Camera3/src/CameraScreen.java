
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.media.control.*;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

// The CameraScreen class shows the live view
// of the camera using the MMAPI and gives
// commands to capture the contents of the camera
class CameraScreen
        extends Canvas
        implements CommandListener {

    private final MMSMIDlet midlet;
    private final Command exitCommand;
    private Player player = null;
    private Command captureCommand = null;
    private Command getImage,  storeImage,  exit;
    private VideoControl videoControl = null;
    private boolean active = false;
    private Form form;
    private Display display = null;
    private RecordStore record = null;
    private Alert alert;

    CameraScreen(MMSMIDlet midlet) {
        this.midlet = midlet;
        alert = new Alert("Image Stored");
        // Builds the user interface
        exitCommand = new Command("Salir", Command.EXIT, 1);
//        exit=new Command("Exit", Command.EXIT, 1);
//        storeImage = new Command("StoreImage", Command.SCREEN, 1);
//        getImage = new Command("GetImage", Command.SCREEN, 1);
        addCommand(exitCommand);
        captureCommand = new Command("Capturar", Command.SCREEN, 1);
        addCommand(captureCommand);
//        form.addCommand(exit);
//        form.addCommand(storeImage);
//        form.addCommand(getImage);
//        form.setCommandListener(this);
        setCommandListener(this);
//        midlet.show(form);
    }

//Paint the background of the Canvas black.

    // Paint the canvas' background in black
    public void paint(Graphics g) {
        // black background
        g.setColor(0x00000000);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

//Create a method for detecting the use of Commands.
    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            midlet.exitApplication();
        } else if (c == captureCommand) {
            captureImage();
        }
    }


//Create a method for detecting key presses.
    public void keyPressed(int keyCode) {
        if (getGameAction(keyCode) == FIRE) {
            captureImage();
        }
    }

//Create a method for building and starting the video player.

    // //Creates and starts the video player
    synchronized void start() {
        try {
            player = Manager.createPlayer("capture://video");
            player.realize();

            // Get VideoControl for the viewfinder
            videoControl = (VideoControl) player.getControl("VideoControl");
            if (videoControl == null) {
                discardPlayer();
                midlet.showError("Cannot get the video control.\n" + "Capture may not be supported.");
                player = null;
            } else {
                // Set up the viewfinder on the screen.
                videoControl.initDisplayMode(VideoControl.USE_DIRECT_VIDEO,
                        this);
                int canvasWidth = getWidth();
                int canvasHeight = getHeight();
                int displayWidth = videoControl.getDisplayWidth();
                int displayHeight = videoControl.getDisplayHeight();
                int x = (canvasWidth - displayWidth) / 2;
                int y = (canvasHeight - displayHeight) / 2;
                videoControl.setDisplayLocation(x, y);
                player.start();
                videoControl.setVisible(true);
            }
        } catch (IOException ioe) {
            discardPlayer();
            midlet.showError("IOException: " + ioe.getMessage());
        } catch (MediaException me) {
            midlet.showError("MediaException: " + me.getMessage());
        } catch (SecurityException se) {
            midlet.showError("SecurityException: " + se.getMessage());
        }
    }

    // Stops the video player
    synchronized void stop() {
        if (player != null) {
            try {
                videoControl.setVisible(false);
                player.stop();
            } catch (MediaException me) {
                midlet.showError("MediaException: " + me.getMessage());
            }
            active = false;
        }
    }

    // this method will discard the video player
    private void discardPlayer() {
        if (player != null) {
            player.deallocate();
            player.close();
            player = null;
        }
        videoControl = null;
    }

    // captures the image from the video player
    // in a separate thread
    private void captureImage() {
        if (player != null) {
            // Capture image in a new thread.
            new Thread() {

                public void run() {
                    try {
                        byte[] pngImage = videoControl.getSnapshot("encoding=png");
                        midlet.imageCaptured(pngImage);
                        Image image = Image.createImage(pngImage, 0, pngImage.length);

                        System.out.println(image);
                        System.out.println("run:primero:ultimo: " + pngImage[0] + " " + pngImage[pngImage.length - 1]);
//                        for (int i = 0; i < pngImage.length; i++) {
//                            System.out.println(pngImage[i]);
//                        }
                        midlet.img=image;
                        midlet.pngImage=pngImage;
                        midlet.form.delete(0);
                        image=createThumbnail(image);
                         ImageItem imageitem = new ImageItem("", image, ImageItem.LAYOUT_TOP | ImageItem.LAYOUT_RIGHT, null);
                        midlet.form.insert(0, imageitem);

                        Display.getDisplay(midlet).setCurrent(midlet.form);
//                        midlet.show(image, pngImage);
//                    discardPlayer();
                    } catch (MediaException me) {
//                        midlet.showError("MediaException: " + me.getMessage());
                    } catch (SecurityException se) {
//                        midlet.showError("SecurityException: " + se.getMessage());
                    }
                }
            }.start();
        }
    }
     private Image createThumbnail(Image image) {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();

        int thumbWidth = 30;//64
        int thumbHeight = -1;//

        if (thumbHeight == -1) {
            thumbHeight = thumbWidth * sourceHeight / sourceWidth;
        }

        Image thumb = Image.createImage(thumbWidth, thumbHeight);
        Graphics g = thumb.getGraphics();

        for (int y = 0; y < thumbHeight; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                g.setClip(x, y, 1, 1);
                int dx = x * sourceWidth / thumbWidth;
                int dy = y * sourceHeight / thumbHeight;
                g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
            }
        }

        Image immutableThumb = Image.createImage(thumb);

        return immutableThumb;
    }

}



