# HR Management System

<img src="./Releases/Icon.png" alt="HR Management Application" style="width:400px; height:auto;" />
*Screenshot of the HR Management Application*

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

- **JavaFX SDK 17.0.15** required to run the released JAR files.  

You can download the JavaFX SDK for Windows here:  
[JavaFX SDK 17.0.15 - Windows x64](https://download2.gluonhq.com/openjfx/17.0.15/openjfx-17.0.15_windows-x64_bin-sdk.zip)  

After downloading, unzip the SDK and place the entire folder inside the `Releases` directory in this repository.

---

## Running the Application Releases
```
Each instance contains a precompiled JAR file located in its respective folder inside `Releases`. To run an instance:

1. Make sure the JavaFX SDK folder (`javafx-sdk`) is inside the `Releases` folder.
2. Open the folder of the desired instance inside `Releases` (e.g., `Releases/Attendance`).
3. Run the included batch file (`run.bat`) to launch the application.

**Example:**  

cd Releases/Attendance
run.bat
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
