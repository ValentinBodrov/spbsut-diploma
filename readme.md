**My project**

Java DIP (digital image processing) application

**Current features**

- reading images from anywhere:
    - from local drive
    - from URLs
- some kinds of pics processing:
    - greyscale
    - sepia
    - negative
    - randomizing image's pixels
    - custom processing (influencing on red, green or blue channels)
    - pics rotating (on 90 grads)
    - pics vertical and horizontal mirroring
- selecting, selective processing and pics-cropping
- drag-and-drop feature support
    - drag and drop an image from everywhere to process it 
- adding labels and captions on image
- some kinds of OpenCV-processing:
    - operations with brightness and contrast
    - operations with sharpness
    - pixelation
    - making image blurred
        - gaussian blur
        - median blur
    - making image filtered
        - bilateral filter
        - box filter
        - SQR-box filter
        - 2D-filter
    - selecting the object in front of pic and enhancing sharpness on it (beta)
- saving processed image

**Future features**

- splitting the whole project into two semi-separate:
    - the project itself
    - the project for testing
- paint-like brushes
- styles
- transformations (making image rounded or squeeze)
- meme creating features:
    - demotivators
    - addition of white field in the top of pic for texts
    - making image more little for the further processing simplifying
- splitting tests classes into tests for native and CV-prosessings
- ...  
- maybe some test scenarios

**Bugs those could be fixed**
 
- signs on large images aren't scalable
- too slow on extra large pics
- an executable jar-file cannot be created 

**Necessary info about OpenCV set-up**
 
This project uses OpenCV libraries, and before starting the work with code they
should be installed. 
1. Download and install OpenCV
2. In ur project go to File -> Project Structure
3. Select Modules -> Dependencies tab. Click the "+"-icon
to add a dependency. Select the "Add JAR/Directory" option
4. Browse to the path where you installed the OpenCv and select
build/java/opencv-***.jar, then click OK. (*** is ur version)
5. Double click on the open opencv-***.jar
6. Click on the "+"-icon to add the Native Library Location
7. Browse to the location where u installed OpenCV and 
select build/java/x64. (or x86 in case of other system specification). 
8. Click OK and apply the changes
NB! The instructions below belong to InteliJ IDEA 
