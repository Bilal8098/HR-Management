# HR Management System

<img src="./Releases/Icon.png" alt="HR Management Application" style="width:400px; height:auto;" />

---

## Overview

The **HR Management System** is a comprehensive software solution designed to streamline human resources operations across different roles within an organization. The system consists of four distinct modules (instances), each targeting a specific function:

- **Attendance Instance**  
- **HR Instance**  
- **Manager Instance**  
- **Employee Instance**  

Each module is developed as a separate project within this repository to provide modularity and flexibility.

---

## Prerequisites

Before running the application, ensure your system meets the following requirements:

### ✅ Java Version
- You must have **Java 11 or newer** installed.

You can download the latest Java JDK from the official Oracle site:
- [Java download](https://www.oracle.com/java/technologies/downloads/)
- or you can downlaod java 17 by [Clicking here](https://www.oracle.com/webapps/redirect/signon?nexturl=https://download.oracle.com/otn/java/jdk/17.0.15%2B9/4f092786cec841d58ae21840b10204d7/jdk-17.0.15_windows-x64_bin.zip)

### ✅ JavaFX SDK

This application was developed using **JavaFX SDK 17**. You must download and link this SDK to run the app.

#### 🔽 Download JavaFX SDK 17.0.15

- You can browse all versions by accessing [JavaFX official download page](https://gluonhq.com/products/javafx/)
- Or you can simply [Click here](https://download2.gluonhq.com/openjfx/17.0.15/openjfx-17.0.15_windows-x64_bin-sdk.zip) to download JavaFX SDK 17.0.15 for Windows (x64)

> After downloading, unzip the SDK and place the entire folder inside the `Releases` directory of this repository.


---

## Running the Application Releases
```
Each instance contains a precompiled JAR file located in its respective folder inside `Releases`. To run an instance:

1. Make sure the JavaFX SDK folder (`javafx-sdk`) is inside the `Releases` folder.
2. Open the folder of the desired instance inside `Releases` (e.g., `Releases/Attendance`).
3. Run the included batch file (`run.bat`) to launch the application.

Example:
cd Releases/Attendance instance
Attendance.bat
The batch files are pre-configured to include the necessary JavaFX libraries from the SDK to run the application smoothly.

Development
If you want to build or modify the projects:

Open the respective instance folder (e.g., Attendance) in your preferred Java IDE.

Ensure your project SDK is set to JDK 17 or above.

Add JavaFX libraries to your project build path (matching the version used in the SDK).

Build and run the project as you normally would.

Notes
The projects are modular and can be deployed independently or together depending on organizational needs.

The Releases folder contains fully executable releases for quick deployment and testing.

JavaFX SDK must always be kept inside the Releases folder for the batch scripts to work properly.

Thank you for using the HR Management System!
```
## To run manually
Run this command in a CMD in the Releases directory
```bash
java --module-path "../javafx-sdk-17.0.15/lib" --add-modules javafx.controls,javafx.fxml -jar target/Replace_with_jar_name.jar
```
## Support

For technical support or contributions:
- Open a [GitHub Issue](https://github.com/Bilal8098/HR-Management/issues)
- Contact our leadership team:

**Project Leadership:**
- **Bilal Ahmed** (Project Lead)  
  Email: [reddivel8098@gmail.com](mailto:reddivel8098@gmail.com)  
  Phone: [+20 114 151 1177](tel:+201141511177)

**Development Team:**
- **Co-leader: Roaya Youssef**
- Seif Sayed
- Kareem Salem 
- Shams Mohammed
- Ahmed Hamdy

© 2024 HR Management System | Developed with JavaFX
