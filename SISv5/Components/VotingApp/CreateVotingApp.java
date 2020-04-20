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

public class CreateVotingApp
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
    private static final String NAME = "VotingApp";
    //Return Message
    private static KeyValueList record = new KeyValueList();
    private static StoreVotes db = new StoreVotes();
	
    //private static ArchiveVotes dba = new ArchiveVotes();

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
        String vote = "";
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
                    case "Vote":
					    //Process a vote from phone
                        System.out.println("Received a Vote...");
						if (db.getPollStatus()) {
							System.out.println("Processing Vote");
							contactId = kvList.getValue("ContactId");
							vote = kvList.getValue("Vote");
							db.store(contactId, vote);
                            //forward vote to TrendAnalyzer
                            KeyValueList forwardVote = new KeyValueList();
                            forwardVote.putPair("Scope", SCOPE);
                            forwardVote.putPair("MessageType", "Setting");
                            forwardVote.putPair("Purpose", "ForwardVote");
                            forwardVote.putPair("Receiver", "TrendsApp");
                            forwardVote.putPair("Sender", "VotingApp");
                            forwardVote.putPair("PosterId", vote);
                            forwardVote.putPair("ContactId", contactId);
                            forwardVote.putPair("Date", "4/12/2020");
                            encoder.sendMsg(forwardVote);
						}
						else {
							System.out.println("Vote rejected - poll is closed.");
						}

                        //Send message to TrendsApp

                        break;
                    case "Kill":
                        //Kill the component
                        System.exit(0);
                        break;
                    case "OpenPoll":
                        //Start the Poll
                        System.out.println("Open Poll...");
						db.openPoll();
						
						//System.out.println("Init Archive...");
						//dba.initArchive();
						
                        Integer numberOfIDs = Integer.parseInt(kvList.getValue("NumberOfCandidates"));
                        String[] candidates = new String[numberOfIDs];
						
                        for (Integer i = 0; i < numberOfIDs; i++){
                            //candidates[i] = Integer.parseInt(kvList.getValue("Candidate" + i.toString()));
							candidates[i] = kvList.getValue("Candidate" + i.toString());
							db.addCandidateId(candidates[i], i);
							
							// add the candidates to the archive
							//dba.addCandidateId(candidates[i], i);
						}
						
						//load prior years
						/*System.out.println("Loading prior year's data...");
						dba.archive("2001", "1000", 5);
						dba.archive("2001", "1001", 10);
						dba.archive("2001", "1002", 0);
						dba.archive("2001", "1003", 2);
						
						dba.archive("2002", "1000", 7);
						dba.archive("2002", "1001", 14);
						dba.archive("2002", "1002", 0);
						dba.archive("2002", "1003", 4);
						
						dba.archive("2003", "1000", 2);
						dba.archive("2003", "1001", 4);
						dba.archive("2003", "1002", 1);
						dba.archive("2003", "1003", 2);*/

						//get predicted winner
						//System.out.println("");
                       // System.out.println("The PREDICTED WINNER is CandidateId: " + dba.getPredictedWinner() + " by " + dba.getPredictedWinnerPercent() + "%");
                        break;
                    case "ClosePoll":
                        //Close the Poll
                        System.out.println("Closing Poll...");
                        
                        //Send message to TrendsApp to analyze the trend
                        KeyValueList analyze = new KeyValueList();
                        analyze.putPair("Scope", SCOPE);
                        analyze.putPair("MessageType", "Setting");
                        analyze.putPair("Purpose", "AnalyzeTrend");
                        analyze.putPair("Receiver", "TrendsApp");
                        analyze.putPair("Sender", "VotingApp");
                        analyze.putPair("Date", "4/12/2020");
                        encoder.sendMsg(analyze);
						db.closePoll();
						//encoder.sendMsg(db.getMyMap());
						//clear archive
						//dba.clear(dba.getMyMap());
						
						//print results
                        db.clear(db.getMyMap());
						System.out.println("");
                        System.out.println("The winner is CandidateId: " + db.getWinner());
						System.out.println("");
						System.out.println("The runner-up is CandidateId: " + db.getRunnerUp());
						System.out.println("");
						db.displayResults();
                        break;
                }
            break;
        }
    }
}