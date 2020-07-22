package com.gatewayExample.gatewaytraining;

import com.gatewayExample.gatewaytraining.packager.TransportPackager;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.NACChannel;

import java.io.IOException;

public class IsoTests {
    public static void main(String[] args) {
        try {
            byte[] TPDU = new byte[5];  // our TPDU from the
            TPDU[0] = 60;
            TPDU[1] = 00;
            TPDU[2] = 00;
            TPDU[3] = 00;
            TPDU[4] = 00;

            TransportPackager packager = new TransportPackager();// setting the packager we are using
           // ISOMsg msg = echoTest();//================= sending the echo test function
            ISOMsg msg=loginRequest();//===================== login Request
           // ISOMsg msg=posIrisData();//========================== POS IRIS DATA
            //setting ISOChannel from server.xml==>{{{host(IP),PORT,Packager.TPDU(if tpdu is present)}}}
            String server;
           // server = "127.0.0.1"; // localhost
             server = "41.215.130.247"; //remote  public
//            server = "192.168.1.124"; //remote private
            ISOChannel channel = new NACChannel(server,  9620,  packager, TPDU);//192.168.43.70
            msg.setPackager(packager);// setting packager for the ISOMsg
            channel.connect();// connect to the channel
            channel.send(msg);// send the message to the channel

            ISOMsg response = channel.receive();// getting the response back


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



}
