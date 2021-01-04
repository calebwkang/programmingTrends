# Programming Trends

## Overview
This is a simple app the uses the GitHub jobs api to inform users of trends in programming jobs. It tells the percentages of jobs in certain city refer to a certain programming languages


## Challenges: 
Some of the challenges I ran into were parsing the GET result from the API to be a JSON object, getting the constraints to properly work so that LinearLayout that I used didn't go off the screen and that all TextViews were being fully displayed, and handling the GET responses asynchronously. 

## Area's of code that I am most proud of: 
I am most proud of coding the makeRequest method to make BOTH types of GET requests: one with just the city param and one with both city and description params. This allowed for some reusing of code without having to make multiple types of methods for specific requests.

## Area of code that I am least proud of: 
I think this has to be not using thread coordination tactics. This would have made code cleaner and would update the UI in a much cleaner and normal way. I would have also used thread coodination tactics in the unit tests as well for the same reasons and to make more effective tests.

## TODOs to move beyond MVP:
Incorporate thread coordination. I think this will also give the ability to displayed sorted trend percentages. Maybe have a Map visual with the abliity to tap on different cities to display programming trends. Another step further would be to maybe be able to easy compare trends of 2 or more cities, put them side by side


