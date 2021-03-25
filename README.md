# Exchange

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Exchange will allow college students to post and look for items that other students have and comunicate what they are willing to exchange.

### App Evaluation
- **Category:** Education, E-Commerce.
- **Mobile:** Uses Camera, Location, and Messaging.
- **Story:** Allows users to post what they think what someone may want to exchange.
- **Market:** College students will use this app in order echange for college materials.
- **Habit:** Users can post college items anytime they have an item available.The user will keep returning see what else might be on the app to exchange.
- **Scope:** It will be a fulled screen app with different section where users will be able to post a school related item, change their profile, be in communicaion with the community.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can sign up to create a new account using Parse authentication.
* User can log in and log out of his or her account.
* The current signed in user is persisted across app restarts.
* User can take a photo, add a description, and list it.
* User can use a search bar to search for materials.
* User can view items that are listed.
* User can view detailed view of listed item.
* User can view thier listings.
* User can request availability of materials.
* User can create messages and comunicate within the app.
* User can select college that they attend.
* User can filter by location, radius, delivery method, item condition, date listed, and category.

**Optional Nice-to-have Stories**

* Show the username and creation time for each post.
* After the user submits a new listing, show an indeterminate progress bar while being uploaded to Parse.
* Navigation animations.
* User can edit profile.
* User can filter listings.
* User can select between different payment methods.

### 2. Screen Archetypes

* Login Screen
   * User can log in with email and password.
* Sign Up
    * User can sign up to create a new account using Parse authentication.
    * User can select college that they attend.
* Home Screen / Stream
   * User can use a search bar to search for materials.
   * Users can view items that are listed.
   * User can filter by location, radius, delivery method, item condition, date listed and category.
* Detail Screen
   * User can view detailed view of listed item.
   * User can request availability of materials.
* Compose Screen
   * User can take a photo, add a description, and list it.
* Profile Screen
   * User can view profile and thier own listings.
   * User can create messages and comunicate within the app.
* Profile Detailed Screen
   * User can edit profile and description (location) etc.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
  * Search bar
  * Detail Screen
* Compose
* Profile

**Flow Navigation** (Screen to Screen)

* Login Screen
    * Home Screen
* Sign Up
    * Login Screen.
    * Home screen
* Home Screen / Stream
   * Detail
* Detail Screen
   * Home Screen.
* Compose Screen
   * Home Screen.
* Profile Screen
   * Detailed Profile Screen(edit).
   * Profile Screen.

## Wireframes
<img src="https://github.com/Exchange-Devs/Exchange/blob/main/Exchange.png" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
