#this should run the app locally under port 5000
sbt compile stage
heroku local web

echo "Try it out: http://localhost:5000"