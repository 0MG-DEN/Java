package Server;

import Client.Client;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import package1.TicTacToe;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.*;

import static package1.XSDValidator.*;

public class Server {
    private static TicTacToe game;

    public static final int PORT = 3009;
    @SuppressWarnings("WeakerAccess")
    public static final String quitMsg = "/quit", messagePath = "",
            serverMessageName = "ServerMessage.xml", serverMessagePath = "";
    private static ServerClient clientsList[] = new ServerClient[2];

    public static void main(String[] args) {
        try {
            //ServerClient.createServerMessage("");
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started");
            try {
                game = new TicTacToe(Integer.parseInt(args[0]));
            } catch (Exception e) {
                System.out.println("Unable to create a game (invalid parameter)");
                e.printStackTrace();
                server.close();
                System.exit(-1);
            }

            for (int i = 0; i < Server.clientsList.length; i++) {
                Socket socket = server.accept();
                System.out.println("Player" + ServerClient.nextID + " connects");
                try {
                    clientsList[i] = new ServerClient(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                    socket.close();
                }
            }
            System.out.println("Reached maximum number of clients\nClosing server's socket");
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (ServerClient sc : Server.clientsList)
                sc.sendToThis(game.currentState());
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ServerClient extends Thread {
        private static int nextID = 1;

        private final int ID;
        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;

        private ServerClient(Socket inSocket) throws IOException {
            ID = nextID++;
            socket = inSocket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Player" + ID + "\'s input/output streams initialized.");
            start();
        }

        @Override
        public void run() {
            sendToThis("<Server>: You're Player" + ID + ". To exit this session enter \"" + Server.quitMsg + '\"');
            try {
                while (true) {
                    String msg;
                    try {
                        if (game.getCurrentPlayer() == this.ID && game.isOn())
                            sendToThis("<Server>: Your turn, enter position:");
                        msg = in.readLine();

                        try {
                            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            String filePath = Client.messagePath + msg;
                            Document doc = docBuilder.parse(new File(filePath));

                            validateXMLSchema(Client.schemaPath, filePath);
                            Node root = doc.getDocumentElement();
                            Node message = root.getFirstChild();
                            msg = message.getTextContent();

                        } catch (ParserConfigurationException | IOException e) {
                            e.printStackTrace(System.out);
                        } catch (SAXException e) {
                            System.out.println("Validate exception");
                            e.printStackTrace(System.out);
                        }

                        if (msg.equals(Server.quitMsg)) {
                            sendToAll("<Server>: Player" + ID + " quits");
                            sendToThis(Server.quitMsg);
                            this.quitSession();
                            System.out.println("Player" + ID + " disconnected");
                            break;
                        }
                        sendToAll("<Player" + ID + ">: " + msg);

                        if (game.getCurrentPlayer() == this.ID && game.isOn()) {
                            try {
                                if (game.takePosition(Integer.parseInt(msg))) {
                                    sendToAll(game.currentState());
                                    sendToThis(game.currentState());
                                }
                            } catch (TicTacToe.GameException e) {
                                sendToThis("<Server>: " + e.getMessage());
                            } catch (NumberFormatException ignored) {

                            }

                            try {
                                game.checkGrid();
                            } catch (TicTacToe.GameException e) {
                                sendToAll("<Server>: " + e.getMessage());
                                sendToThis("<Server>: " + e.getMessage());

                                sendToAll("<Server>: Game is over. Enter \"" + Server.quitMsg + "\" to exit");
                                sendToThis("<Server>: Game is over. Enter \"" + Server.quitMsg + "\" to exit");
                            }
                        }
                    } catch (IOException ignored) {
                    }
                }
            } catch (NullPointerException ignored) {

            }
        }

        private void quitSession() {
            try {
                socket.close();
                in.close();
                out.flush();
                out.close();
                this.interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendToAll(String msg) {
            createServerMessage(msg);
            for (ServerClient sc : Server.clientsList) {
                if (sc != this)
                    try {
                        sc.out.write(serverMessageName + '\n');
                        sc.out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void sendToThis(String msg) {
            createServerMessage(msg);
            for (ServerClient sc : Server.clientsList) {
                if (sc == this)
                    try {
                        sc.out.write(serverMessageName + '\n');
                        sc.out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private static void createServerMessage(String msg) {
            try {
                XMLOutputFactory output = XMLOutputFactory.newInstance();
                XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter(serverMessagePath + serverMessageName));
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
        }
    }
}
