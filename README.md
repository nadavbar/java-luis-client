# java-luis-client
A Java client for Microsoft's [LUIS](https://www.luis.ai), including an object model for the query result.

## Dependecies:
com.fasterxml.jackson.core:jackson-annotations
com.fasterxml.jackson.core:jackson-core
jackson-databind

## Usage example:

```java
LUISClient client = new LUISClient(LUIS_APP_ID, LUIS_SUBSCRIPTION_ID);
LUISResult result = client.queryLUIS("show me the weather");
System.out.println(result.getIntents()[0].getIntent());
```


