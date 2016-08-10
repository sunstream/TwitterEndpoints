# TwitterEndpoints
You will need to verify that given methods are working as described in documentation (https://dev.twitter.com/rest/public) using Groovy or Java. Note that you should use OAuth 1.0 authentication to be able to send any request to Twitter.

Endpoints that should be tested:
GET https://dev.twitter.com/rest/reference/get/statuses/user_timeline
POST https://dev.twitter.com/rest/reference/post/statuses/update
GET https://dev.twitter.com/rest/reference/get/direct_messages (optional)
POST https://dev.twitter.com/rest/reference/post/direct_messages/new (optional)
POST https://dev.twitter.com/rest/reference/post/account/update_profile (optional)
Methods marked as 'optional' can be used if the task is too easy for you.

When the task will be done please upload your code to GitHub (or Tutorialspoint) and send us link to it.
Tips:
- Testing framework: Spock/Junit/TestNG
- Use both positive and negative verifications
- Please add some sort of logging to your tests
- It is highly recommended to use build tool(Maven/Gradle) in your project
- KISS principles. E.g. use third-party libraries to do trivial tasks (parse JSON, HTTP requests etc.)
- It's OK if test finds bug
