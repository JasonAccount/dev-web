package db.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private static final long serialVersionUID = 9007634717614634479L;

    private String id;
    private String accountName;
    private String pasword;
    private Date createDate;
    private Date updateDate;
    private String commonent;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCommonent() {
        return commonent;
    }

    public void setCommonent(String commonent) {
        this.commonent = commonent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
