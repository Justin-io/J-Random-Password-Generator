# 🖤 J-Random Password Generator

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/assets/logo.png" alt="J-Random Password Generator Logo" width="200">

  <h3>A modern, secure, and beautiful password generator with a sleek glass-morphism UI.</h3>
  <p>Generate strong, unique passwords and store them securely in your local database.</p>

  [![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
  [![Swing](https://img.shields.io/badge/Swing-GUI-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
  [![MariaDB](https://img.shields.io/badge/MariaDB-Database-003545.svg)](https://mariadb.org/)
  [![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
  [![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey.svg)]()
</div>

## 📚 Table of Contents

- [Screenshots](#-screenshots)
- [✨ Features](#-features)
- [📋 System Requirements](#-system-requirements)
- [🚀 Installation Guide](#-installation-guide)
  - [Step 1: Install Java](#step-1-install-java)
  - [Step 2: Install MariaDB](#step-2-install-mariadb)
  - [Step 3: Database Setup](#step-3-database-setup)
  - [Step 4: Download & Run the Application](#step-4-download--run-the-application)
- [📖 How to Use](#-how-to-use)
- [⚙️ Configuration](#️-configuration)
- [🐛 Troubleshooting & FAQ](#-troubleshooting--faq)
- [🏗️ Project Structure & For Developers](#️-project-structure--for-developers)
- [🤝 Contributing](#-contributing)
- [📝 License](#-license)
- [👨‍💻 Author](#-author)

---

## 📸 Screenshots

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/screenshots/main-interface.png" alt="Main Interface">
  <p><em>The main interface, featuring the password generator and strength indicator.</em></p>
</div>

<br>

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/screenshots/database-view.png" alt="Database View">
  <p><em>View and manage your saved passwords in the integrated database table.</em></p>
</div>

<br>

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/screenshots/custom-dialog.png" alt="Custom Dialog">
  <p><em>Custom-styled dialogs for saving passwords and displaying messages.</em></p>
</div>

---

## ✨ Features

- 🔐 **Cryptographically Secure**: Uses `SecureRandom` for generating unpredictable, high-quality passwords.
- 🎨 **Glass-Morphism UI**: A stunning, modern dark theme with transparency, blur, and smooth animations.
- 📊 **Visual Password Strength**: Get instant feedback with a color-coded progress bar (Weak, Medium, Strong).
- 💾 **Local Database Storage**: Your passwords are stored locally in a MariaDB database, not on someone else's server.
- 📋 **One-Click Copy**: Instantly copy generated passwords to your clipboard with a single click.
- 🎛️ **Fully Customizable**: Control password length (8-32 characters) and choose which character sets to include (Uppercase, Lowercase, Numbers, Symbols).
- 🖱️ **Draggable Window**: Click and drag anywhere on the window to move it around your screen.
- 🗃️ **Integrated Password Vault**: Show, hide, and delete saved passwords directly from the application.

---

## 📋 System Requirements

Before you begin, ensure your system meets the following requirements:

| Component | Minimum Version | Recommended |
| :--- | :--- | :--- |
| **Java** | Java Runtime Environment (JRE) 17 | JDK 17 or newer |
| **Database** | MariaDB 10.6 | MariaDB 10.6 or newer |
| **Operating System** | Windows 10, macOS 10.15, Ubuntu 20.04 | Latest versions of Windows, macOS, or Linux |

---

## 🚀 Installation Guide

Follow these steps carefully to set up the application on your machine.

### Step 1: Install Java

The application requires Java 17 or newer to run.

1.  **Download**: Go to the official [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/) page.
2.  **Select your OS** (Windows, macOS, or Linux) and download the appropriate `JDK` (Java Development Kit).
3.  **Install**: Run the installer you downloaded and follow the on-screen instructions.
4.  **Verify (Optional)**: Open a Command Prompt (Windows) or Terminal (macOS/Linux) and type `java -version`. You should see the installed Java version.

### Step 2: Install MariaDB

This application uses MariaDB as its database.

1.  **Download**: Go to the official [MariaDB Downloads](https://mariadb.org/download/) page.
2.  **Select your OS** and download the stable version of **MariaDB Server**.
3.  **Install**: Run the installer.
    *   **Important**: During setup, you will be prompted to set a password for the `root` user. Remember this password!
    *   Ensure the "Install as service" option is checked (on Windows) so the database starts automatically.

### Step 3: Database Setup

You only need to do this once. The application will create its own database and table, but it needs a user to connect with.

1.  **Open a Database Client**:
    *   **Easy Way (GUI)**: Install a free tool like [HeidiSQL](https://www.heidisql.com/) (Windows) or [DBeaver](https://dbeaver.io/) (Cross-Platform).
    *   **Command Line**: Open the MariaDB Command Prompt (from the Start Menu) or a regular terminal.

2.  **Connect to MariaDB**:
    *   In your client, connect to `localhost` or `127.0.0.1` with the `root` user and the password you created in Step 2.

3.  **Create the User and Database**:
    Copy and paste the following SQL commands into your client's query window and execute them. This creates a dedicated user `appuser` that the application will use.

    ```sql
    -- Create a new user named 'appuser' that can only connect from localhost
    -- Replace 'YourStrongPassword123!' with a secure password of your choice.
    CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'YourStrongPassword123!';

    -- Grant this user all permissions on the password_db database (which will be created by the app)
    GRANT ALL PRIVILEGES ON password_db.* TO 'appuser'@'localhost';

    -- Apply the changes
    FLUSH PRIVILEGES;
    ```

### Step 4: Download & Run the Application

1.  **Download the Source Code**:
    *   Go to the [GitHub repository](https://github.com/justinalexia/J-Random-PassWord-Gen).
    *   Click the green `< > Code` button and select **Download ZIP**.
    *   Unzip the file to a location you can easily find, like `C:\Programs\PasswordGenerator`.

2.  **Compile the Code**:
    *   Open a Command Prompt or Terminal.
    *   Navigate to the folder where you unzipped the files. For example: `cd C:\Programs\PasswordGenerator`
    *   Compile the Java file:
        ```bash
        javac PasswordGeneratorApp.java
        ```

3.  **Run the Application**:
    *   In the same terminal window, run the application:
        ```bash
        java PasswordGeneratorApp
        ```

#### 🖥️ For Windows Users: Create a One-Click Launcher

To avoid opening the command prompt every time, you can create a batch file.

1.  Inside the application folder, create a new text file.
2.  Rename it to `Run App.bat` (make sure you change the extension from `.txt` to `.bat`).
3.  Right-click `Run App.bat` and choose "Edit".
4.  Paste the following lines into the file, **adjusting the path** to where you unzipped the app:

    ```batch
    @echo off
    cd /d "C:\Programs\PasswordGenerator"
    java PasswordGeneratorApp
    ```
5.  Save the file. Now, you can just double-click `Run App.bat` to launch the application!

---

## 📖 How to Use

1.  **Generate a Password**:
    *   Use the **Length Slider** to set the desired password length.
    *   Check the boxes for the character types you want to include (Uppercase, Lowercase, etc.).
    *   Click the **Generate** button. A new password will appear in the field.

2.  **Check Password Strength**:
    *   Below the password field, the **Strength Bar** will automatically update to show you how strong the generated password is.

3.  **Copy to Clipboard**:
    *   After generating a password, click the **Copy** button. The password is now on your clipboard and ready to be pasted anywhere.

4.  **Save a Password**:
    *   Click the **Save** button.
    *   A dialog will pop up asking for a **description** (e.g., "My Gmail Account," "Netflix Login").
    *   Enter a description and click **OK**. The password is now saved in your database.

5.  **Manage Saved Passwords**:
    *   Click the **Show Database** button to reveal the table of all your saved passwords.
    *   To delete a password, click on its row in the table to select it, then click the **Delete** button.
    *   Click **Hide Database** to collapse the table view again.

6.  **Exit the Application**:
    *   Click the **Exit** button or the red `✕` button in the top-right corner. A confirmation dialog will appear.

---

## ⚙️ Configuration

If you changed the database password during setup, you must update the application's configuration.

1.  Open the `PasswordGeneratorApp.java` file in a text editor.
2.  Find these lines near the top of the file:
    ```java
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/password_db";
    private static final String DB_USER = "appuser";
    private static final String DB_PASSWORD = "YourStrongPassword123!";
    ```
3.  Change the value of `DB_PASSWORD` to the new password you set for the `appuser`.
4.  Save the file and **recompile** it using the `javac` command from the installation guide.

---

## 🐛 Troubleshooting & FAQ

**Q: I get a "Database error" when I start the app.**
**A:** This is the most common issue. It means the application cannot connect to MariaDB.
*   **Check 1:** Is MariaDB running? On Windows, check Services for "MariaDB". On macOS/Linux, run `sudo systemctl status mariadb`.
*   **Check 2:** Did you complete [Step 3: Database Setup](#step-3-database-setup)? Ensure the `appuser` exists and the password in the `PasswordGeneratorApp.java` file matches exactly.
*   **Check 3:** Is your firewall blocking the connection? Ensure it allows connections to `localhost` on port `3306`.

**Q: The app window is blank or looks weird.**
**A:** This is usually a graphics or Java issue.
*   Make sure you have the latest graphics drivers for your computer.
*   Try updating your Java installation to the newest version.

**Q: I click "Run App.bat" and a window flashes and disappears.**
**A:** This means an error occurred on startup.
*   Edit the `.bat` file and add a new line: `pause`
    ```batch
    @echo off
    cd /d "C:\Programs\PasswordGenerator"
    java PasswordGeneratorApp
    pause
    ```
*   Now when you run it, the window will stay open and show you the error message.

---

## 🏗️ Project Structure & For Developers

This project is a single-file Java Swing application.

-   `PasswordGeneratorApp.java`: The main class containing all UI, logic, and database handling.
-   `BlackGlassDialog.java`: A helper class for creating custom-styled dialog windows.

**To import into an IDE (like IntelliJ or Eclipse):**
1.  Create a new Java project.
2.  Copy the `.java` files into the project's `src` directory.
3.  Add the MariaDB JDBC driver JAR to your project's dependencies/classpath. You can download it from [Maven Central](https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client).

---

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Justin Alexia**

*   Portfolio: [https://justinalexia.vercel.app](https://justinalexia.vercel.app)
*   GitHub: [@justinalexia](https://github.com/justinalexia)

---

<div align="center">
  <p>If you find this project helpful, consider giving it a ⭐️!</p>
  <p>Made with ❤️ and a lot of ☕</p>
</div>
