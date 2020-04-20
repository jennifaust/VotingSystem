import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map.*;
import java.util.*;

public class CreateTrendsApp
{
    // socket for connection to SISServer
    static Socket universal;
    private static int port = 53217;
    // message writer
    static MsgEncoder encoder;
    // message reader
    static MsgDecoder decoder;
    // scope of this component
    private static final String SCOPE = "SIS.Scope1";
    // name of this component
    private static final String NAME = "TrendsApp";
    //Return Message
    private static KeyValueList record = new KeyValueList();
    private static Archive db = new Archive();

    public static void main(String[] args)
    {
        //Main Program Loop
        while (true)
        {
            //Connect to SIS Server
            try
            {
                // try to establish a connection to SISServer
                universal = connect();

                // bind the message reader to inputstream of the socket
                decoder = new MsgDecoder(universal.getInputStream());
                // bind the message writer to outputstream of the socket
                encoder = new MsgEncoder(universal.getOutputStream());

                /*
                 * construct a Connect message to establish the connection
                 */
                KeyValueList conn = new KeyValueList();
                conn.putPair("Scope", SCOPE);
                conn.putPair("MessageType", "Connect");
                conn.putPair("Role", "Basic");
                conn.putPair("Name", NAME);
                encoder.sendMsg(conn);

                initRecord();

                // KeyValueList for inward messages, see KeyValueList for
                // details
                KeyValueList kvList;

                while (true)
                {
                    // attempt to read and decode a message, see MsgDecoder for
                    // details
                    kvList = decoder.getMsg();

                    // process that message
                    ProcessMsg(kvList);
                }

            }
            catch (Exception e)
            {
                // if anything goes wrong, try to re-establish the connection
                e.printStackTrace();
                try
                {
                    // wait for 1 second to retry
                    Thread.sleep(1000);
                }
                catch (InterruptedException e2)
                {
                }
                System.out.println("Try to reconnect");
                try
                {
                    universal = connect();
                }
                catch (IOException e1)
                {
                }
            }
        }
    }

    static Socket connect() throws IOException
    {
        //Used for connect(reconnect) to SIS Server
        Socket socket = new Socket("127.0.0.1", port);
        return socket;
    }

    private static void initRecord()
    {
        //Constructor for return message.
        record.putPair("Scope", "SIS");
        record.putPair("Sender", NAME);
        record.putPair("Date", System.currentTimeMillis() + "");
    }

    private static void ProcessMsg(KeyValueList kvList) throws Exception
    {
        System.out.println("Message Received.");

        String message = kvList.getValue("MessageType");
        String purpose = kvList.getValue("Purpose");
        String contactId = "";
        String posterId = "";
        String date = "";
        Map<String, String> map = new HashMap<String, String>();

        switch (message)
        {
            case "Confirm":
                //Confirm connection.
                System.out.println("Connect to SISServer successful.");
                break;
            //messages
            case "Setting":
                switch(purpose)
                {
                    case "ForwardVote":
					    //Process a vote from phone
                        System.out.println("Receiving a vote from VotingApp...");
						
						System.out.println("Adding vote to knowledge base");
						contactId = kvList.getValue("ContactId");
						posterId = kvList.getValue("PosterId");
                        //use this later
                        date = kvList.getValue("Date");
						db.store(contactId, posterId);
						
                        break;
                    case "AnalyzeTrend":
                        //Start the Poll
                        System.out.println("Analyzing trend and making predictions...");
                        db.displayPrediction();
				
                        break;
           
                }
            break;
        }
    }
}