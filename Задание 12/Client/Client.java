package Client;

import Server.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static package1.XSDValidator.validateXMLSchema;

public class Client {

    private static final int port = Server.PORT;
    @SuppressWarnings("WeakerAccess")
    public static final String messagePath = Server.messagePath, messageName = "message.xml", schemaPath = "schema.xsd";
    private static Socket clientSocket;
    private static BufferedReader clientConsole;
    private static BufferedReader in;
    private static BufferedWriter out;


    public static void main(String[] args) {
        if (args.length == 1)
            new Client(args[0]);
        else
            new Client("localhost");
    }

    private Client(String serverAddress) {
        try {
            clientSocket = new Socket(serverAddress, port);
            clientConsole = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            new ReadMsg().start();
            new WriteMsg().start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private class ReadMsg extends Thread {

        @Override
        public void run() {
            String msg;
            while (true) {
                try {
                    if (clientSocket.isClosed()) break;
                    msg = in.readLine();

                    try {
                        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        String filePath = Server.serverMessagePath + msg;
                        Document doc = docBuilder.parse(new File(filePath));

                        validateXMLSchema(schemaPath, filePath);
                        Node root = doc.getDocumentElement();
                        Node message = root.getFirstChild();
                        msg = message.getTextContent();

                    } catch (ParserConfigurationException | IOException e) {
                        e.printStackTrace(System.out);
                    } catch (SAXException e) {
                        System.out.println("Validate exception");
                        e.printStackTrace(System.out);
                    }

                    switch (msg) {
                        case Server.quitMsg: {
                            in.close();
                            if (!clientSocket.isClosed())
                                clientSocket.close();
                            System.out.println("You've been disconnected form server");
                            break;
                        }
                    }

                    System.out.println(msg);
                } catch (SocketException ignored) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Session is closed");
        }
    }

    private class WriteMsg extends Thread {

        @Override
        public void run() {
            String msg;
            while (true) {
                try {
                    if (clientSocket.isClosed()) break;
                    msg = clientConsole.readLine();

                    try {
                        XMLOutputFactory output = XMLOutputFactory.newInstance();
                        XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter(messagePath + messageName));
                        writer.writeStartDocument("1.0");
                        writer.writeCharacters("\n");
                        writer.writeStartElement("line");
                        writer.writeCharacters(msg);
                        writer.writeEndElement();
                        writer.writeEndDocument();
                        writer.flush();
                    } catch (IOException | XMLStreamException ex) {
                        ex.printStackTrace(System.out);
                    }

                    if (msg.equals(Server.quitMsg)) {
                        System.out.println("Disconnecting form server");
                        out.write(messageName + '\n');
                        out.flush();
                        out.close();
                        clientConsole.close();
                        break;
                    }

                    if (!msg.equals(""))
                        out.write(messageName + '\n');
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}