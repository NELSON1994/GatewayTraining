package com.gatewayExample.gatewaytraining.Services;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import java.io.IOException;

public interface AllTransactionServiceTemplate {
    public void saveTransaction(ISOMsg isoMsg) throws ISOException;

    //void SplitTLVData(ISOMsg msg);

    void SplitTLVData(String field47Data, String msg1);
    String SplitTLVData1(String msg1, String m2);
    String SplitTLVData2(String msg1,String m2);

    void getHotels();

    //=========================ROKEL MINISTATEMENT========================
    String getMiniStatement(String ministatementDetails);

    //=========================Rokel Transfer==============================
    String transferTransaction(String transferDetails);

    String getAccountDetails(String cusNumber);

    String balanceEnquiryRokel(String accountNumber);

    String lastTransactionDetails(String customernumber);

    // Getting LIST OF CONTACTPERSONNNELS FROM A GIVEN API
    String fetchContactPersons3();

    // create contact personnel with REST TEMPLATE
    String createUser(String userDetails);

    // getting individual contact personnel
    String fetchOnePersonnel(Long id);

    String PosIrisData(String data);
    String echoTest();

    String generateRRN(String serialNumber);

    // using httpClient
    String fetchPersonsUsingHttpClient() throws IOException;

    String field4PaddingWithZeroes(String amount);

    String removePaddedZerosOnField4(String fld4);

    ISOMsg usingDifferentPackager(ISOMsg isoMsg);
}
