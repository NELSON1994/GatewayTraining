package com.gatewayExample.gatewaytraining;

import com.gatewayExample.gatewaytraining.Entities.AllTransactions;
//import com.gatewayExample.gatewaytraining.Repositories.AllTransactionRepository;
import com.gatewayExample.gatewaytraining.Services.AllTransactionServiceTemplate;
import com.gatewayExample.gatewaytraining.Services.AllTransactionsService;
import com.gatewayExample.gatewaytraining.config.SpringContextBridgeService;
//import com.gatewayExample.gatewaytraining.constant.ContextBridge;
import com.gatewayExample.gatewaytraining.packager.TransportPackager;
import com.sleepycat.je.DatabaseEntry;
import org.jpos.iso.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

public class RequestHandlers implements ISORequestListener {
    private Logger logger = LoggerFactory.getLogger(RequestHandlers.class);

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
//        AllTransactionRepository allTransactionRepo = SpringContextBridgeService.services().getAllTransactionsRepo();
        AllTransactionServiceTemplate all = SpringContextBridgeService.services().getAllTransactionServiceTemplate();
        try {
            //setting field 12
            String now = ISODate.formatDate(new Date(), "YYYYMMddhhmm");
            isoMsg.set(12, now);
            // ============================process the request according to the MTI
            switch (isoMsg.getMTI()) {
                case "0120"://Authorisation advice
                    break;
                case "0220"://Completion
                    ISOMsg isoMsg1 = all.usingDifferentPackager(isoMsg);
                    break;
                case "0420"://Authorization Reversal
                    break;
                case "0500"://Settlement
                    break;
                case "0200":// void,[020000]==>refund,[200000]===>deposit[010000]
                    switch (isoMsg.getString(3)) {
                        case "020000"://VOID
                            break;
                        case "200000"://REFUND
                            break;
                        case "210000"://DEPOSIT CASH
                            break;
                        case "010000"://WITHDRAWAL
                            break;
                        case "090000"://Sale with Cashback
                            break;
                    }
                    break;
                case "0300": //user management
                    logger.info("Processing User management");
                    String isoField47 = isoMsg.getString(47);
                    isoMsg.set(39, "00");
                    break;
                case "0800": //Network management====>echo test
                    Date d = new Date();
                    logger.info("Running an echo test");
                    isoMsg.set(39, "00");
                    //all.saveTransaction(isoMsg);// called my service here
                    String ress = all.echoTest();
                    isoMsg.set(72, ress);
                    break;
                case "0100"://AUTHORIZATION TRANSACTIONS
                    //switch using processing code
                    switch (isoMsg.getString(3)) {
                        case "300000"://BALANCE INQUIRY
                            break;
                        case "003000"://LOG IN
                            String field47Data = isoMsg.getString(47);
                            String proCode = isoMsg.getString(3);

                            if (isoMsg.hasField(102)) {
                                //check if field 102 has serial number that is valid
                                String sn = isoMsg.getString(102);
                                if (sn.equalsIgnoreCase("") || sn.length() < 5) {
                                    isoMsg.set(39, "10");
                                    isoMsg.set(72, "Serial Number is INVALID");
                                } else {
                                    //split the data in terms of TLV and validate log in
                                    String res = all.SplitTLVData1(field47Data, proCode);
                                    if (res.equalsIgnoreCase("N/A")) {
                                        isoMsg.set(39, "06");
                                        isoMsg.set(72, "INVALID LOGIN CREDENTIALS");
                                    } else {
                                        String rrn = all.generateRRN(isoMsg.getString(102));
                                        isoMsg.set(37, rrn);
                                        isoMsg.set(39, "00");
                                        isoMsg.set(72, res);
                                        all.saveTransaction(isoMsg);// just to save the transaction when needed
                                    }
                                }

                            } else {
                                isoMsg.set(39, "10");
                                isoMsg.set(72, "Serial Number for the Terminal needed on field 102");
                            }

                            break;
                        case "003100":// terminal sending posIris Data
                            if (isoMsg.hasField(72)) {
                                String posIrisData = isoMsg.getString(72);
                                all.PosIrisData(posIrisData);
                                all.saveTransaction(isoMsg);
                                isoMsg.set(39, "00");
                                isoMsg.unset(72);
                                isoMsg.set(72, "POS IRIS DATA Received Succesful");
                            } else {
                                isoMsg.set(39, "06");
                                isoMsg.unset(72);
                                isoMsg.set(72, "POSIRIS DATA NEEDED FOR THE TERMINAL");
                            }
                            break;
                        case "003200"://========fetching one personnel====send id on field 72 USING REST TEMPLATE
                            String id = isoMsg.getString(72);
                            Long idd = Long.parseLong(id);
                            all.fetchOnePersonnel(idd);
                            isoMsg.set(39, "00");
                            isoMsg.set(72, "NEW TRANSACTION");
                            break;
                        case "003300"://======fetch all list of personnels using REST TEMPLATE
                            String listOfPersonnel = all.fetchContactPersons3();
                            isoMsg.set(39, "00");
                            isoMsg.set(72, listOfPersonnel);
                            break;
                        case "003400"://======fetch all list of personnels using HTTP CLIENT
                            String listOfPersonnel1 = all.fetchPersonsUsingHttpClient();
                            isoMsg.set(39, "00");
                            System.out.println(listOfPersonnel1);
                            isoMsg.set(72, listOfPersonnel1);
                            break;
                        case "003500"://======POST URL using REST TEMPLATE
                            if (isoMsg.hasField(72)) {
                                String hotelDetails = isoMsg.getString(72);
                                String response = all.createUser(hotelDetails);
                                if (response.equalsIgnoreCase("ok")) {
                                    isoMsg.set(39, "00");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "created succesfully");
                                } else {
                                    isoMsg.set(39, "06");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "failed to create the  required item");
                                }
                            } else {
                                isoMsg.set(39, "10");
                                isoMsg.unset(72);
                                isoMsg.set(72, "details needed on field 72.");
                            }

                            break;
                        case "003600"://======validate amount according to ISO amount
                            String fld72 = isoMsg.getString(72);
                            if (fld72.equalsIgnoreCase("")) {
                                isoMsg.set(39, "06");
                                isoMsg.unset(72);
                                isoMsg.set(72, "No Amount sent");
                            } else {
                                String amount = all.field4PaddingWithZeroes(fld72);
                                isoMsg.set(39, "00");
                                isoMsg.set(4, amount);
                                isoMsg.unset(72);
                                isoMsg.set(72, "Successful amount fetched");
                            }
                            break;
                        case "003700"://======removing padded zeros
                            String amount = isoMsg.getString(4);
                            try {
                                if (isoMsg.hasField(4)) {
                                    // String result=all.removePaddedZerosOnField4(amount);
                                    int val = Integer.parseInt(amount);
                                    isoMsg.set(39, "00");
                                    isoMsg.set(72, String.valueOf(val));
                                } else {
                                    isoMsg.set(39, "06");
                                    isoMsg.set(72, "Field 4 is Expected");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(">>>>>>>>>>>>>>>> Error>>>>>>>" + e);
                                isoMsg.set(39, "10");
                                isoMsg.set(72, "INVALID AMOUNT");
                            }
                            break;
                        case "003800"://======ROKEL getting the last transaction
                            String cusnum = isoMsg.getString(72);
                            if (cusnum.equalsIgnoreCase("")) {
                                isoMsg.set(39, "10");
                                isoMsg.unset(72);
                                isoMsg.set(72, "No Customer Number provided");
                            } else {
                                String lastTransaction = all.lastTransactionDetails(cusnum);
                                if (lastTransaction.equalsIgnoreCase("N/A")) {
                                    isoMsg.set(39, "11");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "No Last Transaction Found");
                                } else if (lastTransaction.equalsIgnoreCase("B")) {
                                    isoMsg.set(39, "06");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "Fetching of Last Transaction Failed");
                                } else {
                                    isoMsg.set(39, "00");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, lastTransaction);
                                }
                            }
                            break;
                        case "003900"://======ROKEL getting the last transaction
                            String cusnumm = isoMsg.getString(72);
                            if (cusnumm.equalsIgnoreCase("")) {
                                isoMsg.set(39, "10");
                                isoMsg.unset(72);
                                isoMsg.set(72, "No Customer Number provided");
                            } else {
                                String balanceDetails = all.balanceEnquiryRokel(cusnumm);
                                if (balanceDetails.equalsIgnoreCase("F/A")) {
                                    isoMsg.set(39, "06");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "Response From APIs responded with not 200");
                                } else if (balanceDetails.equalsIgnoreCase("Failed")) {
                                    isoMsg.set(39, "90");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "API connection Failed");
                                } else {
                                    isoMsg.set(39, "00");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, balanceDetails);
                                }
                            }
                            break;
                        case "004000"://======ROKEL ACCOUNT DETAILS
                            String cu = isoMsg.getString(72);
                            if (cu.equalsIgnoreCase("")) {
                                isoMsg.set(39, "10");
                                isoMsg.unset(72);
                                isoMsg.set(72, "No Customer Number provided");
                            } else {
                                String accountDetails = all.getAccountDetails(cu);
                                if (accountDetails.equalsIgnoreCase("A")) {
                                    isoMsg.set(39, "06");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "Response From APIs responded with not 200 Response Code");
                                } else if (accountDetails.equalsIgnoreCase("B")) {
                                    isoMsg.set(39, "90");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "API connection Failed");
                                } else {
                                    isoMsg.set(39, "00");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, accountDetails);
                                }
                            }
                            break;
                        case "004100"://======ROKEL FUND TRANSFER DETAILS
                            String transfer = isoMsg.getString(72);
                            if (transfer.equalsIgnoreCase("")) {
                                isoMsg.set(39, "10");
                                isoMsg.unset(72);
                                isoMsg.set(72, "No Transfer Details  provided");
                            } else {
                                String transferResponse = all.transferTransaction(transfer);
                                if (transferResponse.equalsIgnoreCase("A")) {
                                    isoMsg.set(39, "06");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "Fetching failed");
                                } else if (transferResponse.equalsIgnoreCase("B")) {
                                    isoMsg.set(39, "90");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "Connection Failed");
                                } else {
                                    isoMsg.set(39, "00");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, transferResponse);
                                }
                            }
                            break;
                        case "004200"://======ROKEL MINISTATEMENT DETAILS
                            String data = isoMsg.getString(72);
                            if (data.equalsIgnoreCase("")) {
                                isoMsg.set(39, "10");
                                isoMsg.unset(72);
                                isoMsg.set(72, "No MiniStatement Details  provided");
                            } else {
                                String ministatementResponse = all.getMiniStatement(data);
                                if (ministatementResponse.equalsIgnoreCase("A")) {
                                    isoMsg.set(39, "06");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "Error Faced on APIs");
                                } else if (ministatementResponse.equalsIgnoreCase("B")) {
                                    isoMsg.set(39, "90");
                                    isoMsg.unset(72);
                                    isoMsg.set(72, "Connection Failed");
                                } else {
                                    String[] breakdown = ministatementResponse.split("#");
                                    String summary = breakdown[0];
                                    String detail = breakdown[1];
                                    isoMsg.unset(72);// set detail statement
                                    isoMsg.unset(47);// set detail statement 0->999
                                    isoMsg.unset(48); // set summary statemnt 999->---
                                    isoMsg.set(39, "00");
                                    isoMsg.set(72, summary);
                                    int lengthOfDetail = detail.length();
                                    if (lengthOfDetail < 1000) {
                                        isoMsg.set(47, detail);
                                    } else {
                                        String a = detail.substring(0, 1000);
                                        String b = detail.substring(1000, lengthOfDetail);
                                        isoMsg.set(47, a);
                                        isoMsg.set(48, b);
                                    }
                                }
                            }
                            break;
                        default:
                            isoMsg.set(39, "06");
                            isoMsg.set(72, "TRANSACTION NOT ALLOWED ON TERMINAL");
                            break;
                    }
                    break;
                default:
                    logger.info("Unsupported MTI");
                    isoMsg.set(39, "06");
                    isoMsg.set(72, "TRANSACTION NOT ALLOWED ON TERMINAL");
            }

            isoMsg.setResponseMTI();// Setting the response MTI====Automatically add 10 to the sent MTI
            isoSource.send(isoMsg);// Sending back the message to the sending host
        } catch (ISOException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
