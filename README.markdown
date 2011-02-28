# CS4532 Class Project: Karma code

This is the source code edits log for our CS4532 class project at the Naval Postgraduate School.  The project is titled [*Karma*](https://github.com/shuchton/Karma).  For more information, contact [Scott Huchton](https://github.com/shuchton) or [Kevin LaFrenier](https://github.com/kjlafrenier).

#February 27, 2011
Did a lot of work on this today.  The Login function works now.  Basically the user enters a username and password and the login button checks to see if the credentials return the logged in version of the page or not.  It might be a hack, but I can't think of a better way to do it using basic http auth.

Now that I know the REST Client is working, I can also get the events from the server and parse them into our list.  Most of the code additions today were to get those lists working.

#February 2, 2011
Initial commit has just a single login form and no functionality.

Todo:  
*	Get the login function to work with the server