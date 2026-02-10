# Amazon Shopping App - Appium Cucumber Automation Framework

## 🏗️ Framework Architecture

The framework follows best practices:

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

### 5. **Zoom Gestures**
- Zoom in on elements (pinch to zoom)
- Zoom out on elements

### 6. **Drag and Drop**
- Drag element from source to destination
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

### Run from IDE

#### IntelliJ IDEA
1. Right-click on `TestRunner.java`
2. Select "Run 'TestRunner'"

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

## ☁️ AWS Device Farm Integration

Follow this checklist to get your tests running on AWS Device Farm:

- [ ] **Step 1:** Have an AWS account (sign up at aws.amazon.com)
- [ ] **Step 2:** Obtain Amazon Shopping app APK file
- [ ] **Step 3:** Modify `DriverManager.java` to support Device Farm (see code below)
- [ ] **Step 4:** Update `pom.xml` with assembly plugin configuration
- [ ] **Step 5:** Create `zip-assembly.xml` in `src/test/resources/`
- [ ] **Step 6:** Create `testng-devicefarm.xml` configuration
- [ ] **Step 7:** Build test package: `mvn clean package -DskipTests`
- [ ] **Step 8:** Verify `target/zip-with-dependencies.zip` is created
- [ ] **Step 9:** Log in to AWS Device Farm console
- [ ] **Step 10:** Create a new project
- [ ] **Step 11:** Upload APK file
- [ ] **Step 12:** Upload test package (ZIP file)
- [ ] **Step 13:** Select devices to test on
- [ ] **Step 14:** Start test run
- [ ] **Step 15:** Monitor results and view reports
