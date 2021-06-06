## JSONXMLConverter


Steps to follow:

Install Maven

To run the Maven command from command prompt.
Navigate to the project folder and Open command prompt and run the below maven command to build the jar file
		
	mvn clean package
		
To run the jar open up command prompt and use below command with updated jar, input json and output xml file paths.

	java -jar "C:\Users\eclipseworkspace\JsonToXmlConverter.jar" "C:\Users\input\example.json" "C:\Users\input\output.xml"