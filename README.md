# query-finder
Get Document with maximum matches to a query

#Overview
Code has been written completely in Java. Basic HTTP servlets have been used to create the Web Application.
The api /kredex/Home?query=<tokens> takes in comma separated values and returns the result.
The data structures used are loaded whenever the server is started.

#logic
Each document was assigned a documentId and DocumentId ~ Document relation was stored in a map.
The score of each document was stored in a seperate map to enable to break ties fast.
A Set of doccumentIds was stored with each token which appeared in the text. The tokens were all processes such that they consisted only of lower case alpha numeric characters.

On taking an input query, the count of tokens matching each document was taken. The top 20 documents with maximum count are returned as result.Only documents with non-zero matches are returned.

#Data set
The sample data set (foods-sample.txt) consisted 56773 entries randomly sampled from the original dataset.
100000 queries were generated randomly for the load test (query-sample.csv)
The code for generating both the data sets has been included at the end of the DataLoader.java file.
The data sets used are present in the resources directory.

#instructions for running
Checkout the repo in a local folder. 
Using IDE:
    point your IDE to the pom.xml
    goto plugins->jetty->jetty:run to run the server

From CommandLine:
    Method 1:
    run 'mvn package' on the root folder to create a war file 
    The war file can then be run using tomacat or any other container.
    Method 2:
    use 'mvn jetty:run' on the root folder
        
go to localhost:8080/kredex/Home/home.jsp
Enter your comma separated query and click on submit to get the result.








