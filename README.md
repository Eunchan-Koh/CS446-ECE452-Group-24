# CS446/CS646/ECE452-Group-24: MealMates
**Team members:**   
| Name | QuestID |
| --- | --- |
| Ali, Samir | s295ali |  
| Koh, Eunchan | e2koh |
| Lim Ah Tock, Layne | mllimaht |
| Wang, Jason | j2689wan |
| Zhu, Vivian | v2zhu |

# Description

Have you and your friends ever found yourself struggling with picking where to eat? Someone likes italian, someone doesn't, but another person may only be vegetarian. It can be difficult and time-consuming scrolling through the internet, looking for the perfect restaurant that fits everyones' interests. Our application has streamlined this process. Just create a group, add your friends, and start looking for restaurants now!

# Instructions on Installing/Building the App:

* The following tools will be needed:
  * Android Studio (Latest version is fine)
    * Standard Installation
    * Do not import settings
    * Install everything (Agree to all Fine Print)
  * JDK 18
  * Gradle (latest version is fine)
* Steps:
  1. Open MealMates folder in root of project via Android Studio, this is not the root of the repository.
  2. Wait a few minutes to allow for the gradle build to happen, you'll know it is done when the green play button is available.
  3. You can click play to run the app on the top bar of Android Studio. Alternatively use Shift + F10 (Windows), Control + R (macOS).
  4. The app should open on the right, if it doesn't then please click on "Running Devices" on the right bar of Android Studio and the app should appear.
  5. (Not to worry, this step will never occur!) If all else fails, contact s295ali@uwaterloo.ca.

**Specified Device: Please use the Medium Phone API 34 with the following specifications to reflect the most accurate experience as intended**

![image](https://github.com/Samir2003/RemindMed/assets/59757852/8667789e-55c1-409e-8987-850f12f8d020)

**Some Notes for Testing:**
* The backend is hosted on Google Cloud which makes pods go inactive if no request has been made in the last hour. Due to this, initial sign-in may take some time (~5-10 seconds). You will sign-in successfully and it will go back to the screen with the logo and "Begin" button. Just wait, don't click anything here because the pod is being woken up. This is done by Google Cloud to conserve resources and reduce credit spent on uptime.
* We already have many test users and one in particular which can be used for testing which is the most populated is: 
<br />**Email: samirhas49@gmail.com <br /> password: JeffAvery1!**

# Features/User Flows

**Users:**
* Login [2:13]
* Sign-up -> Select Preferences -> Select Location [0:05 - 0:40]
* User Profile Management (Edit user data) [0:45]

**Groups:**
* Create Group -> Add Members, Group Name, Group Location [0:55]
* Group -> List of Matches -> Match Info (Restaurants for that Match) [2:40]
* Group Profile Management (edit group data) [1:35]
* Group -> Start Match/ Resume Match [1:53, 2:25]

**Matching/Restaurants:**
* Like/Dislike Restaurant [2:00, 2:28]
* Swipeable Restaurant Photos [1:58, 2:26]

# Demo Video

**All above features will be displayed in the video below and their timestamp is mentioned.**

[![MealMates Demo Video](https://img.youtube.com/vi/kPbHTOw2wwM/0.jpg)](https://www.youtube.com/watch?v=kPbHTOw2wwM)