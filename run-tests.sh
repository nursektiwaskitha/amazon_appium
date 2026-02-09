#!/bin/bash

# Amazon Shopping Appium Test Execution Script
# This script automates the setup and execution of tests

echo "======================================"
echo "Amazon Shopping Automation Test Suite"
echo "======================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
echo "Checking prerequisites..."

if ! command_exists java; then
    echo -e "${RED}Error: Java is not installed${NC}"
    exit 1
else
    echo -e "${GREEN}✓ Java is installed${NC}"
    java -version
fi

if ! command_exists mvn; then
    echo -e "${RED}Error: Maven is not installed${NC}"
    exit 1
else
    echo -e "${GREEN}✓ Maven is installed${NC}"
fi

if ! command_exists adb; then
    echo -e "${RED}Error: ADB is not installed${NC}"
    exit 1
else
    echo -e "${GREEN}✓ ADB is installed${NC}"
fi

if ! command_exists appium; then
    echo -e "${RED}Error: Appium is not installed${NC}"
    echo "Install with: npm install -g appium@next"
    exit 1
else
    echo -e "${GREEN}✓ Appium is installed${NC}"
fi

# Check if emulator is running
echo ""
echo "Checking for connected devices..."
DEVICES=$(adb devices | grep -v "List" | grep "device" | wc -l)
if [ $DEVICES -eq 0 ]; then
    echo -e "${RED}Error: No Android devices/emulators found${NC}"
    echo "Please start an Android emulator or connect a device"
    exit 1
else
    echo -e "${GREEN}✓ Found $DEVICES device(s)${NC}"
    adb devices
fi

# Check if Amazon app is installed
echo ""
echo "Checking for Amazon app..."
APP_INSTALLED=$(adb shell pm list packages | grep -c "amazon.mShop")
if [ $APP_INSTALLED -eq 0 ]; then
    echo -e "${YELLOW}Warning: Amazon Shopping app not found on device${NC}"
    echo "Please install the app from Play Store or using ADB"
else
    echo -e "${GREEN}✓ Amazon Shopping app is installed${NC}"
fi

# Check if Appium server is running
echo ""
echo "Checking Appium server..."
if curl -s http://127.0.0.1:4723/wd/hub/status > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Appium server is running${NC}"
else
    echo -e "${YELLOW}Warning: Appium server is not running${NC}"
    echo "Starting Appium server..."
    appium > appium.log 2>&1 &
    APPIUM_PID=$!
    echo "Appium started with PID: $APPIUM_PID"
    sleep 5
    
    if curl -s http://127.0.0.1:4723/wd/hub/status > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Appium server started successfully${NC}"
    else
        echo -e "${RED}Error: Failed to start Appium server${NC}"
        exit 1
    fi
fi

# Parse command line arguments
TAG=${1:-"@AmazonShopping"}
echo ""
echo "Running tests with tag: $TAG"

# Clean and run tests
echo ""
echo "Cleaning previous build..."
mvn clean

echo ""
echo "Running tests..."
mvn test -Dcucumber.filter.tags="$TAG"

TEST_EXIT_CODE=$?

# Generate report
echo ""
echo "Test execution completed!"
if [ $TEST_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}✓ All tests passed${NC}"
else
    echo -e "${YELLOW}! Some tests failed or have issues${NC}"
fi

# Show report location
echo ""
echo "======================================"
echo "Test Reports Generated:"
echo "======================================"
echo "HTML Report: target/cucumber-reports/cucumber-html-report.html"
echo "JSON Report: target/cucumber-reports/cucumber.json"
echo "Logs: logs/automation.log"
echo ""

# Open HTML report (optional)
read -p "Do you want to open the HTML report? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    if [[ "$OSTYPE" == "darwin"* ]]; then
        open target/cucumber-reports/cucumber-html-report.html
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        xdg-open target/cucumber-reports/cucumber-html-report.html
    fi
fi

exit $TEST_EXIT_CODE
