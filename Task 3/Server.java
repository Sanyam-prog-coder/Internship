import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : ChatServer
/// Description : This class contains the logic for all functions
/// Author      : Ravne Sanyam Bhupendrakumar
/// Date        : 06-March-2026
/// 
////////////////////////////////////////////////////////////////////////////////////////////

class ChatServer
{
    private int port;
    private Set<ClientHandler> clientHandlers;

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : ChatServer
    /// Desctiption : Initializes the chat server with the specified port number
    ///               and creates a collection to store connected clients.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public ChatServer(int port)
    {
        this.port = port;
        clientHandlers = new HashSet<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : startServer
    /// Desctiption : Starts the server socket, waits for client connections,
    ///               and creates a new thread for each connected client.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public void startServer()
    {
        System.out.println("Server Started...");

        try(ServerSocket serverSocket = new ServerSocket(port))
        {
            while(true)
            {
                Socket socket = serverSocket.accept();
                System.out.println("New Client connected.");

                ClientHandler handler = new ClientHandler(socket,this);
                clientHandlers.add(handler);

                new Thread(handler).start();
            }
        }
        catch(IOException e)
        {
            System.out.println("Server error : "+e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : broadcast
    /// Desctiption : Sends a message to all connected clients except the sender.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public void broadcast(String message, ClientHandler excludeUser)
    {
        for(ClientHandler client : clientHandlers)
        {
            if(client != excludeUser)
            {
                client.sendMessage(message);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : removeClient
    /// Desctiption : Removes a client from the list of active clients when
    ///               the client disconnects from the server.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public void removeClient(ClientHandler client)
    {
        clientHandlers.remove(client);
    }
}

class ClientHandler implements Runnable
{
    private Socket socket;
    private ChatServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : ClientHandler
    /// Desctiption : Initializes the client handler with the client socket
    ///               and reference to the chat server.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public ClientHandler(Socket socket, ChatServer server)
    {
        this.socket = socket;
        this.server = server;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : run
    /// Desctiption : Handles communication with the connected client. It reads
    ///               messages from the client and broadcasts them to other users.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public void run()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            out.println("Enter your name : ");
            clientName = in.readLine();

            server.broadcast(clientName + " joined the chat", this);

            String message;

            while((message = in.readLine()) != null)
            {
                if(message.equalsIgnoreCase("exit"))
                {
                    System.out.println("Client left...");
                    break;
                }
                server.broadcast(clientName + " : " + message, this);
            }
        }
        catch(IOException e)
        {
            System.out.println("Connection error : "+ e.getMessage());
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(IOException e)
            {}

            server.removeClient(this);
            server.broadcast(clientName + " left the chat",this);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : sendMessage
    /// Desctiption : Sends a message from the server to the connected client.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public void sendMessage(String message)
    {
        out.println(message);
    }
}

////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Function    : main
/// Description : Entry point of the chat client application.
///               Creates a ChatClient object and starts the client.
/// Author      : Ravne Sanyam Bhupendrakumar
/// Date        : 06-March-2026
/// 
////////////////////////////////////////////////////////////////////////////////////////////                

class Server
{
    public static void main(String A[])
    {
        ChatServer server = new ChatServer(5000);
        server.startServer();
    }
}
    