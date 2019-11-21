# Apidaze Java SDK

# Requirements
JDK 1.8 or higher

# Installation

## Using Maven
- Clone the code repository
 
    `git clone https://github.com/apidaze/sdk-java.git`
    
- Go to the folder with the cloned repository
 
    `cd sdk-java`
    
- Install artifacts in the local Maven repository
  
    `mvn clean install`
    
- Add the following dependency to your project if you want to make calls, send text messages, etc...
       
```xml
        <dependency>
          <groupId>com.apidaze.sdk</groupId>
          <artifactId>apidaze-java-client</artifactId>
          <version>1.0.0-beta</version>         
       </dependency>
```
    
- Add the following dependency to your project if you want to build external scripts
   
```xml
        <dependency>
          <groupId>com.apidaze.sdk</groupId>
          <artifactId>apidaze-scripts-builders</artifactId>
          <version>1.0.0-beta</version>         
       </dependency>
```
     

## Without Maven

- Download jar files from the latest [release](https://github.com/apidaze/sdk-java/releases)

- Add **apidaze-java-client-1.X.X.jar** and the following files (downloaded from [maven](https://search.maven.org/) to your application classpath if you want to make calls, send text messages, etc...  
    
    | file                                        | groupId                         | artifactId              | version
    | ------------------------------------------- | --------------------------------|-------------------------|--------
    | commons-validator-1.6.jar                   | commons-validator               | commons-validator       | 1.6
    | jackson-annotations-2.10.0.jar              | com.fasterxml.jackson.core      | jackson-annotations     | 2.10.0
    | jackson-core-2.10.0.jar                     | com.fasterxml.jackson.core      | jackson-core            | 2.10.0
    | jackson-databind-2.10.0.jar                 | com.fasterxml.jackson.core      | jackson-databind        | 2.10.0
    | jackson-datatype-jdk8-2.10.0.jar            | com.fasterxml.jackson.datatype  | jackson-datatype-jdk8   | 2.10.0
    | jackson-datatype-jsr310-2.10.0.jar          | com.fasterxml.jackson.datatype  | jackson-datatype-jsr310 | 2.10.0
    | okhttp-4.2.2.jar                            | com.squareup.okhttp3            | okhttp                  | 4.2.2
    | okio-2.2.2.jar                              | com.squareup.okio               | okio                    | 2.2.2
    | kotlin-stdlib-1.3.50.jar                    | org.jetbrains.kotlin            | kotlin-stdlib           | 1.3.50
    | guava-28.1-jre.jar                          | com.google.guava                | guava                   | 28.1-jre
     
- Add **apidaze-scripts-builders-1.X.X.jar** and the following files (downloaded from [maven](https://search.maven.org/))to your application classpath if you want to build external scripts

    | file                                        | groupId                         | artifactId                       | version
    | ------------------------------------------- | --------------------------------|----------------------------------|--------
    | jackson-annotations-2.10.0.jar              | com.fasterxml.jackson.core      | jackson-annotations              | 2.10.0
    | jackson-core-2.10.0.jar                     | com.fasterxml.jackson.core      | jackson-core                     | 2.10.0
    | jackson-databind-2.10.0.jar                 | com.fasterxml.jackson.core      | jackson-databind                 | 2.10.0
    | jackson-dataformat-xml-2.10.0.jar           | com.fasterxml.jackson.dataformat| jackson-dataformat-xml           | 2.10.0
    | jackson-module-jaxb-annotations-2.10.0.jar  | com.fasterxml.jackson.module    | jackson-module-jaxb-annotations  | 2.10.0
    | stax2-api-4.2.jar                           | org.codehaus.woodstox           | stax2-api                        | 4.2
    | woodstox-core-6.0.1.jar                     | com.fasterxml.woodstox          | woodstox-core                    | 6.0.1
    
# Quickstart

## SDK client

### Initiate ApplicationAction

```java
Credentials credentials = new Credentials(apiKey, apiSecret);
ApplicationAction applicationAction = ApplicationAction.create(credentials);
```

### Make a call

```java
String callId = applicationAction.createCall(
    PhoneNumber.of("14123456789"),  // The phone number to present as the caller id
    "14123456789",                  // The phone number or SIP account to ring first
    "14123456789"                   // The destination passed as a parameter to your External Script URL
);
```

### Send a text message

```java
applicationAction.sendTextMessage(
    PhoneNumber.of("14123456789"),  // The number to send the text from
    PhoneNumber.of("14123456789"),  // The destination number
    "Have a nice day!"              // The text message to send
);
```

### Download recordings


## Scripts builders


# Examples


