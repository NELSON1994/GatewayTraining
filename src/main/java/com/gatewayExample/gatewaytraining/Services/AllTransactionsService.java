package com.gatewayExample.gatewaytraining.Services;

import com.gatewayExample.gatewaytraining.Entities.AllTransactions;
import com.gatewayExample.gatewaytraining.Entities.Hotel;
import com.gatewayExample.gatewaytraining.Entities.PosIrisData;
import com.gatewayExample.gatewaytraining.Repositories.AllTransactionRepository;
import com.gatewayExample.gatewaytraining.Repositories.PosIrisDataRepository;
import com.gatewayExample.gatewaytraining.Wrappers.*;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.channel.PostChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("allTransactionServiceTemplate")
public class AllTransactionsService implements AllTransactionServiceTemplate {
    @Value("${switch-ip}")
    String ip;

    @Value("${switch-port}")
    String port;
    //===================================================REST API==========================================
    @Value("${hotels-url}")// url for fetching all hotels
            String hotelsUrl;

    @Value("${hotels-ContactPerson}")// for fetching contact persons
            String contactPersonUrl;

    @Value("${rokel-LastTransaction}")
    String lastTransactionDetailsUrl;

    @Value("${rokel-apikey}")
    String apiKey;

    @Value("${rokel-balanceEnquiry}")
    String balanceEnquiryUrl;

    @Value("${rokel-getAccountDetails}")
    String AccountDetailsUrl;

    @Value("${rokel-transfer}")
    String transferUrl;

    @Value("${rokel-ministatement}")
    String ministatementUrl;

    @Autowired
    RestTemplate restTemplate;
    //===================================================REST API==========================================

//      private AllTransactionRepository allTransactionRepo = SpringContextBridgeService.services().getAllTransactionsRepo();

    @Autowired
    private AllTransactionRepository allTransactionRepo;

    @Autowired
    private PosIrisDataRepository posIrisDataRepository;


//    @Autowired
//    AllTransactionsService allTransactionsService;

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
        LoginWrapper loginWrapper = new LoginWrapper();
        ChangePasswordWrapper changePasswordWrapper = new ChangePasswordWrapper();
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
                    loginWrapper.setSerialNumber(value1);
                    changePasswordWrapper.setSerialnumber(value1);
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

    //=========================ROKEL MINISTATEMENT========================
    @Override
    public String getMiniStatement(String ministatementDetails){
        String response=null;
        //cusnum#startdate#enddate
        String[] details=ministatementDetails.split("#");
        MiniStatementRequest request=new MiniStatementRequest();
        request.setApikey(apiKey);
        request.setCusNumber(details[0]);
        request.setStartDate(details[1]);
        request.setEndDate(details[2]);
        MultiValueMap<String,String> map=new LinkedMultiValueMap<>();
        map.add("apikey",request.getApikey());
        map.add("cusnum",request.getCusNumber());
        map.add("start_Date",request.getStartDate());
        map.add("end_date",request.getEndDate());
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        try{
            StringBuilder builder=new StringBuilder();
            StringBuilder builder1=new StringBuilder();
            StringBuilder builder2=new StringBuilder();
           // System.out.println("~~~~~~~~~~~~~~~~~~~~~~URL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+ministatementUrl);
            ResponseEntity<MiniStatementResponseWrapper> mresponse=restTemplate.postForEntity(
                    ministatementUrl,
                    new org.springframework.http.HttpEntity<>(map,httpHeaders),
                    MiniStatementResponseWrapper.class
                    );
           // System.out.println("~~~~~~~~~~~~~~~~~~RESPONSE BODY~~~~~~~~~~~~~~"+mresponse.getBody());
            int responseCode=mresponse.getStatusCode().value();
           // System.out.println("~~~~~~~~~~~~~~~~~~RESPONSE CODE~~~~~~~~~~~~~~"+responseCode);
            MiniStatementResponseWrapper body=mresponse.getBody();
            if (responseCode !=200 || body==null){
                response="A";
            }
            else{
               // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~`SUMMARY ~~~~~~~~~~"+body.getSummary());
                //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~DETAIL ~~~~~~~~~~"+body.getDetail());

                SummaryResponse sResponse=body.getSummary();
                builder1.append(sResponse.getTotalCredits()).append("&")
                        .append(sResponse.getTotalDebits()).append("&")
                        .append(sResponse.getAccount()).append("&")
                        .append(sResponse.getCrCount()).append("&")
                        .append(sResponse.getDrCount()).append("&")
                        .append(sResponse.getEndDate()).append("&")
                        .append(sResponse.getPresentBalance()).append("&")
                        .append(sResponse.getPreviousBalance()).append("&")
                        .append(sResponse.getStartDate()).append("&")
                        .append(sResponse.getStdte());

               // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~`SUMMARY  STRING~~~~~~~~~~"+builder1.toString());
               // System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                DetailResponse[] detailResponses=body.getDetail();
               // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~DETAIL size~~~~~~~~~~"+detailResponses.length);
                for (DetailResponse dResponse:detailResponses){
                        builder2.append(dResponse.getBalance()).append("&")
                                .append(dResponse.getCredit()).append("&")
                                .append(dResponse.getDebit()).append("&")
                                .append(dResponse.getDate()).append("&")
                                .append(dResponse.getDescription())
                                .append("%");
                }
               // System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~`DETAIL  STRING~~~~~~~~~~"+builder2.toString());
              //  System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                String summary=builder1.toString();
                String detail=builder2.toString();
                builder.append(summary).append("#")
                        .append(detail);

                response=builder.toString();
            }
        }
        catch(Exception e){
            e.printStackTrace();
            response="B";
        }
        return response;
    }

