# XpensAuditor
a smart way to track your expenses

[![Code Coverage](https://codecov.io/gh/AtharvaGole/XpensAuditor/branch/main/graphs/badge.svg)](https://codecov.io/gh/AtharvaGole/XpensAuditor/branch/main)
[![DOI](https://zenodo.org/badge/543328644.svg)](https://zenodo.org/badge/latestdoi/543328644)
[![Collaborators](https://img.shields.io/badge/Collaborators-5-orange.svg?style=flat)](https://github.com/AtharvaGole/XpensAuditor/graphs/contributors)
[![License](https://img.shields.io/badge/License-MIT-purple.svg?style=flat)](https://github.com/AtharvaGole/XpensAuditor/blob/main/LICENSE)
[![Language](https://img.shields.io/badge/Language-Java-blue.svg?style=flat)](https://github.com/AtharvaGole/XpensAuditor/search?l=java)
[![Documentation Status](https://readthedocs.org/projects/ansicolortags/badge/?version=latest)](https://github.com/AtharvaGole/XpensAuditor/blob/main/README.md)
[![GitHub Release](https://img.shields.io/github/release/AtharvaGole/XpensAuditor.svg)](https://github.com/AtharvaGole/XpensAuditor/releases)
[![Open Issues](https://img.shields.io/github/issues/AtharvaGole/XpensAuditor)](https://github.com/AtharvaGole/XpensAuditor/issues)
[![Build-And-Test](https://github.com/AtharvaGole/XpensAuditor/actions/workflows/android.yml/badge.svg)](https://github.com/AtharvaGole/XpensAuditor/actions/workflows/android.yml)
[![GitHub Repo Size](https://img.shields.io/github/repo-size/AtharvaGole/XpensAuditor.svg)](https://img.shields.io/github/repo-size/AtharvaGole/XpensAuditor.svg)

#

 ## Summary
 
 - This mobile application allows customers to add their expenses and keep track of them. 
 - Takes required minimal amount of data like date of transaction, product name and value
 - Each user needs to create/sign up to access the application, Firebase is used to achieve authorization, authentication and accounting
 - The application keeps track of user data and stores it in the Firebase realtime database
 - The app supports multiple interesting features like Rating, Customer Service Feedback, Contact Us
 - The Mailing and SMS services have been configured for the app for ease of sending customer updates, password reset requests and reading personal expenses 
 - Profile set up module is where user can update their details 
 - Account Settings are available to change password, send password reset email

## Demo

https://user-images.githubusercontent.com/112219214/194787499-0125447e-f68d-444b-9a86-559b14adf898.mp4


## Roadmap

 - Issues encountered and solved so far - [ISSUES](https://github.com/SaiPavanYalla/XpensAuditor/issues?q=is%3Aissue+is%3Aclosed)
 - Scope of Improvement :
 
   - Analysis on detected expenses - Category wise expenses and expenditure graphs
   - Detect transaction alerts from notifications and emails as well
   - Add custom categories & Auto detect category by vendor
   - Change password feature is implemented. Likewise, change email also needs to be implemented
 

## License

 This project is licensed under the MIT License. See the [LICENSE](https://github.com/SaiPavanYalla/XpensAuditor/blob/main/LICENSE) file for details
 
## Tools used

- Android Studio
- Embedded Emulator/ USB debugging on android device

## Run the application
### Android Mobile
 - Download the apk from the latest [release](https://github.com/AtharvaGole/XpensAuditor/releases/tag/v1.0.0) 
 
### Android Studio
 - Must have android studio installed
 - Clone the github repo
   git clone https://github.com/AtharvaGole/XpensAuditor.git
 - Open the cloned repo using android studio
 - Build the application for any dependency inconsistencies and proper functioning of app
 - Expand the file structure and run the app using "Run" option
 
## Test the application

 - Application has an existing test suite
 - Navigate to the folder of Android Test and right click and select "Run tests in ...."
 - It runs all the test cases in the package and provides the result and code coverage

## Project Management
https://www.notion.so/SE-Project-2-3a1517a6d8aa42b590b1f31a288249ce

## Kanban Board
https://www.notion.so/525c9918752e452cade1c9773fb22a02?v=bbac6938b19a42798e6a6f7382504559
 
## Team Members:

This repository is made for CSC 510 Software Engineering Course:

Members
 - Atharva Gole
 - Arnab Datta
 - Vishal Sharma
 - Dhanya Dasari
 - Dakshil Kanakia

Previous Contributors
 - Sahithi Ammana
 - Mithila Reddy Tatigotla
 - Sunandini Medisetti
 - Sai Pavan Yalla
 - Vineeth Dasi

