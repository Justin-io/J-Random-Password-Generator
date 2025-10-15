# 🖤 J-Random Password Generator

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/assets/logo.png" alt="J-Random Password Generator Logo" width="200">
  
  <h3>A modern, secure password generator with a sleek glass-morphism UI</h3>
  
  [![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
  [![Swing](https://img.shields.io/badge/Swing-GUI-blue.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
  [![MariaDB](https://img.shields.io/badge/MariaDB-Database-003545.svg)](https://mariadb.org/)
  [![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
</div>

## 📸 Screenshots

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/screenshots/main-interface.png" alt="Main Interface" width="800">
  <p>Main Interface</p>
</div>

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/screenshots/database-view.png" alt="Database View" width="800">
  <p>Database View</p>
</div>

<div align="center">
  <img src="https://raw.githubusercontent.com/justinalexia/J-Random-PassWord-Gen/main/screenshots/password-strength.png" alt="Password Strength Indicator" width="800">
  <p>Password Strength Indicator</p>
</div>

## 🚀 Features

- 🔐 **Secure Password Generation**: Uses cryptographically secure random generation
- 🎨 **Modern Glass-Morphism UI**: Sleek dark theme with transparency effects
- 📊 **Password Strength Indicator**: Visual feedback on password strength
- 💾 **Database Storage**: Save passwords with descriptions in MariaDB
- 📋 **Clipboard Integration**: One-click copy to clipboard
- 🎛️ **Customizable Options**: Control length, character types, and more
- 🖱️ **Draggable Interface**: Move the window by dragging anywhere
- 📱 **Responsive Design**: Adapts to different screen sizes

## 🛠️ Installation

### Prerequisites

- Java 17 or higher
- MariaDB server
- An IDE like IntelliJ IDEA or Eclipse (optional)

### Database Setup

1. Install MariaDB on your system
2. Create a database user with the following credentials:
   - Username: `appuser`
   - Password: `YourStrongPassword123!`
3. Ensure the MariaDB server is running on `localhost:3306`

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/justinalexia/J-Random-PassWord-Gen.git
   cd J-Random-PassWord-Gen
   ```

2. Compile the Java files:
   ```bash
   javac PasswordGeneratorApp.java
   ```

3. Run the application:
   ```bash
   java PasswordGeneratorApp
   ```

## 📖 Usage

### Generating Passwords

1. Adjust the password length using the slider (8-32 characters)
2. Select the character types you want to include:
   - Uppercase letters (A-Z)
   - Lowercase letters (a-z)
   - Numbers (0-9)
   - Symbols (!@#$...)
3. Click the "Generate" button to create a new password
4. The password strength indicator will show you how secure your password is

### Saving Passwords

1. Generate a password or enter one manually
2. Click the "Save" button
3. Enter a description for the password
4. The password will be saved to the database

### Managing Saved Passwords

1. Click the "Show Database" button to view saved passwords
2. Select a password from the table
3. Use the "Delete" button to remove selected passwords

### Copying Passwords

1. Generate or select a password
2. Click the "Copy" button to copy it to your clipboard

## 🏗️ Architecture

The application is built with the following components:

### BlackGlassDialog Class
- Custom dialog with glass-like appearance
- Semi-transparent with rounded corners and blue glow effects
- Includes mouse drag functionality

### PasswordGeneratorApp Class
- Main application class with the following features:
  - Database integration
  - Custom UI components
  - Password generation logic
  - Event handling

## 🔧 Configuration

### Database Configuration

To change the database connection settings, modify these constants in `PasswordGeneratorApp.java`:

```java
private static final String DB_URL = "jdbc:mariadb://localhost:3306/password_db";
private static final String DB_USER = "appuser";
private static final String DB_PASSWORD = "YourStrongPassword123!";
```

### UI Customization

The application's appearance can be customized by modifying the color values in the code:

- Background colors: `new Color(8, 8, 12, 220)`
- Accent colors: `new Color(70, 130, 240)`
- Text colors: `new Color(200, 210, 230)`

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Justin Alexia**

- Portfolio: [https://justinalexia.vercel.app](https://justinalexia.vercel.app)
- GitHub: [@justinalexia](https://github.com/justinalexia)

## 🙏 Acknowledgments

- Icons and graphics created with love
- Inspired by modern glass-morphism design trends
- Built with Java Swing for cross-platform compatibility

## 🔮 Future Enhancements

- [ ] Password encryption before storing in database
- [ ] User authentication system
- [ ] Password categories and tags
- [ ] Password expiration reminders
- [ ] Export/import functionality
- [ ] Connection pooling for database operations
- [ ] Cross-platform installer packages

---

<div align="center">
  <p>Made with ❤️ by Justin Alexia</p>
</div>
