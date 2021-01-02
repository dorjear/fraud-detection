# fraud-detection

Consider the following credit card fraud detection algorithm: 
A credit card transaction comprises the following elements.
hashed credit card number, timestamp - of format year-month-dayThour:minute:second, amount - of format dollars.cent

Transactions are to be received in a file as a comma separated string of elements, one per line, eg:
`10d7ce2f43e35fa57d1bbf8b1e2,2014-04-29T13:15:54,10.00`

The file contains a sequence of transactions in chronological order.

A credit card will be identified as fraudulent if the sum of amounts for a unique hashed credit card number over a 24-hour sliding window period exceeds the price threshold.

This is a command line application which takes a price threshold argument and a filename, eg:

`java -jar build/libs/fraud-detection-1.0-SNAPSHOT.jar 100 src/test/resources/transactions.csv`

To build this application, run `./gradlew clean build` and the jar file will be in ./build/libs/fraud-detection-1.0-SNAPSHOT.jar

This version is reactive with Flux provided by projectreactor. 
This version runs in multi-thread mode. With current data set, overhead is greater than the benefit. The running time is longer than the single thread version. 
This version will have better performance when the file is in GB level. 