package com.gatewayExample.gatewaytraining.Services;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

public interface AllTransactionServiceTemplate {
    public void saveTransaction(ISOMsg isoMsg) throws ISOException;

    //void SplitTLVData(ISOMsg msg);

    void SplitTLVData(String field47Data, String msg1);
    String SplitTLVData1(String msg1, String m2);
    String SplitTLVData2(String msg1,String m2);

    void getHotels();

    // Getting LIST OF CONTACTPERSONNNELS FROM A GIVEN API
    String fetchContactPersons3();

    // getting individual contact personnel
    String fetchOnePersonnel(Long id);

    String PosIrisData(String data);
    String echoTest();

    String generateRRN(String serialNumber);
}
