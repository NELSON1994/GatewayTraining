package com.gatewayExample.gatewaytraining.Services;

import com.gatewayExample.gatewaytraining.Entities.AllTransactions;
import com.gatewayExample.gatewaytraining.Entities.PosIrisData;
import com.gatewayExample.gatewaytraining.Repositories.AllTransactionRepository;
import com.gatewayExample.gatewaytraining.Repositories.PosIrisDataRepository;
import com.gatewayExample.gatewaytraining.Wrappers.ContactPersonWrapper;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service("allTransactionServiceTemplate")
public class AllTransactionsService implements AllTransactionServiceTemplate {
    //===================================================REST API==========================================
    @Value("${hotels-url}")// url for fetching all hottels
            String hotelsUrl;

    @Value("${hotels-ContactPerson}")// for fetching contact persons
            String contactPersonUrl;

    @Autowired
    RestTemplate restTemplate;
    //===================================================REST API==========================================

    //  private AllTransactionRepository allTransactionRepo = SpringContextBridgeService.services().getAllTransactionsRepo();

    @Autowired
    private AllTransactionRepository allTransactionRepo;

    @Autowired
    private PosIrisDataRepository posIrisDataRepository;

    @Autowired
    AllTransactionsService allTransactionsService;

    //    public AllTransactionsService(AllTransactionRepository allTransactionRepo) {
//        this.allTransactionRepo = SpringContextBridgeService.services().getAllTransactionsRepo();
//    }
    @Override
    public String echoTest() {
        return "Echo Test Succesful";
    }

    //save transaction here
    @Override
    public void saveTransaction(ISOMsg isoMsg) throws ISOException {
        AllTransactions allTransactions = new AllTransactions();
        allTransactions.setField39(isoMsg.getString(39));
        allTransactions.setField0(isoMsg.getMTI());
        allTransactions.setField3(isoMsg.getString(3));
        allTransactions.setField12(isoMsg.getString(12));
        allTransactions.setField11(isoMsg.getString(11));
        allTransactions.setField37(isoMsg.getString(37));
        allTransactions.setField39(isoMsg.getString(39));
        allTransactions.setField41(isoMsg.getString(41));
        allTransactions.setField42(isoMsg.getString(42));
        allTransactions.setField47(isoMsg.getString(47));
        allTransactions.setField70(isoMsg.getString(70));
        allTransactions.setField72(isoMsg.getString(72));
        allTransactions.setField102(isoMsg.getString(102));
        allTransactionRepo.save(allTransactions); // saving the data to database
    }

    @Override
    public void SplitTLVData(String field47Data, String msg1) {

    }

    @Override
    public String SplitTLVData1(String msg1, String b) {
        String response = null;
        StringBuilder rese = new StringBuilder();
        String tag;
        String len;
        String value;
        String value1 = null;
        String value2 = null;
        String value3 = null;
        String value4 = null;
        String value5 = null;
        String value6 = null;
        while (msg1.length() > 0) {
            tag = msg1.substring(0, 3);
            len = msg1.substring(3, 6);
            int valueLen = Integer.parseInt(len);
            value = msg1.substring(6, valueLen + 6);
            int totalLen = 6 + valueLen;
            switch (Integer.parseInt(tag)) {
                case 20:
                    value1 = value;
                    break;
                case 21:
                    value2 = value;
                    break;
                case 22:
                    value3 = value;
                    break;
                case 30:
                    value4 = value;
                    break;
                case 31:
                    value5 = value;
                    break;
                case 32:
                    value6 = value;
                    break;
                default:
                    break;
            }
            String output = msg1.substring(0, totalLen);
            msg1 = msg1.replace(output, "");
        }

        // switch according to transaction to use the splitted data
        switch (b) {
            case "003000"://log in
                // send the details in the api to check if the details are ok

                String s1 = "16148WL82127229";
                String s2 = "NELSON";
                String s3 = "123456";
                if (value6.equalsIgnoreCase(s1) && value4.equalsIgnoreCase(s2) && value5.equalsIgnoreCase(s3)) {
                    rese.append("NELSON").append("#").append("1234@gmail.com").append("#").append("NAIROBI").append("#").append("KABETE").append("#").
                            append("ADMINISTRATOR").append("#").append(value4).append("#").append(value6);
                    response = rese.toString();

                    response = rese.toString();
                } else {
                    response = "N/A";
                }
                break;
            default:
                break;
        }
        return response;
    }

    @Override
    public String SplitTLVData2(String msg1, String m2) {
        return null;
    }

