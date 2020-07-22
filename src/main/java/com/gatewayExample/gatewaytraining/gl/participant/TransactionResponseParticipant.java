package com.gatewayExample.gatewaytraining.gl.participant;

import com.gatewayExample.gatewaytraining.constant.Constants;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import java.io.Serializable;

/**
 * @author kmacharia
 */
public class TransactionResponseParticipant implements TransactionParticipant{
    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context)serializable;
        ISOMsg respMsg = (ISOMsg)ctx.get(Constants.RESPONSE_KEY);
        try {
            respMsg.set(39,"01");
            ctx.put(Constants.RESPONSE_KEY,respMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
		System.out.println(":::: = TransactionResponseParticipant Processing...");
        return PREPARED;
    }

    @Override
    public void commit(long l, Serializable serializable) {

    }

    @Override
    public void abort(long l, Serializable serializable) {

    }
}
