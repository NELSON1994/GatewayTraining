package com.gatewayExample.gatewaytraining;

import com.gatewayExample.gatewaytraining.Entities.AllTransactions;
import com.gatewayExample.gatewaytraining.Repositories.AllTransactionRepository;
import com.gatewayExample.gatewaytraining.Services.AllTransactionServiceTemplate;
import com.gatewayExample.gatewaytraining.Services.AllTransactionsService;
import com.gatewayExample.gatewaytraining.config.SpringContextBridgeService;
import com.gatewayExample.gatewaytraining.constant.ContextBridge;
import com.gatewayExample.gatewaytraining.packager.TransportPackager;
import com.sleepycat.je.DatabaseEntry;
import org.jpos.iso.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestHandlers implements ISORequestListener {
    private Logger logger = LoggerFactory.getLogger(RequestHandlers.class);
    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        AllTransactionRepository allTransactionRepo = SpringContextBridgeService.services().getAllTransactionsRepo();
        AllTransactionServiceTemplate all=SpringContextBridgeService.services().getAllTransactionServiceTemplate();
        try {
            //setting field 12
            String now = ISODate.formatDate(new Date(), "YYYYMMddhhmm");
            isoMsg.set(12, now);
            // ============================process the request according to the MTI
            switch (isoMsg.getMTI()){
                case "0120":// ============================Authorisation advice
                    break;
                case "0220":// ============================Completion
                    break;
                case "0420":// ===========================Authorization Reversal
                    break;
                case "0500"://=========================== Settlement
                    break;
                case "0200":// ====================================================void,[020000]==>refund,[200000]===>deposit[010000]
                    switch (isoMsg.getString(3)){
                        case "020000"://=================VOID
                            break;
                        case "200000"://=================REFUND
                            break;
                        case "210000"://=================DEPOSIT CASH
                            break;
                        case "010000"://=================WITHDRAWAL
                            break;
                        case "090000"://=================Sale with Cashback
                            break;
                    }
                    break;
                case "0300": // ========================================================user management
                    logger.info("Processing User management");
                    String isoField47=isoMsg.getString(47);
                    isoMsg.set(39, "00");
                    break;
                case "0800": // =======================================================Network management====>echo test
                    Date d=new Date();
                    logger.info("Running an echo test");
                    isoMsg.set(39, "00");

                     //all.saveTransaction(isoMsg);// called my service here
                    String ress=all.echoTest();
                    isoMsg.set(72,ress);
                    break;
                case "0100":// ==================================================AUTHORIZATION TRANSACTIONS
                    //switch using processing code
                    switch (isoMsg.getString(3)){
                        case "300000"://=================BALANCE INQUIRY
                            break;
                        case "003000":// ================   LOG IN
                                String field47Data=isoMsg.getString(47);
                                String proCode=isoMsg.getString(3);
                                //check if field 102 is in the message sent
                            if (isoMsg.hasField(102)){
                                //check if field 102 has serial number that is valid
                                String sn=isoMsg.getString(102);
                                if (sn.equalsIgnoreCase("") || sn.length()<5 ){
                                    isoMsg.set(39, "10");
                                    isoMsg.set(72,"Serial Number is INVALID");
                                }
                                else{
                                    //split the data in terms of TLV and validate log in
                                    String res=  all.SplitTLVData1(field47Data,proCode);
                                    if (res.equalsIgnoreCase("N/A")){
                                        isoMsg.set(39, "06");
                                        isoMsg.set(72,"INVALID LOGIN CREDENTIALS");
                                    }
                                    else{
                                        String rrn=all.generateRRN(isoMsg.getString(102));
                                        isoMsg.set(37,rrn);
                                        isoMsg.set(39, "00");
                                        isoMsg.set(72,res);
                                        all.saveTransaction(isoMsg);// just to save the transaction when needed
                                    }
                                }

                            }
                            else{
                                isoMsg.set(39, "10");
                                isoMsg.set(72,"Serial Number for the Terminal needed on field 102");
                            }

                            break;
                        case "003100":// terminal sending posIris Data
                            if (isoMsg.hasField(72)){
                                String posIrisData=isoMsg.getString(72);
                                all.PosIrisData(posIrisData);
                                all.saveTransaction(isoMsg);
                                isoMsg.set(39,"00");
                                isoMsg.unset(72);
                                isoMsg.set(72,"POS IRIS DATA Received Succesful");
                            }
                            else {
                                isoMsg.set(39, "06");
                                isoMsg.unset(72);
                                isoMsg.set(72,"POSIRIS DATA NEEDED FOR THE TERMINAL");
                            }
                            break;
                        case "003200":
                            String id=isoMsg.getString(72);
                            Long idd=Long.parseLong(id);
                            all.fetchOnePersonnel(idd);
                            isoMsg.set(39,"00");
                            isoMsg.set(72,"NEW TRANSACTION");
                            break;
                        case "003300":
                            isoMsg.set(39,"00");
                            isoMsg.set(72,"NEW TRANSACTION 2");
                            break;
                        default:
                            isoMsg.set(39,"06");
                            isoMsg.set(72,"TRANSACTION NOT ALLOWED ON TERMINAL");
                            break;
                    }

                    break;
                default:
                    logger.info("Unsupported MTI");
                    isoMsg.set(39, "06");
                    isoMsg.set(72,"TRANSACTION NOT ALLOWED ON TERMINAL");
            }

            isoMsg.setResponseMTI();// Setting the response MTI====Automatically add 10 to the sent MTI
            isoSource.send(isoMsg);// Sending back the message to the sending host
        } catch (ISOException | IOException e){
            e.printStackTrace();
        }
        return true;
    }
}
