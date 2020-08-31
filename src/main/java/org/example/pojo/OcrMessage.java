package org.example.pojo;


import java.util.List;

/**
 * @author Ethan
 * @version 1.0
 * @date 8/30 22:48
 */
public class OcrMessage {
    private TicketMessage data;

    private int error_code;

    private String error_msg;


    private String log_id;

    public TicketMessage getData() {
        return data;
    }

    public void setData(TicketMessage data) {
        this.data = data;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }
}
