##My project

Native Java DIP (digital image processing) application

**Current features**

- reading images from anywhere:
    - from local drive
    - from URLs
- some kinds of pics processing:
    - greyscale
    - sepia
    - negative
- saving processed image

**Future features**

- different types of pics processing
- different types of filters (gaussian, box etc)
- ...

####Necessary info about OpenCV set-up 
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