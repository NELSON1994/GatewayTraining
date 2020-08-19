package com.gatewayExample.gatewaytraining;

import com.gatewayExample.gatewaytraining.packager.TransportPackager;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.channel.NACChannel;

import java.io.IOException;

public class IsoTests {
    public static void main(String[] args) {
        try {
            byte[] TPDU = new byte[5];  // our TPDU from the server.xml
            TPDU[0] = 60;
            TPDU[1] = 00;
            TPDU[2] = 00;
            TPDU[3] = 00;
            TPDU[4] = 00;

            TransportPackager packager = new TransportPackager();// setting the packager we are using
           // ISOMsg msg = echoTest();//========= sending the echo test function
            //ISOMsg msg=loginRequest();//========== login Request
           // ISOMsg msg=posIrisData();//=========== POS IRIS DATA
            // ISOMsg msg=lastTransaction();//========= last Transaction  from Rokel
           // ISOMsg msg=balanceInquiry();//============= balance Inquiry  from Rokel
            //ISOMsg msg=getAccountDatails();//=============get ACCOUNT DETAILS from Rokel
           // ISOMsg msg=transferTransaction();//============= TRANSFER TRANSACTION
            ISOMsg msg=miniStatement();//============= MiniStatement TRANSACTION
           // ISOMsg msg=fetchPersonnels();//========fetch list of contact personnels using REST TEMPLATE
           // ISOMsg msg=fetchPersonnelsUsingHttpClient();//=======fetch list of contact personnels using HTTP CLIENT
          //  ISOMsg msg=create();// ========create using REST TEMPLATE (POST)
            //ISOMsg msg=amountValidation(); // ==========validating amount according to ISO field 4
           // ISOMsg msg=removePaddedZerosonAmount(); // =========remove padded zeros on field 4
           // ISOMsg msg=sendToPackager2();//===========test on two packagers
            //setting ISOChannel from server.xml==>{{{host(IP),PORT,Packager.TPDU(if tpdu is present)}}}
            String server;
          //  server = "127.0.0.1"; // localhost
          server = "41.215.130.247"; //remote  public address
//            server = "192.168.1.124"; //remote private address
            ISOChannel channel = new NACChannel(server,  9620,  packager, TPDU);

           // ISOChannel channel = new ASCIIChannel(server,9620,packager);
            msg.setPackager(packager);// setting packager for the ISOMsg
            channel.connect();// connect to the channel
            channel.send(msg);// send the message to the channel

            ISOMsg response = channel.receive();// getting the response back
          //  response.dump(response,"RESPONSE");

        } catch (ISOException | IOException e) {
            e.printStackTrace();
        }
    }

    ISOMsg createUser() throws ISOException {
        ISOMsg isoMsg=new ISOMsg();
        isoMsg.setResponseMTI();
        isoMsg.set(39,"00");
        return isoMsg;
    }

    private static ISOMsg echoTest() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0800");
        isoMsg.set(3,"001100");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(47,"020002AB021004FGHJ022006YGHBNL");
        isoMsg.set(70,"301");
        isoMsg.set(72,"3");
        return isoMsg;
    }

    private static ISOMsg loginRequest() throws ISOException {
        ISOMsg isoMsg= new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003000");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(47,"030006NELSON03100612345603201516148WL82127229");
        isoMsg.set(102,"35677894790577899");
        return isoMsg;
    }

    private static ISOMsg posIrisData() throws ISOException {
        ISOMsg isoMsg=new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003100");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"678909099939#11.16.05.12 Patch A#80%#5#6.34.566 A#38#SBM Rev Version 1#No#IWL250");
        return isoMsg;
    }

    private static ISOMsg fetchPersonnels() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003300");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        return isoMsg;
    }

    private static ISOMsg fetchPersonnelsUsingHttpClient() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003400");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        return isoMsg;
    }
    private static ISOMsg create() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003500");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"Califonia#Best in Usa WSestern# all needed services for five star hotel");
        return isoMsg;
    }
    private static ISOMsg amountValidation() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003600");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"7200");
        return isoMsg;
    }

    private static ISOMsg lastTransaction() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003800");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"0201301840301");
        return isoMsg;
    }

    private static ISOMsg balanceInquiry() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003900");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"0201301840301");
        return isoMsg;
    }

    private static ISOMsg getAccountDatails() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"004000");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"0201301840301");
        return isoMsg;
    }

    private static ISOMsg transferTransaction() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"004100");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"0201303135001#0201301840301#1000.00#Personal#local");
        return isoMsg;
    }
    private static ISOMsg miniStatement() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"004200");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        isoMsg.set(72,"0201301840301#01-01-2020#01-03-2020");
        return isoMsg;
    }


    private static ISOMsg removePaddedZerosonAmount() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0100");
        isoMsg.set(3,"003700");
        isoMsg.set(4,"000000370000");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        return isoMsg;
    }
    private static ISOMsg sendToPackager2() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setMTI("0220");
        isoMsg.set(3,"004000");
        isoMsg.set(4,"000000370000");
        isoMsg.set(11,"000001");
        isoMsg.set(41,"45789021");
        isoMsg.set(42,"421456908756435");
        return isoMsg;
    }

    private static ISOMsg balanceCheck() throws ISOException{
        ISOMsg isoMsg = new ISOMsg();
        String decAmountAsAtr = "91.34"; // amount returnrd by the switch
        Float doubleAmt = Float.valueOf(decAmountAsAtr)*100; // remove the decimal place
        int valx100 = doubleAmt.intValue();
        isoMsg.set(4, ISOUtil.padleft(valx100+"",12,'0')); // padding using ISO inbuilt function
        isoMsg.set(39,"00");
        return isoMsg;
    }

}
