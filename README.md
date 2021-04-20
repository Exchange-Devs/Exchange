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

- [x] User can sign up to create a new account using Parse authentication.
- [x] User can log in and log out of his or her account.
- [x] The current signed in user is persisted across app restarts.
- [ ] User can take a photo, add a description, and list it.
- [ ] User can use a search bar to search for materials.
- [x] User can view items that are listed.
- [ ] User can view detailed view of listed item.
- [x] User can view thier own listings.
- [ ] User can request availability of materials.
- [ ] User can create messages and communicate within the app.
- [ ] User can select college that they attend.
- [ ] User can filter by location, radius, delivery method, item condition, date listed, and category.

**Optional Nice-to-have Stories**

- [ ] Show the username and creation time for each post.
- [ ] After the user submits a new listing, show an indeterminate progress bar while being uploaded to Parse.
- [ ] Navigation animations.
- [ ] User can edit profile.
- [ ] User can filter listings.
- [ ] User can select between different payment methods if needed.

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
<img src= "https://github.com/Exchange-Devs/Exchange/blob/main/Exchange.png" width=600>

### Models

#### Listings

| Property  | Type            | Description                     |
| --------- | --------------- | ------------------------------- |
| objectId  | String          | unique id for the user listings |
| author    | Pointer to User | image author                    |
| image     | File            | image that user posts           |
| location  | String          | users general location          |
| createdAt | DateTime        | date when listing is created |
| exchangeOrSell     | Boolean         | user can choose to either sell or exchange| 

#### Messages

| Property  | Type            | Description                |
|---------  | --------------- | -------------------------- |
| messageId | String          | unique id for user message |
| time      | DateTime        | time message is created    |
| author    | Pointer to User | message author             |
| message   | String          | message sent from user     |

### Networking
- Home Screen
  - (Read/GET) Query all listings where user is author.
  ```swift
      ParseQuery<Listings> query = ParseQuery.getQuery(Listings.class);
            query.include(Listings.KEY_USER);
            query.setLimit(20);
            query.findInBackground(new FindCallback<Listings>() {
                @Override
                public void done(List<Listings> lists, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting listings", e);
                        return;
                    }
                    for (Listings list : lists) {
                        Log.i(TAG, "Listings " + list.getDescription() + "username: " + list.getUser().getUsername());
                    }

                    allListings.addAll(lists);
                    adapter.notifyDataSetChanged();
                }
            }); 
       ```
- Create listing Screen
  - (Create/LIST) Create a new list object.
- Profile Screen
  - (Update/PUT) Update user profile image
  - (Read/GET) Query logged in user object
  ```swift
      ParseQuery<Listings> query = ParseQuery.getQuery(Listings.class);
            query.include(Listings.KEY_USER);
            query.setLimit(limit);
            query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
            query.addDescendingOrder(Listings.KEY_CREATED_KEY);
            query.findInBackground(new FindCallback<Listings>()
            {
                @Override
                public void done(List<Listings> lists, ParseException e)
                {
                    if (e != null)
                    {
                        Log.e(TAG, "Issue with getting listings", e);
                        return;
                    }
                    for (Listings listings : listings)
                    {
                        Log.i(TAG, "Listings: " + listings.getDescription() + ", username: " + listings.getUser().getUsername());
                    }
                    allListings.addAll(listings);
                    adapter.notifyDataSetChanged();
                }
            });
         ```
         ## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://github.com/Exchange-Devs/Exchange/blob/main/Exchange%231.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

<img src='https://github.com/Exchange-Devs/Exchange/blob/main/Exchange%232.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