    //=========================Rokel Transfer==============================
    @Override
    public String transferTransaction(String transferDetails){
        String response=null;
        TransferRequest request=new TransferRequest();
        String[] df = transferDetails.split("#");
        request.setAccountFrom(df[0]);
        request.setAccountTo(df[1]);
        request.setAmount(df[2]);
        request.setRemarks(df[3]);
        request.setType(df[4]);
        request.setApikey(apiKey);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("apikey",request.getApikey());
        data.add("Acct_fro",request.getAccountFrom());
        data.add("Acct_to",request.getAccountTo());
        data.add("Amount",request.getAmount());
        data.add("remarks",request.getRemarks());
        data.add("type",request.getType());
        try{
            //System.out.println("~~~~~~~~~~~~~~~~~~~~~~URL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+transferUrl);
           ResponseEntity<TransferResponseWrapper> resp=restTemplate.postForEntity(
                   transferUrl, new org.springframework.http.HttpEntity<>(data,httpHeaders),TransferResponseWrapper.class
           );
           // System.out.println("~~~~~~~~~~~~~~~~~Response body~~~~~~~~~~~~~~~~~~~~~~"+resp.getBody());
            int responseCode=resp.getStatusCode().value();
           // System.out.println("~~~~~~~~~~~~~~~~~Response Code~~~~~~~~~~~~~~~~~~~~~~"+responseCode);
            TransferResponseWrapper transferResponseWrapper=resp.getBody();
            if (responseCode==200 && transferResponseWrapper !=null){
                StringBuilder dq=new StringBuilder();
                dq.append(transferResponseWrapper.getStatus()).append("@")
                        .append(transferResponseWrapper.getRefNumber()).append("@")
                        .append(transferResponseWrapper.getSource()).append("@")
                        .append(transferResponseWrapper.getDestination()).append("@")
                        .append(transferResponseWrapper.getAmount());
                  response=dq.toString();
            }
            else{
                response="A";
            }

        }
        catch(Exception ex){
            ex.printStackTrace();
            response="B";
        }

        return response;
    }
    //========================Rokel get account details======================
    @Override
    public String getAccountDetails(String cusNumber){
        String res=null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        GetAccountDetailsRequest request=new GetAccountDetailsRequest();
        MultiValueMap<String, String> details = new LinkedMultiValueMap<>();
        request.setApikey(apiKey);
        request.setCustumerNumber(cusNumber);

        details.add("apikey",request.getApikey());
        details.add("cusnum",request.getCustumerNumber());
        try{
           // System.out.println("~~~~~~~~~~~~~~~~~~~~~~URL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+AccountDetailsUrl);
            org.springframework.http.HttpEntity<GetAccountDetailsRequest> req=new org.springframework.http.HttpEntity<>(httpHeaders);
            ResponseEntity<GetAccountDetailsResponseWrapper> response=restTemplate.postForEntity(
                    AccountDetailsUrl, new org.springframework.http.HttpEntity<>(details,httpHeaders),GetAccountDetailsResponseWrapper.class
            );
            //System.out.println("~~~~~~~~~~~~~~~~~Response body~~~~~~~~~~~~~~~~~~~~~~"+response.getBody());

            int status=response.getStatusCode().value();
            if (status==200){
                StringBuilder dd=new StringBuilder();
                GetAccountDetailsResponseWrapper rt=response.getBody();
                        dd.append(rt.getStatus()).append("#")
                        .append(rt.getCustomerName()).append("#")
                        .append(rt.getAddress()).append("#")
                        .append(rt.getBirthdate()).append("#")
                        .append(rt.getEmail()).append("#")
                        .append(rt.getMobileNumber()).append("#")
                        .append(rt.getMonthName()).append("#")
                        .append(rt.getTelephone());

                        res=dd.toString();
            }
            else{
                res="A";
            }
        }
        catch(Exception e){
            e.printStackTrace();
            res="B";
        }
        return res;
    }

