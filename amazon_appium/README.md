# Amazon Shopping App - Appium Cucumber Automation Framework

A comprehensive Cucumber BDD framework for mobile automation testing using Appium and Java. This framework automates various scenarios on the Amazon Shopping Android app with Android 14.0 emulator.

## 🏗️ Framework Architecture

The framework follows industry-standard design patterns and best practices:

- **Page Object Model (POM)** - Separates page elements and actions
- **Cucumber BDD** - Behavior-driven development with Gherkin syntax
- **Singleton Pattern** - For driver management
- **Factory Pattern** - For capabilities configuration
- **Utility Classes** - Reusable helper methods

## 📁 Project Structure

```
getgo_appium/
├── src/test/java/com/getgo/
│   ├── config/
│   │   └── CapabilitiesConfig.java          # Desired Capabilities configuration
│   ├── drivermanager/
│   │   └── DriverManager.java               # Appium driver lifecycle management
│   ├── pages/
│   │   ├── BasePage.java                    # Base page with common methods
│   │   ├── AmazonHomePage.java              # Amazon home page objects
│   │   ├── AmazonSearchResultsPage.java     # Search results page objects
│   │   └── AmazonProductDetailsPage.java    # Product details page objects
│   ├── runner/
│   │   ├── TestRunner.java                  # JUnit test runner
│   │   └── TestRunnerTestNG.java            # TestNG test runner
│   ├── stepdefinitions/
│   │   └── AmazonShoppingSteps.java         # Cucumber step definitions
│   └── utils/
│       ├── ElementUtil.java                 # Element interaction utilities
│       ├── GestureUtil.java                 # Gesture utilities (swipe, zoom, drag)
│       ├── ImageComparisonUtil.java         # Image comparison utilities
│       └── WaitUtil.java                    # Wait utilities
├── src/test/resources/
│   ├── config/
│   │   └── capabilities.properties          # Appium capabilities config
│   ├── features/
│   │   └── AmazonShoppingAutomation.feature # Cucumber feature files
│   ├── cucumber.properties                  # Cucumber configuration
│   └── log4j2.xml                          # Logging configuration
├── pom.xml                                  # Maven dependencies
└── README.md                                # This file
```

## ✨ Features Implemented

### 1. **Click Operations**
- Click on elements using locators
- Click with wait for element to be clickable
- Click on WebElement objects

### 2. **Element Verification**
- Verify if element is present
- Verify if element is not present
- Verify if element is displayed
- Verify if element is enabled

### 3. **Swipe Gestures**
- Swipe up, down, left, right
- Custom swipe with coordinates and duration
- Swipe through carousels and banners

### 4. **Scroll Operations**
- Scroll down/up on pages
- Scroll until element is visible
- Scroll to element by text
- Scroll through search results

### 5. **Zoom Gestures**
- Zoom in on elements (pinch to zoom)
- Zoom out on elements
- Multi-touch gestures

### 6. **Drag and Drop**
- Drag element from source to destination
- Long press gestures
- Custom drag operations

### 7. **Image Comparison**
- Compare two images and get similarity percentage
- SSIM (Structural Similarity Index) comparison
- Verify images match within 80-90% threshold
- Save comparison reports with difference images

### 8. **Filter Handling**
- Open filter options
- Apply multiple filters
- Sort search results
- Navigate through filter categories

### 9. **Page Navigation**
- Navigate between pages (Home → Category → Product)
- Navigate back functionality
- Route verification
- Deep linking support

## 🛠️ Prerequisites

### Required Software

1. **Java Development Kit (JDK)**
   - Version: JDK 11 or higher
   - Download: https://www.oracle.com/java/technologies/downloads/

2. **Maven**
   - Version: 3.6.0 or higher
   - Download: https://maven.apache.org/download.cgi

3. **Node.js and npm**
   - Version: Node 16.x or higher
   - Download: https://nodejs.org/

4. **Appium Server**
   - Install globally: `npm install -g appium@next`
   - Install UiAutomator2 driver: `appium driver install uiautomator2`

