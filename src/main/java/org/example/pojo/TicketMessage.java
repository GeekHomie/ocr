package org.example.pojo;

import java.util.List;

/**
 * @author Ethan
 * @version 1.0
 * @date 8/30 22:51
 */
public class TicketMessage {
    private List<AbstractTicket> ret;
    private String logId;

    public List<AbstractTicket> getRet() {
        return ret;
    }

    public void setRet(List<AbstractTicket> ret) {
        this.ret = ret;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}
