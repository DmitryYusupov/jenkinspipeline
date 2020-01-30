package utils

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

final class XmlDomUtils {
    private XmlDomUtils(){

    }

    static Document getDocument(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        return documentBuilder.parse(file);
    }

    static Element getOnlyElementFromDocument(Document document, String tagName) {
        return (Element) document.getElementsByTagName(tagName).item(0);
    }

    static String getOnlyElementTextContent(Element elementSource, String tagName) {
        NodeList elementsByTagName = elementSource.getElementsByTagName(tagName);
        return elementsByTagName.length > 0 ? elementsByTagName.item(0).getTextContent() : null
    }

    static Element getOnlyElement(Element elementSource, String tagName) {
        def children = elementSource.getElementsByTagName(tagName)
        return children.getLength() > 0 ? (Element) children.item(0) : null
    }

}
