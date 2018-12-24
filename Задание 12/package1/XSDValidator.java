//http://java-course.ru/begin/xml/
package package1;

import java.io.*;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import org.xml.sax.SAXException;

public class XSDValidator
{
    public static void validateXMLSchema(String xsdPath, String xmlPath) throws SAXException
    {
        try {
			
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			
            Schema schema = factory.newSchema(new File(xsdPath));
			
            Validator validator = schema.newValidator();
			
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
