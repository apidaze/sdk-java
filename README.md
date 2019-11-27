# Apidaze Java SDK

The Apidaze Java SDK contains Java client of Apidaze REST API as well as XML scripts builders.
The SDK allows you to leverage all Apidaze platform features such as making calls, sending text messages, serving IVR systems and many others in your Java based application.
The SDK also includes sample applications that demonstrate how to use the SDK interfaces.
See [Apidaze REST API specification](https://apidocs.voipinnovations.com) which includes XML Scripting Reference as well.

# Requirements
JDK 1.8+

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
  <version>1.0.0-beta.1</version>         
</dependency>
```
    
- Add the following dependency to your project if you want to build external scripts
   
```xml
<dependency>
  <groupId>com.apidaze.sdk</groupId>
  <artifactId>apidaze-scripts-builders</artifactId>
  <version>1.0.0-beta.1</version>         
</dependency>
```
     

## Without Maven

- Download jar files from the latest [release](https://github.com/apidaze/sdk-java/releases)

- Add **apidaze-java-client-1.X.X.jar** and the following files (downloaded from [maven](https://search.maven.org/)) to your application classpath if you want to make calls, send text messages, etc...  
    
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
     
- Add **apidaze-scripts-builders-1.X.X.jar** and the following files (downloaded from [maven](https://search.maven.org/)) to your application classpath if you want to build external scripts

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

### Initiate an ApplicationAction
To execute any action such as making a call or send text message you need to initiate an **ApplicationAction** first. 

#### Root application
You can initiate an ApplicationAction using credentials assigned to your application 
```java
import com.apidaze.sdk.client.base.Credentials
import com.apidaze.sdk.client.ApplicationAction
 
// use api_key and api_secret assigned to your Apidaze application
Credentials credentials = new Credentials(apiKey, apiSecret);
ApplicationAction applicationAction = ApplicationAction.create(credentials);
```
#### Sub-applications
Optionally if you have created sub-applications you can use *ApplicationManager* to initiate *ApplicationAction* by using on of the following methods
```java
import com.apidaze.sdk.client.base.Credentials
import com.apidaze.sdk.client.ApplicationAction
import com.apidaze.sdk.client.ApplicationManager

// use root api_key and api_secret assigned to your Apidaze account
ApplicationManager applicationManager = ApplicationManager.create(new Credentials(apiKey, apiSecret));

// get ApplicationAction by id 
ApplicationAction applicationAction = applicationManager.getApplicationActionById(1L);

// get ApplicationAction by api_key assigned to sub-application
//ApplicationAction applicationAction = applicationManager.getApplicationActionByApiKey("n8fetkvn");

// get ApplicationAction by sub-application name 
//ApplicationAction applicationAction = applicationManager.getApplicationActionByName("MY APPLICATION");
```

### Make a call

Use **applicationAction** object from step [Initiate an ApplicationAction](#initiate-an-applicationaction)
 
```java
String callId = applicationAction.createCall(
    PhoneNumber.of("14123456789"),  // The phone number to present as the caller id
    "14987654321",                  // The phone number or SIP account to ring first
    "14987654321"                   // The destination passed as a parameter to your External Script URL
);
```

### Send a text message

```java
applicationAction.sendTextMessage(
    PhoneNumber.of("14123456789"),  // The number to send the text from
    PhoneNumber.of("14987654321"),  // The destination number
    "Have a nice day!"              // The text message to send
);
```

### Download recordings

#### Keep the original file name
In this case the file will be saved with the original name (*example.wav*) to the local directory *foo*.
```java
String sourceFileName = "example.wav";
Path targetDir = Paths.get("foo"); 
File downloadedFile1 = applicationAction.downloadRecordingToFile(sourceFileName, targetDir);
```

#### Save the file with a new name 
In this case the file will be saved with the new name (*cool-example.wav*) to the local directory *foo*.
```java
String sourceFileName = "example.wav";
Path targetFile = Paths.get("foo/cool-example.wav");
File downloadedFile2 = applicationAction.downloadRecordingToFile(sourceFileName, targetFile);
```

### More examples
More examples are [here](https://github.com/apidaze/sdk-java/tree/master/examples/src/main/java/com/apidaze/sdk/examples) .

## Scripts builders

Scripts builders are used to build XML instructions described in [XML Scripting Reference](https://apidocs.voipinnovations.com).
To build an instruction which echo back received audio to the caller with some delay use the following code.
```java
ApidazeScript script = ApidazeScript.builder()
    .node(new Answer())
    .node(Echo.withDelay(Duration.ofMillis(500)))
    .build();

String xml = script.toXmlWithPrettyPrinter();
``` 
The content of produced xml is as follow.
```xml
<document>
  <work>
    <answer/>
    <echo>500</echo>
  </work>
</document>
``` 

For more examples see [unit tests](https://github.com/apidaze/sdk-java/tree/master/scripts-builders/src/test/java/com/apidaze/sdk/xml)

Sample applications with real life scenarios (i.e. IVR demo) are [here](https://github.com/apidaze/sdk-java/tree/master/examples/src/main/java/com/apidaze/sdk/examples/xml)