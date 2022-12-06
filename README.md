# XpensAuditor
a smart way to track your expenses

[![Code Coverage](https://codecov.io/gh/AtharvaGole/XpensAuditor/branch/main/graphs/badge.svg)](https://codecov.io/gh/AtharvaGole/XpensAuditor/branch/main)
[![DOI](https://zenodo.org/badge/543328644.svg)](https://zenodo.org/badge/latestdoi/543328644)
[![Collaborators](https://img.shields.io/badge/Collaborators-10-orange.svg?style=flat)](https://github.com/AtharvaGole/XpensAuditor/graphs/contributors)
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
 
 ## New Features
 
 - Ability to create Groups and track Group expenses
  ![Page4](https://user-images.githubusercontent.com/44353511/205784651-950673a8-49c8-47e9-bbe8-96546964af48.png)
  ![Page5](https://user-images.githubusercontent.com/44353511/205784767-19919eba-3f74-42db-8618-59415223c424.png)
 - Analytics for Users and Groups
  ![Page7](https://user-images.githubusercontent.com/44353511/205784840-12bac317-eb2b-4d14-b388-40b35326b386.png)
 - Push Notifications when Group Transactions are created
  ![Page6](https://user-images.githubusercontent.com/44353511/205784815-2160e6ed-2cf8-4c31-941b-8d1ff74f4066.png)
 - Sorting Transactions by Date, Amount, Category and Memo
  ![Page3](https://user-images.githubusercontent.com/44353511/205784539-4f16c5d7-0993-4a69-947a-6c00952179fe.png)
 - Ability to view past transactions of previous months
 - Ability to create custom categories
  ![Page2](https://user-images.githubusercontent.com/44353511/205784340-ad0c2668-3fbc-4aa5-9f15-4e06563987cb.png)
 - UI refresh
 

## Demo

[Link to Demo Video](https://drive.google.com/file/d/1uOYYxBSlLLNwlBKz2-shEShyU4YVj-L6/view?usp=sharing)
## System Architecture
<img width="968" alt="Note Dec 5, 2022 png" src="https://user-images.githubusercontent.com/44353511/205783646-0a733c40-00b9-46a8-ab2c-9285e138088d.png">

## Scalability
Currently the application stores User and Group data on Firebase Real-Time Database. Every smartphone with the app running needs to establish a connection to the database in order to sync data with the cloud. The app will run even without an internet connection, but in order to view the latest transactions of a group, you will need to query data from the database. The free version of Firebase Real-Time Database can support up to 100 concurrent connections, but this can easily be scaled up if we choose to go for a paid subscription from Firebase. The number of concurrent users in a Group can be scaled up to 200,000 per database easily without any impact on the performance of the app. 

You can see a list of the paid plans at Firebase below - https://firebase.google.com/pricing?authuser=3&hl=en 

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

