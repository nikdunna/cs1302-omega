# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the app
> category that you chose, and the primary functions available to users
> of the app.

> **Also, include the GitHub `https` URL to your repository.**

I chose the API call application category. My application takes the user's birthdate
and birthplace and, using two RESTful APIs, produces a nicely formatted image of the moon on that day.
The most interesting part of my app is how the user can choose any day(even in the 0001-1000 years) and will still
be given an accurate look at the moon on that day. It is also very lenient with the user's input for birthplace,
as cities and city abbreviations work. The application is able to run in this way because of the chaining of
a GET call to the PositionStack API to retrieve the coordinates of the inputted city and a POST call
to the AstronomyAPI which returns a URL of a generated moon graphic. The url to my GitHub repository for this
project can be found here: https://github.com/attavex/cs1302-omega

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

While in class, I only learned about getting JSON information through the HttpClient using GET requests. Being
able to implement a POST request in this application was very challenging but exciting. It required me to create
classes and objects to perfectly match the JSON information that the AstronomyAPI needed, and then work backwards:
serializing JSON instead of deserialzing it.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

If I could start the project over, I would reduce some of the complexity of my application by creating more helper
methods. While I think that I did a decent job at keeping my code organized, it can always be better. I would
also be interested in using one of the NASA APIs as they seem very interesting and exciting to learn to use.