5. **Android Studio**
   - Download: https://developer.android.com/studio
   - Install Android SDK
   - Set up Android emulator with Android 14.0

6. **Amazon Shopping App**
   - Install from Google Play Store on emulator
   - Or download APK from trusted source

### Environment Variables

Add these to your system environment variables:

```bash
# Java
JAVA_HOME=/path/to/jdk
PATH=$PATH:$JAVA_HOME/bin

# Android SDK
ANDROID_HOME=/path/to/android/sdk
PATH=$PATH:$ANDROID_HOME/platform-tools
PATH=$PATH:$ANDROID_HOME/tools
PATH=$PATH:$ANDROID_HOME/emulator

# Maven
MAVEN_HOME=/path/to/maven
PATH=$PATH:$MAVEN_HOME/bin
```

## 📱 Android Emulator Setup

### Create Android Emulator

1. Open Android Studio
2. Go to Tools → Device Manager
3. Click "Create Device"
4. Select a device (e.g., Pixel 6)
5. Select System Image: **Android 14.0 (UpsideDownCake) API 34**
6. Configure AVD settings
7. Click "Finish"

### Start Emulator

```bash
# List available emulators
emulator -list-avds

# Start emulator
emulator -avd <emulator_name>

# Or start from Android Studio Device Manager
```

### Install Amazon App on Emulator

Option 1: Using Play Store
- Open Play Store on emulator
- Search for "Amazon Shopping"
- Install the app

Option 2: Using ADB
```bash
adb install path/to/amazon.apk
```

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd getgo_appium
```

### 2. Install Dependencies

```bash
mvn clean install
```

### 3. Start Appium Server

```bash
# Option 1: Start Appium from terminal
appium

# Option 2: Start with specific port
appium --port 4723

# Option 3: Use Appium Desktop (GUI)
# Download from: https://github.com/appium/appium-desktop/releases
```

### 4. Verify Setup

```bash
# Check if emulator is running
adb devices

# Check if app is installed
adb shell pm list packages | grep amazon
```

## 🎯 Running Tests

### Run All Tests (JUnit)

```bash
mvn clean test
```

### Run All Tests (TestNG)

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### Run Specific Tag

```bash
# Run smoke tests
mvn clean test -Dcucumber.filter.tags="@Smoke"

# Run search tests
mvn clean test -Dcucumber.filter.tags="@Search"

# Run gesture tests
mvn clean test -Dcucumber.filter.tags="@Gesture"
```

### Run Specific Scenario

```bash
mvn clean test -Dcucumber.filter.tags="@E2E"
```

### Run Multiple Tags

```bash
# Run Smoke OR Search tests
mvn clean test -Dcucumber.filter.tags="@Smoke or @Search"

# Run Smoke AND Search tests
mvn clean test -Dcucumber.filter.tags="@Smoke and @Search"
```

### Run from IDE

#### IntelliJ IDEA
1. Right-click on `TestRunner.java`
2. Select "Run 'TestRunner'"

#### Eclipse
1. Right-click on `TestRunner.java`
2. Select "Run As" → "JUnit Test"

## 📊 Test Reports

After test execution, reports are generated in:

### Cucumber HTML Report
```
target/cucumber-reports/cucumber-html-report.html
```

### Cucumber JSON Report
```
target/cucumber-reports/cucumber.json
```

### Screenshots
```
screenshots/
```

### Logs
```
logs/automation.log
```

## ⚙️ Configuration

### Capabilities Configuration

Edit `src/test/resources/config/capabilities.properties`:

```properties
# Appium Server
appium.server.url=http://127.0.0.1:4723/wd/hub

# Android Capabilities
platform.name=Android
platform.version=14.0
device.name=Android Emulator
automation.name=UiAutomator2

# App Configuration
app.package=com.amazon.mShop.android.shopping
app.activity=com.amazon.mShop.home.HomeActivity