    //getting list of hotels from API
    @Override
    public void getHotels() {
        try {
            Object response = restTemplate.getForObject(hotelsUrl, Object.class);
            if (response != null) {
                System.out.println(response.toString());
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    // Getting LIST OF CONTACTPERSONNNELS FROM A GIVEN API
    @Override
    public String fetchContactPersons3() {
        ResponseEntity<ContactPersonWrapper[]> contactPersonWrappers = restTemplate.getForEntity(contactPersonUrl, ContactPersonWrapper[].class);
        HttpStatus statusCode = contactPersonWrappers.getStatusCode();
        List<ContactPersonWrapper> contactPersonWrappers1 = new ArrayList<>();
        StringBuilder values = new StringBuilder();
        if (statusCode.value() == 200) {
            System.out.println(Arrays.toString(contactPersonWrappers.getBody()));

            for (ContactPersonWrapper c : contactPersonWrappers.getBody()) {
                contactPersonWrappers1.add(c);
                values.append(c.getFname()).append("#").
                        append(c.getLname()).append("#").
                        append(c.getEmail()).append("#").
                        append(c.getIdNo()).append("#").
                        append(c.getUsername()).
                        append("%");
            }
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println("Result: " + values.toString());
            System.out.println("===============Length of array================================" + contactPersonWrappers1.size());
            //return contactPersonWrappers.getBody();
            return values.toString();
        }
        return "";
    }

    // getting individual contact personnel
    @Override
    public String fetchOnePersonnel(Long id) {
        String response = null;
        StringBuilder stringBuilder = new StringBuilder();
        String getApi = contactPersonUrl + "/" + id;
        ResponseEntity<ContactPersonWrapper> c = restTemplate.getForEntity(getApi, ContactPersonWrapper.class);
        if (c.getStatusCode().value() == 200) {
            ContactPersonWrapper con = new ContactPersonWrapper();
            con = c.getBody();
            System.out.println(c.getBody());
            stringBuilder.append(con.getUsername()).append("#").append(con.getIdNo()).append("#").append(con.getEmail()).append("#").
                    append(con.getLname()).append("#").append(con.getFname()).append("#").append(con.getGender());
        } else {
            stringBuilder = null;
        }
        System.out.println("=========================uiiiiii==" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private void fetchContactPersons() {
        Object fetch3 = restTemplate.getForObject(contactPersonUrl, Object.class);
        System.out.println("=========================================VALUES===================" + fetch3);
    }

    // login function
    private Integer login(String serialNumber, String username, String password) {
        StringBuilder response = new StringBuilder();
        int res = 0;
        ISOMsg isoMsg = new ISOMsg();
        // rest API to check if serial number exist?
        // Rest Api to validate User using username and Password
        if (serialNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
            isoMsg.set(39, "90");
            isoMsg.unset(72);
            isoMsg.set(72, "Ensure All login Parameters are available.[Serial number,UserName and Password]");
            res = -1;
        } else {
            // to be checked from database for login API from ufs side
            String s1 = "16148WL82127229";
            String s2 = "NELSON";
            String s3 = "123456";
            if (serialNumber.equalsIgnoreCase(s1) && username.equalsIgnoreCase(s2) && password.equalsIgnoreCase(s3)) {
                response.append("NELSON").append("#").append("1234@gmail.com").append("#").append("NAIROBI").append("#").append("KABETE").append("#").
                        append("ADMINISTRATOR").append("#").append(username).append("#").append(serialNumber);
                isoMsg.set(39, "00");
                isoMsg.set(72, response.toString());
                res = 1;
            } else {
                isoMsg.set(39, "06");
                isoMsg.set(72, "INVALID LOGIN CREDENTIALS");
            }
        }
        return res;
    }

    @Override
    public String PosIrisData(String field72) {
        String response = null;
        String[] df = field72.split("#");
        String one = df[0];
        String two = df[1];
        String three = df[2];
        String four = df[3];
        String five = df[4];
        String six = df[5];
        String seven = df[6];
        String eight = df[7];
        String nine = df[8];
        // function to dump the data to db for saving
        PosIrisData posIrisData = new PosIrisData();
        posIrisData.setAppVersion(seven);
        posIrisData.setSerialNumber(one);
        posIrisData.setSdkUsed(two);
        posIrisData.setBatterylevel(three);
        posIrisData.setNetwork(four);
        posIrisData.setOsVersion(five);
        posIrisData.setTemperature(six);
        posIrisData.setIsCharging(eight);
        posIrisData.setTerminalType(nine);

        posIrisDataRepository.save(posIrisData);
        return response;

    }

    @Override
    // Serial number to be sent on field 102
    public String generateRRN(String serialNumber) {
        StringBuilder rrn = new StringBuilder();
        int l=serialNumber.length();
        String sn = serialNumber.substring(l-5,l);
        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        String formatedDate = simpleDateFormat.format(d);
       String a= formatedDate.replace("-", "");
        String b=  a.replace(":", "");
        String c=b.replace(" ", "");
        int len=c.length();
        String res=c.substring(len-7,len);
        rrn.append(sn).append(res);
        return rrn.toString();
    }
}
