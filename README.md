# Email sender application
This is a application is a implementation of socket listner server in java that accepts requests and sends emails messages using SMTP server. Also client is acknowledged with a response.

### **How to install**
1. ### Using prebuild jar files

You can download the generated jars in below location
>https://drive.google.com/drive/folders/1fY8cPMMB9XSPjD3TmF2WcZ7Rt924-Hxv?usp=sharing

* Run the FakeSMTP server
`java  -jar  fakeSMTP-2.0.jar`
use the default port to run SMTP server

* Run the server
`jar -jar email-server.jar`

* Run the client
`jar -jar -DTHREAD_COUNT=5 -DTOTAL_REQUESTS=30 email-client.jar`

2. ### Build from source

* Run the following commnd to generate jars
`mvn -DskipTests=true package`

*   Then follow the above instructions to run

### Import to eclipse

`File -> Import -> Maven -> Existing Maven Projects -> browse Directory with parent pom file`

### Additional JVM properties

* Mail server
````
    Thread count    :   -DTHREADS=5
    Default port    :   -DDEFAULT_PORT=9990
    SMTP address    :   -DSMTP_HOST_ADDRESS="127.0.0.1"
    SMTP port       :   -DSMTP_PORT=25
````
* Client
````
    Thread count        :   -DTHREAD_COUNT=5
    Total Request count :   -DTOTAL_REQUESTS=20
    Default sender      :   -DMAIL_SENDER="sender@gmail.com"
    Default reciever    :   -DMAIL_RECEIVER="reciever@gmail.com"
    Default port        :   -DPORT=9990
    Default address     :   -DSOCKET_ADDRESS="127.0.0.1"
````

### Troubleshooting


Console log level is set as ALL. So everything will be print on console
Also for SEVERE level logs will be wrote to a file as well.

file will be created in below path
`\logs\email-logger.log`

