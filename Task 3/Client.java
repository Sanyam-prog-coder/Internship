import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

////////////////////////////////////////////////////////////////////////////////////////////
/// 
/// Class       : ChatClient
/// Description : This class contains the logic for all functions
/// Author      : Ravne Sanyam Bhupendrakumar
/// Date        : 06-March-2026
/// 
////////////////////////////////////////////////////////////////////////////////////////////

class ChatClient
{
    private String serverAddress;
    private int serverPort;

    private Socket socket;
    private BufferedReader console;
    private BufferedReader in;
    private PrintWriter out;

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : ChatClient
    /// Desctiption :  Initializes the chat client with the server address
    ///               and port number required to connect to the server.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public ChatClient(String address, int port)
    {
        this.serverAddress = address;
        this.serverPort = port;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : start
    /// Desctiption : Establishes connection with the chat server, initializes
    ///               input/output streams, starts the message receiver thread,
    ///               and allows the user to send messages to the server.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public void start()
    {
        try
        {
            socket = new Socket(serverAddress, serverPort);

            console = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to server...");

            MessageReceiver receiver = new MessageReceiver(in);
            receiver.start();

            String userInput;

            while((userInput = console.readLine()) != null)
            {
                out.println(userInput);

                if(userInput.equalsIgnoreCase("exit"))
                {
                    break;
                }
            }

            socket.close();
        }
        catch(IOException e)
        {
            System.out.println("Client Error : " + e.getMessage());
        }
    }
}

class MessageReceiver extends Thread
{
    private BufferedReader in;

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : MessageReceiver
    /// Desctiption : Initializes the message receiver thread with the
    ///               input stream to receive messages from the server.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public MessageReceiver(BufferedReader in)
    {
        this.in = in;
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    /// 
    /// Function    : run
    /// Desctiption : Continuously listens for messages from the server
    ///               and displays them on the client console.
    /// Author      : Ravne Sanyam Bhupendrakumar
    /// Date        : 06-March-2026
    /// 
    ////////////////////////////////////////////////////////////////////////////////////////

    public void run()
    {
        String serverMessage;

        try
        {
            while((serverMessage = in.readLine()) != null)
            {
                System.out.println(serverMessage);
            }
        }
        catch(IOException e)
        {
            System.out.println("Disconnected from server.");
        }
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

class Client
{
    public static void main(String A[]) 
    {
        ChatClient client = new ChatClient("localhost", 5000);
        client.start();
    }
}