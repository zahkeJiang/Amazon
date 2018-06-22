package amazon.jy.com.amazon.entity;

/**
 * Created by jiangy on 18-4-27.
 */

public class Order {
    private Integer oId;

    private String bussinessId;

    private Double oCount;

    private String oDate;

    private String oStatus;

    private String oDeliver;

    private Integer oDeliverFee;

    private String uPay;

    private String uInvoiceType;

    private String uInvoiceTitle;

    public Integer getoId() {
        return oId;
    }

    public void setoId(Integer oId) {
        this.oId = oId;
    }

    public String getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(String bussinessId) {
        this.bussinessId = bussinessId;
    }

    public Double getoCount() {
        return oCount;
    }

    public void setoCount(Double oCount) {
        this.oCount = oCount;
    }

    public String getoDate() {
        return oDate;
    }

    public void setoDate(String oDate) {
        this.oDate = oDate;
    }

    public String getoStatus() {
        return oStatus;
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }

    public String getoDeliver() {
        return oDeliver;
    }

    public void setoDeliver(String oDeliver) {
        this.oDeliver = oDeliver;
    }

    public Integer getoDeliverFee() {
        return oDeliverFee;
    }

    public void setoDeliverFee(Integer oDeliverFee) {
        this.oDeliverFee = oDeliverFee;
    }

    public String getuPay() {
        return uPay;
    }

    public void setuPay(String uPay) {
        this.uPay = uPay;
    }

    public String getuInvoiceType() {
        return uInvoiceType;
    }

    public void setuInvoiceType(String uInvoiceType) {
        this.uInvoiceType = uInvoiceType;
    }

    public String getuInvoiceTitle() {
        return uInvoiceTitle;
    }

    public void setuInvoiceTitle(String uInvoiceTitle) {
        this.uInvoiceTitle = uInvoiceTitle;
    }
}