# Timeouts
implicit.wait=10
explicit.wait=20
```

### Cucumber Tags

Available tags in feature file:
- `@AmazonShopping` - All Amazon shopping tests
- `@Smoke` - Smoke test scenarios
- `@Search` - Search functionality tests
- `@ProductDetails` - Product details tests
- `@Click` - Click operation tests
- `@Swipe` - Swipe gesture tests
- `@Scroll` - Scroll operation tests
- `@Zoom` - Zoom gesture tests
- `@Filter` - Filter functionality tests
- `@Verification` - Element verification tests
- `@Navigation` - Navigation/routing tests
- `@Drag` - Drag gesture tests
- `@ImageComparison` - Image comparison tests
- `@E2E` - End-to-end scenarios
- `@Gesture` - Gesture tests
- `@ElementPresence` - Element presence verification
- `@ElementNotPresence` - Element absence verification

## 📝 Test Scenarios Covered

### Scenario 1: Product Search
- Search for products
- Verify search results
- Click on products

### Scenario 2: Product Navigation
- Navigate to product details
- View product information
- Scroll through product details

### Scenario 3: Image Interactions
- Swipe through product images
- Zoom in/out on images
- Compare images

### Scenario 4: Filters & Sorting
- Apply filters to search results
- Sort products
- Verify filtered results

### Scenario 5: Page Navigation
- Navigate between pages
- Use back navigation
- Verify correct routing

### Scenario 6: Gestures
- Swipe gestures (up, down, left, right)
- Scroll operations
- Zoom gestures
- Drag and drop

### Scenario 7: Element Verification
- Verify element presence
- Verify element absence
- Verify element display status

## 🔧 Troubleshooting

### Issue: Appium Server Not Starting
**Solution:**
```bash
# Kill existing Appium processes
killall node

# Restart Appium
appium
```

### Issue: Emulator Not Detected
**Solution:**
```bash
# Restart ADB
adb kill-server
adb start-server

# Check devices
adb devices
```

### Issue: App Not Found
**Solution:**
```bash
# Verify app is installed
adb shell pm list packages | grep amazon

# Get app package and activity
adb shell dumpsys window | grep -E 'mCurrentFocus'
```

### Issue: Element Not Found
**Solution:**
- Use Appium Inspector to get correct locators
- Increase implicit/explicit wait time
- Verify element is not inside a frame or shadow DOM

### Issue: OpenCV Not Loading (Image Comparison)
**Solution:**
- OpenCV is auto-loaded by the framework
- If issues persist, check OpenCV native libraries
- Verify Java architecture (32-bit vs 64-bit)

## 📚 Additional Resources

### Appium Documentation
- Official Docs: https://appium.io/docs/en/
- Appium Java Client: https://github.com/appium/java-client

### Cucumber Documentation
- Cucumber Docs: https://cucumber.io/docs/cucumber/
- BDD Practices: https://cucumber.io/docs/bdd/

### Selenium Documentation
- WebDriver Docs: https://www.selenium.dev/documentation/

### OpenCV for Java
- OpenCV Java: https://docs.opencv.org/4.x/d0/d0d/tutorial_java_intro.html

## 🤝 Best Practices Implemented

1. **Page Object Model** - Separation of concerns
2. **Explicit Waits** - Reliable element interactions
3. **Logging** - Comprehensive logging with Log4j2
4. **Exception Handling** - Proper error handling
5. **Reusable Methods** - DRY principle
6. **Configuration Management** - Externalized configuration
7. **Reporting** - Multiple report formats
8. **Screenshot on Failure** - Visual debugging
9. **Clean Code** - Readable and maintainable code
10. **BDD Scenarios** - Business-readable test cases

## 📞 Support

For issues or questions:
- Check the troubleshooting section
- Review Appium logs: `appium.log`
- Review application logs: `logs/automation.log`

## 📄 License

This project is created for educational and testing purposes.

## 🎉 Conclusion

This framework provides a complete solution for mobile automation testing with:
- ✅ Comprehensive gesture support
- ✅ Image comparison capabilities
- ✅ BDD approach with Cucumber
- ✅ Page Object Model
- ✅ Detailed reporting
- ✅ Easy configuration
- ✅ Robust error handling
- ✅ Extensive logging

Happy Testing! 🚀