    //===========================Rokel Balance Inquiry======================
    @Override
    public String balanceEnquiryRokel(String accountNumber) {
        StringBuilder rp = new StringBuilder();
        String response = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        BalanceInquiryRequest request = new BalanceInquiryRequest();
        request.setApikey(apiKey);
        request.setCustumerNumber(accountNumber);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("apikey", request.getApikey());
        map.add("cusnum", request.getCustumerNumber());
        try {
           // System.out.println("~~~~~~~~~~~~~~~~~~~~~~URL~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"+balanceEnquiryUrl);
            ResponseEntity<BalanceInquiryResponseWrapper> responsee = restTemplate.postForEntity(balanceEnquiryUrl,
                    new org.springframework.http.HttpEntity<>(map, httpHeaders), BalanceInquiryResponseWrapper.class);

            HttpStatus code = responsee.getStatusCode();

            int coddee = code.value();
           // System.out.println("~~~~~~~~~~~~~~~~~~~~~~Response code~~~~~~~~~~~~~~" + coddee);
            if (coddee != 200) {
                response = "F/A";
            } else {
                BalanceInquiryResponseWrapper responseWrapper = new BalanceInquiryResponseWrapper();
                responseWrapper = responsee.getBody();
                //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~``RESPONSE BODY~~~~~~~~~~~~~~~~" + responseWrapper);
                rp.append(responseWrapper.getSTATUS()).append("#")
                        .append(responseWrapper.getAVAILABLEBALANCE()).append("#")
                        .append(responseWrapper.getBOOKBALANCE()).append("#")
                        .append(responseWrapper.getPERMITTEDBALANCE());
                response = rp.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response = "Failed";
        }


        return response;
    }

    //===============Rokel last Transaction==========================
    @Override
    public String lastTransactionDetails(String customernumber) {
        //System.out.println("***********Rokel API: " + lastTransactionDetailsUrl);
       // System.out.println("************API-KEY: " + apiKey);
        StringBuilder details = new StringBuilder();
        LastTransactionRequestWrapper lastTransactionRequestWrapper = new LastTransactionRequestWrapper();
        lastTransactionRequestWrapper.setCustomerNumber(customernumber);
        lastTransactionRequestWrapper.setApiKey(apiKey);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> detail = new LinkedMultiValueMap<>();
            detail.add("apikey", lastTransactionRequestWrapper.getApiKey());
            detail.add("cusnum", lastTransactionRequestWrapper.getCustomerNumber());
            LastTransactionResponse lastTransactionResponse = new LastTransactionResponse();

//            ResponseEntity<LastTransactionResponseWrapper[]> response= restTemplate.postForEntity(lastTransactionDetailsUrl
//                    ,new org.springframework.http.HttpEntity<>(detail, headers),
//                    LastTransactionResponseWrapper[].class);

            ResponseEntity<LastTransactionResponse> response = restTemplate.postForEntity(lastTransactionDetailsUrl
                    , new org.springframework.http.HttpEntity<>(detail, headers),
                    LastTransactionResponse.class);
            HttpStatus responseCode = response.getStatusCode();
            //System.out.println("=========================================================");
            lastTransactionResponse = response.getBody();
            List<LastTransactionResponseWrapper> kk = new ArrayList<>();
            assert lastTransactionResponse != null;
            for (LastTransactionResponseWrapper l : lastTransactionResponse.getLastTransactiondetail()) {
                kk.add(l);
                // System.out.println("~~~~~~~~~~~~~~~~~~~~~LAST TRANSACTION DETAILS~~~~~~~~~~" +l);
            }
           // System.out.println("~~~~~~~~~~~~~~~~~~~~~~Response Code~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + responseCode);
            if (responseCode.value() == 200) {
                List<LastTransactionResponseWrapper> lres = new ArrayList<>();
                //  System.out.println("~~~~~~~~~~~~~~~~~~~~response body~~~~~~~~~~~~~~"+response.getBody());
                if (response.getBody() == null) {
                    details.append("N/A");
                } else {
                    // LastTransactionResponseWrapper[] responseWrapper=response.getBody();
                    //   LastTransactionResponseWrapper one=responseWrapper[0];//getting the top details on the List
                    LastTransactionResponseWrapper one = response.getBody().getLastTransactiondetail()[0];
                   // System.out.println("~~~~~~~~~~~~~~~~~~ first details~~~~~~~~~~^^^^^^^^^^^^^^^^^^~~~~~~" + one);
                    details.append(one.getAccFrom()).append("#")
                            .append(one.getAccTo()).append("#")
                            .append(one.getAmount()).append("#")
                            .append(one.getDate()).append("#")
                            .append(one.getTransactionId()).append("#")
                            .append(one.getDetails()).append("#")
                            .append(one.getAction());
                }
            } else {
                details.append("Fetching Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            details.append("B");
        }
        return details.toString();
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
            //System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
           // System.out.println("Result: " + values.toString());
           // System.out.println("===============Length of array================================" + contactPersonWrappers1.size());
            //return contactPersonWrappers.getBody();
            return values.toString();
        }
        return "";
    }

    // create contact personnel with REST TEMPLATE
    @Override
    public String createUser(String userDetails) {
        String response = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Hotel hotel = new Hotel();
        String[] df = userDetails.split("#");
        String name = df[0];
        String description = df[1];
        String services = df[2];

        hotel.setHotelName(name);
        hotel.setHotelDescriptions(description);
        hotel.setServices(services);

        ResponseEntity<Hotel> requestEntity = restTemplate.postForEntity(hotelsUrl, hotel, Hotel.class);
        if (requestEntity.getStatusCode() == HttpStatus.CREATED) {
            response = "OK";
        } else {
            response = "ERROR";
        }

        return response;
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
       // System.out.println("=========================uiiiiii==" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private void fetchContactPersons() {
        Object fetch3 = restTemplate.getForObject(contactPersonUrl, Object.class);
        //System.out.println("=========================================VALUES===================" + fetch3);
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
        int l = serialNumber.length();
        String sn = serialNumber.substring(l - 5, l);
        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        String formatedDate = simpleDateFormat.format(d);
        String a = formatedDate.replace("-", "");
        String b = a.replace(":", "");
        String c = b.replace(" ", "");
        int len = c.length();
        String res = c.substring(len - 7, len);
        rrn.append(sn).append(res);
        return rrn.toString();
    }

    // =========================using httpClient=================================
    @Override
    public String fetchPersonsUsingHttpClient() throws IOException {
        StringBuilder re = new StringBuilder();
        // create default httpclient
        HttpGet request = new HttpGet(contactPersonUrl);
        // add headers=====httpGet.addHeader(,)

        try {
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse res = client.execute(request);
            // get HTTP
            System.out.println("============================PROTOCOL VERSION=======" + res.getProtocolVersion());
            int resc = res.getStatusLine().getStatusCode();
            System.out.println("============================RESPONSE CODE=============" + resc);
            InputStream body = res.getEntity().getContent();
            HttpEntity httpEntity = res.getEntity();
            System.out.println("============================RESPONSE BODY=============" + body);
            List<ContactPersonWrapper> contactPersonWrappers = (List<ContactPersonWrapper>) httpEntity.getContent();
            for (ContactPersonWrapper c : contactPersonWrappers) {
                re.append(c.getFname()).append("*")
                        .append(c.getLname()).append("*")
                        .append(c.getGender()).append("*")
                        .append(c.getEmail()).append("*")
                        .append("#");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re.toString();
    }

    @Override
    public String field4PaddingWithZeroes(String amount) {
        String paddedVal = StringUtils.leftPad(amount, 12, "0");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>======PADDED AMOUNT=====>>>>>>>>>>>>>" + paddedVal);
        return paddedVal;
    }

    @Override
    public String removePaddedZerosOnField4(String fld4) {
        String finalAmount = "";
        System.out.println("============================on fnc " + fld4);
        if (fld4.length() > 4) {
            System.out.println("============================on fnc " + fld4.length());
            while (fld4.startsWith("0")) {
                char val = fld4.charAt(0);
                String newVal = fld4.replace(String.valueOf(val), "");
                fld4 = newVal;
            }
            finalAmount = fld4;
        } else {
            finalAmount = "Invalid Amount Sent";
        }
        return finalAmount;
    }

    @Override
    public ISOMsg usingDifferentPackager(ISOMsg isoMsg) {
       // String ip = "127.0.0.1";
        //String port = "6000";
        // set Packager
        ISOPackager iso87APackager = new ISO87APackager();
        // ===set channel,  // ===set port  //===set IP// ====Packager
        ISOChannel postChannel = new ASCIIChannel(ip, Integer.parseInt(port), iso87APackager);
        try {
            // validate if port and IP is valid
            if (ip == null || port.isEmpty()) {
                System.out.println(">>>>>>>>>>>~~~~~~~~~~~~~~~~ERROR ON PORT & IP>>>>>>>~~~~~~~~~~~~~~");
                System.out.println("Invalid Port and IP for connection");
                isoMsg.set(39, "10");
                isoMsg.set(72, "Invalid IP and Port");
                postChannel.disconnect();
            } else {
                System.out.println(">>>>>~~~~Connect on port=======~~~~~~~~~~" + port);
                System.out.println(">>>>>~~~~Connect on ip=======~~~~~~~~~~" + ip);
                // make connection
                postChannel.connect();
                //check if it is connected
                if (postChannel.isConnected()) {
                    Logger logger = new Logger();
                    logger.addListener(new SimpleLogListener(System.out));
                    ((LogSource) postChannel).setLogger(logger, "test-channel");
                    postChannel.setPackager(iso87APackager);
                    //send the message
                    //check if message from pos has field 3
                    if (isoMsg.hasField(3)) {
                        // postChannel.setTimeout(10000);
                        postChannel.send(isoMsg);
                        //  Thread.sleep(200);
                        ISOMsg response = postChannel.receive();// receive the response back
                        String valueProCode = response.getString(3);
                        response.set(39, "00");
                        String responseCode = response.getString(39);
                        switch (valueProCode) {
                            case "040000":
                                isoMsg.unset(72);
                                isoMsg.set(39, responseCode);
                                isoMsg.set(72, "MINI STATEMENT");
                                break;
                            case "005000":
                                isoMsg.unset(72);
                                isoMsg.set(39, responseCode);
                                isoMsg.set(72, "RECONCILIATION");
                                break;
                            case "006000":
                                isoMsg.unset(72);
                                isoMsg.set(39, responseCode);
                                isoMsg.set(72, "BALANCE INQUIRY");
                                break;
                            default:
                                isoMsg.set(39, "06");
                                break;
                        }
                    } else {
                        isoMsg.set(39, "06");
                        isoMsg.unset(72);
                        isoMsg.set(72, "Processing code needed in your ISO Message");
                    }

                } else {
                    postChannel.reconnect();
                    System.out.println("~~~~~~~~~~~~~~CONNECTION FAILED on IP and Port~~~~~~~~~~" + ip + ":" + port);
                    // getting counters for reconnection
//                    int[] counters = postChannel.getCounters();
//                    int counterss=counters.length;
//                    // reconnections attempts set to 4
//                    for (int i = 4;  counterss<i; counterss++) {
//                        postChannel.reconnect();
//                        System.out.println("~~~~~~~~RECONNECTION ATTEMPT~~~~"+i);
//                    }
                    //  postChannel.resetCounters();
                    postChannel.disconnect();
                    isoMsg.set(39, "96");
                    isoMsg.set(72, "Maximum Reconnections reached.");
                }
            }
        } catch (IOException | ISOException e) {
            e.printStackTrace();
            isoMsg.set(39, "06");
            isoMsg.set(72, "Connection failed");
        }
        return isoMsg;

    }
}
